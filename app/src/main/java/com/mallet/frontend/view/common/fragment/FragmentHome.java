package com.mallet.frontend.view.common.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.agh.api.GroupBasicDTO;
import com.agh.api.SetBasicDTO;
import com.mallet.MALLet;
import com.mallet.R;
import com.mallet.backend.client.configuration.ResponseHandler;
import com.mallet.backend.client.user.boundary.UserServiceImpl;
import com.mallet.backend.mapper.group.ModelGroupMapper;
import com.mallet.backend.mapper.set.ModelLearningSetMapper;
import com.mallet.databinding.FragmentHomeBinding;
import com.mallet.frontend.model.flashcard.Terminology;
import com.mallet.frontend.model.group.AdapterGroup;
import com.mallet.frontend.model.group.ModelGroup;
import com.mallet.frontend.model.set.AdapterLearningSet;
import com.mallet.frontend.model.set.ModelLearningSet;
import com.mallet.frontend.security.CredentialsHandler;
import com.mallet.frontend.utils.ViewUtils;
import com.mallet.frontend.view.common.activity.ActivityMain;
import com.mallet.frontend.view.group.ActivityCreateGroup;
import com.mallet.frontend.view.group.ActivityViewGroup;
import com.mallet.frontend.view.set.ActivityEditLearningSet;
import com.mallet.frontend.view.set.ActivityViewLearningSet;
import com.mallet.frontend.view.userlibrary.FragmentUserLibrary;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private ActivityMain activityMain;
    private FragmentHomeBinding binding;
    private UserServiceImpl userService;
    private ViewPager2 homeGroupsVp2;
    private ViewPager2 homeSetsVp2;
    private ProgressBar progressBar;
    private Animation fadeInAnimation;
    private ScrollView homeSv;
    private FragmentUserLibrary fragmentUserLibrary;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ActivityMain) {
            activityMain = (ActivityMain) context;
            fadeInAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in);
        } else {
            // Handle the case where the activity does not implement the method
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        String credential = CredentialsHandler.get(Objects.requireNonNull(requireContext()));
        this.userService = new UserServiceImpl(credential);

        setupContents();
        setupWordOfTheDay();
        activityMain = (ActivityMain) getActivity();

        Executors.newFixedThreadPool(1).submit(this::setupGroups);
        Executors.newFixedThreadPool(1).submit(this::setupLearningSets);

        return binding.getRoot();
    }

    private void setupWordOfTheDay() {
        TextView homeWordOfTheDayDefinitionTv = binding.homeWordOfTheDayDefinitionTv;
        TextView homeWordOfTheDayTermTv = binding.homeWordOfTheDayTermTv;
        TextView homeWordOfTheDayTranslationTv = binding.homeWordOfTheDayTranslationTv;

        Terminology wordOfDay = getWordOfDay();
        homeWordOfTheDayTranslationTv.setText(wordOfDay.getTermTranslation());
        homeWordOfTheDayTermTv.setText(wordOfDay.getTermName());
        homeWordOfTheDayDefinitionTv.setText(wordOfDay.getTermDefinition());
    }

    @Override
    public void onResume() {
        super.onResume();
        setupGroups();
        setupLearningSets();
    }

    private LinearLayout noSetsLl, noGroupsLl;

    private void setupContents() {
        homeSv = binding.homeSv;
        ViewUtils.hideItems(homeSv);

        homeSetsVp2 = binding.homeSetsViewPager;
        homeGroupsVp2 = binding.homeGroupsViewPager;
        ViewUtils.disableItems(homeSetsVp2, homeGroupsVp2);

        binding.homeSetViewAllTv.setOnClickListener(v -> showAllItems(0));
        binding.homeGroupViewAllTv.setOnClickListener(v -> showAllItems(1));

        progressBar = binding.fragmentHomeProgressBar;

        noSetsLl = binding.homeNoSetsLl;
        noGroupsLl = binding.homeNoGroupsLl;

        TextView addSetBtn = binding.homeAddSetTv;
        addSetBtn.setOnClickListener(v -> createNewSet());

        TextView addGroupBtn = binding.homeAddGroupTv;
        addGroupBtn.setOnClickListener(v -> createNewGroup());
    }

    private void createNewSet() {
        Intent intent = new Intent(requireContext(), ActivityEditLearningSet.class);

        //intent.putExtra("isSetNew", true);

        startActivity(intent);
    }

    private void createNewGroup() {
        Intent intent = new Intent(requireContext(), ActivityCreateGroup.class);
        startActivity(intent);
    }

    private void setupLearningSetsWithRestart(int attemptCount) {
        userService.getUserSets(0, 5, new Callback<SetBasicDTO>() {
            @Override
            public void onResponse(Call<SetBasicDTO> call, Response<SetBasicDTO> response) {
                if (Objects.isNull(getView())) {
                    return;
                }
                    ViewUtils.showItems(homeSv);
                    homeSv.startAnimation(fadeInAnimation);

                    ViewUtils.hideItems(progressBar);

                    SetBasicDTO setBasicDTO = ResponseHandler.handleResponse(response);
                    List<ModelLearningSet> modelLearningSets = ModelLearningSetMapper.from(setBasicDTO.sets());

                    AdapterLearningSet adapterSets = new AdapterLearningSet(getActivity(), modelLearningSets, openActivityViewSet());

                    homeSetsVp2.setAdapter(adapterSets);
                    homeSetsVp2.setPageTransformer(ViewUtils::applySwipeTransformer);

                    homeSetsVp2.startAnimation(fadeInAnimation);

                    if (modelLearningSets == null || modelLearningSets.isEmpty()) {
                        ViewUtils.showItems(noSetsLl);
                        ViewUtils.enableItems(noSetsLl);
                        ViewUtils.hideItems(homeSetsVp2);
                        ViewUtils.disableItems(homeSetsVp2);
                    } else {
                        ViewUtils.showItems(homeSetsVp2);
                        ViewUtils.enableItems(homeSetsVp2);
                        ViewUtils.hideItems(noSetsLl);
                        ViewUtils.disableItems(noSetsLl);
                    }
            }
            @Override
            public void onFailure(Call<SetBasicDTO> call, Throwable t) {
                if (attemptCount < MALLet.MAX_RETRY_ATTEMPTS) {
                    // Retry the operation
                    setupLearningSetsWithRestart(attemptCount + 1);

                } else {
                    // Maximum attempts reached, handle failure
                    ViewUtils.showToast(getActivity(), "Network error");
                }
            }
        });
    }

    private void setupLearningSets() {
        setupLearningSetsWithRestart(0);
    }

    @NonNull
    private AdapterLearningSet.OnLearningSetClickListener openActivityViewSet() {
        return learningSet -> {
            Intent intent = new Intent(getActivity(), ActivityViewLearningSet.class);

            intent.putExtra("learningSet", learningSet);
            intent.putExtra("setId", learningSet.getId());

            intent.putExtra("isSetNew", false);
            intent.putExtra("isUserSet", true);
            intent.putExtra("isSetInGroup", false);

            startActivity(intent);
        };
    }

    private void setupGroupsWithRestart(int attemptCount) {
        userService.getUserGroups(0, 5, new Callback<GroupBasicDTO>() {
            @Override
            public void onResponse(Call<GroupBasicDTO> call, Response<GroupBasicDTO> response) {
                if (Objects.isNull(getView())) {
                    return;
                }
                ViewUtils.enableItems(homeGroupsVp2);

                GroupBasicDTO groupDTO = ResponseHandler.handleResponse(response);
                List<ModelGroup> modelGroups = ModelGroupMapper.from(groupDTO.groups());

                AdapterGroup adapterGroups = new AdapterGroup(getActivity(), modelGroups, openActivityViewGroup());
                homeGroupsVp2.setAdapter(adapterGroups);

                homeGroupsVp2.setPageTransformer(ViewUtils::applySwipeTransformer);

                homeGroupsVp2.startAnimation(fadeInAnimation);

                if (modelGroups == null || modelGroups.isEmpty()) {
                    ViewUtils.showItems(noGroupsLl);
                    ViewUtils.enableItems(noGroupsLl);
                    ViewUtils.hideItems(homeGroupsVp2);
                    ViewUtils.disableItems(homeGroupsVp2);
                } else {
                    ViewUtils.showItems(homeGroupsVp2);
                    ViewUtils.enableItems(homeGroupsVp2);
                    ViewUtils.hideItems(noGroupsLl);
                    ViewUtils.disableItems(noGroupsLl);
                }
            }

            @Override
            public void onFailure(Call<GroupBasicDTO> call, Throwable t) {
                if (attemptCount < MAX_RETRY_ATTEMPTS) {
                    // System.out.println(attemptCount);
                    // Retry the operation
                    setupGroupsWithRestart(attemptCount + 1);

                } else {
                    // Maximum attempts reached, handle failure
                    ViewUtils.showToast(getActivity(), "Network error");
                }
            }
        });
    }

    private void setupGroups() {
        setupGroupsWithRestart(0);
    }

    @NonNull
    private AdapterGroup.OnGroupClickListener openActivityViewGroup() {
        return v -> {
            Intent intent = new Intent(getActivity(), ActivityViewGroup.class);

            intent.putExtra("groupId", v.getId());
            intent.putExtra("groupName", v.getGroupName());
            intent.putExtra("isUserSet", true);

            startActivity(intent);
        };
    }

    private void showAllItems(int selectedTabIndex) {
        FragmentUserLibrary fragmentUserLibrary = FragmentUserLibrary.newInstance(selectedTabIndex);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFl, fragmentUserLibrary);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private Terminology getWordOfDay() {
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        return switch (dayOfYear) {
            case 1 + 300 ->
                    new Terminology("Renewable Energy", "Energy derived from resources that are naturally replenished.", "Energia odnawialna");
            case 2 + 300 ->
                    new Terminology("Genetic Engineering", "Manipulating an organism's genes using biotechnology.", "Inżynieria genetyczna");
            case 3 + 300 ->
                    new Terminology("Distributed Ledger", "A consensus of replicated, shared, and synchronized digital data.", "Rozproszony rejestr");
            case 4 + 300 ->
                    new Terminology("Biometric Authentication", "Security method based on unique biological characteristics.", "Biometryczna autoryzacja");
            case 5 + 300 ->
                    new Terminology("Dark Web", "A part of the internet inaccessible to standard search engines.", "Ciemna sieć");
            case 6 + 300 ->
                    new Terminology("Genome Sequencing", "Determining the order of DNA nucleotides in a genome.", "Sekwencjonowanie genomu");
            case 7 + 300 ->
                    new Terminology("Neural Network", "A system modeled after the human brain's neural structure.", "Sieć neuronowa");
            case 8 + 300 ->
                    new Terminology("Green Computing", "Environmentally friendly use of computers and resources.", "Zielone obliczenia");
            case 9 + 300 ->
                    new Terminology("Augmented Intelligence", "Using technology to enhance human intelligence.", "Inteligencja wspomagana");
            case 10 + 300 ->
                    new Terminology("Supply Chain Management", "The handling of the entire production flow of a good or service.", "Zarządzanie łańcuchem dostaw");
            case 11 + 300 ->
                    new Terminology("Quantum Entanglement", "Quantum phenomenon where particles become connected.", "Splątanie kwantowe");
            case 12 + 300 ->
                    new Terminology("Bioinformatics", "Application of computer science to biological data.", "Bioinformatyka");
            case 13 + 300 ->
                    new Terminology("Smart Grid", "An electrical grid with advanced control and communication.", "Inteligentna sieć energetyczna");
            case 14 + 300 ->
                    new Terminology("Fintech", "Technology in the financial sector.", "Technologia finansowa");
            case 15 + 300 ->
                    new Terminology("Cyber-Physical System", "Integrated computer-based algorithms with physical processes.", "System cyber-fizyczny");
            case 16 + 300 ->
                    new Terminology("Cryptography", "The practice of secure communication.", "Kryptografia");
            case 17 + 300 ->
                    new Terminology("Precision Agriculture", "Using technology for optimized farming.", "Rolnictwo precyzyjne");
            case 18 + 300 ->
                    new Terminology("Dark Matter", "Non-luminous material in the universe.", "Ciemna materia");
            case 19 + 300 ->
                    new Terminology("Natural Language Processing", "Interpreting and generating human language by computers.", "Przetwarzanie języka naturalnego");
            case 20 + 300 ->
                    new Terminology("E-commerce", "Buying and selling goods and services over the internet.", "Handel elektroniczny");
            case 21 + 300 ->
                    new Terminology("Encryption", "Securing information by converting it into a code.", "Szyfrowanie");
            case 22 + 300 ->
                    new Terminology("Firewall", "A network security system that monitors and controls incoming and outgoing network traffic.", "Zapora ogniowa");
            case 23 + 300 ->
                    new Terminology("Algorithmic Trading", "Using algorithms to make trading decisions in financial markets.", "Handel algorytmiczny");
            case 24 + 300 ->
                    new Terminology("Open Source Software", "Software with a license allowing its source code to be freely available.", "Otwarte oprogramowanie");
            case 25 + 300 ->
                    new Terminology("User Experience (UX)", "The overall experience of a person using a product.", "Doświadczenie użytkownika (UX)");
            case 26 + 300 ->
                    new Terminology("Crowdfunding", "Raising funds from a large number of people.", "Finansowanie społecznościowe");
            case 27 + 300 ->
                    new Terminology("Agile Development", "An iterative and incremental approach to software development.", "Rozwój zwinny");
            case 28 + 300 ->
                    new Terminology("Internet Protocol (IP)", "A set of rules for sending and receiving data on the internet.", "Protokół internetowy (IP)");
            case 29 + 300 ->
                    new Terminology("Open Data", "Data that anyone can access, use, and share.", "Otwarte dane");
            case 30 + 300 ->
                    new Terminology("Smart City", "Using technology to enhance performance and well-being in urban areas.", "Inteligentne miasto");
            case 31 + 300 ->
                    new Terminology("API (Application Programming Interface)", "A set of rules allowing one software application to interact with another.", "Interfejs programowania aplikacji (API)");
            case 32 + 300 ->
                    new Terminology("Malware", "Malicious software designed to harm or exploit computers.", "Złośliwe oprogramowanie");
            case 33 + 300 ->
                    new Terminology("SaaS (Software as a Service)", "A software licensing and delivery model in which software is provided on a subscription basis.", "Oprogramowanie jako usługa");
            case 34 + 300 ->
                    new Terminology("Responsive Design", "Designing web pages to be easily viewed on a variety of devices and window or screen sizes.", "Projektowanie responsywne");
            case 35 + 300 ->
                    new Terminology("Metadata", "Data that provides information about other data.", "Metadane");
            case 36 + 300 ->
                    new Terminology("Phishing", "Fraudulent attempt to obtain sensitive information by disguising as a trustworthy entity.", "Phishing");
            case 37 + 300 ->
                    new Terminology("Latency", "The time delay between the initiation of a process and the occurrence of its effect.", "Opóźnienie");
            case 38 + 300 ->
                    new Terminology("Cyberbullying", "The use of electronic communication to bully a person.", "Cyberprzemoc");
            case 39 + 300 ->
                    new Terminology("Firewire", "A high-speed data transfer interface.", "Firewire");
            case 40 + 300 ->
                    new Terminology("Digital Signature", "An electronic signature used to authenticate the identity of a sender or document.", "Podpis cyfrowy");
            case 41 + 300 ->
                    new Terminology("Machine Vision", "Technology that enables computers to interpret visual information.", "Wizja maszynowa");
            case 42 + 300 ->
                    new Terminology("Rootkit", "A type of malicious software that provides unauthorized access to a computer.", "Rootkit");
            case 43 + 300 ->
                    new Terminology("Cloud Storage", "Storing data on remote servers accessed via the internet.", "Przechowywanie w chmurze");
            case 44 + 300 ->
                    new Terminology("Artificial General Intelligence (AGI)", "A type of artificial intelligence that can understand, learn, and apply knowledge across different domains.", "Sztuczna ogólna inteligencja (AGI)");
            case 45 + 300 ->
                    new Terminology("CAPTCHA", "A test to determine whether the user is human or a computer program.", "CAPTCHA");
            case 46 + 300 ->
                    new Terminology("Smart Home", "A residence that uses smart devices for automation and control.", "Inteligentny dom");
            case 47 + 300 ->
                    new Terminology("Machine Translation", "The use of computers to translate text from one language to another.", "Maszynowe tłumaczenie");
            case 48 + 300 ->
                    new Terminology("Data Mining", "Extracting patterns and knowledge from large sets of data.", "Eksploracja danych");
            case 49 + 300 ->
                    new Terminology("Cryptographic Hash Function", "A mathematical algorithm that transforms data into a fixed-size string of characters.", "Kryptograficzna funkcja skrótu");
            case 50 + 300 ->
                    new Terminology("Open Banking", "A financial services model that allows third-party access to financial information.", "Otwarte bankowość");
            case 51 + 300 ->
                    new Terminology("Dark Mode", "A display setting that uses a dark color scheme.", "Tryb ciemny");
            case 52 + 300 ->
                    new Terminology("Semantic Web", "An extension of the World Wide Web that enables data to be linked and understood by machines.", "Semantyczny internet");
            case 53 + 300 ->
                    new Terminology("Multi-factor Authentication", "A security process that requires multiple forms of identification for access.", "Wieloczynnikowa autoryzacja");
            case 54 + 300 ->
                    new Terminology("Edge Computing", "Processing data near the source of data generation.", "Edge Computing");
            case 55 + 300 ->
                    new Terminology("Biomimicry", "Design and production inspired by nature.", "Biomimikra");
            case 56 + 300 ->
                    new Terminology("Zero-Day Exploit", "An attack that targets a software vulnerability on the same day it becomes known.", "Atak zero-day");
            case 57 + 300 ->
                    new Terminology("Augmented Analytics", "Using machine learning and natural language processing to enhance data analytics.", "Rozszerzona analiza danych");
            case 58 + 300 ->
                    new Terminology("Zero Trust Security", "A security model that does not trust any entity inside or outside the network.", "Bezpieczeństwo Zero Trust");
            case 59 + 300 ->
                    new Terminology("Robot Process Automation (RPA)", "Using software robots to automate repetitive tasks.", "Automatyzacja procesów robotycznych");
            case 60 + 300 ->
                    new Terminology("Deep Learning", "A subset of machine learning that uses neural networks to simulate human-like decision-making.", "Głębokie uczenie");
            case 61 + 300 ->
                    new Terminology("Vulnerability", "A weakness that can be exploited by attackers.", "Podatność");
            case 62 + 300 ->
                    new Terminology("Smart Contract", "A self-executing contract with the terms directly written into code.", "Smart kontrakt");
            case 63 + 300 ->
                    new Terminology("Social Engineering", "Manipulating people to disclose confidential information.", "Inżynieria społeczna");
            case 64 + 300 ->
                    new Terminology("Quantum Encryption", "Using quantum mechanics to secure communication.", "Kwantowe szyfrowanie");
            case 65 + 300 ->
                    new Terminology("Digital Twin", "A digital representation of a physical object or system.", "Cyfrowy bliźniak");
            case 66 + 300 ->
                    new Terminology("Dark Fiber", "Unused or underused fiber-optic cable infrastructure.", "Ciemne włókno");
            default ->
                    new Terminology("Urban Renewal", "The redevelopment of areas within a city to improve infrastructure and living conditions.", "Rewitalizacja miejska");
        };
    }
}