package com.example.mallet;

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
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.backend.entity.group.ModelGroupMapper;
import com.example.mallet.backend.entity.set.ModelLearningSetMapper;
import com.example.mallet.databinding.FragmentHomeBinding;
import com.example.mallet.utils.AdapterGroup;
import com.example.mallet.utils.AdapterLearningSet;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.ModelGroup;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Terminology;
import com.example.mallet.utils.Utils;

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

        String credential = AuthenticationUtils.get(Objects.requireNonNull(requireContext()));
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
        Utils.hideItems(homeSv);

        homeSetsVp2 = binding.homeSetsViewPager;
        homeGroupsVp2 = binding.homeGroupsViewPager;
        Utils.disableItems(homeSetsVp2, homeGroupsVp2);

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
                Utils.showItems(homeSv);
                homeSv.startAnimation(fadeInAnimation);

                Utils.hideItems(progressBar);

                SetBasicDTO setBasicDTO = ResponseHandler.handleResponse(response);
                List<ModelLearningSet> modelLearningSets = ModelLearningSetMapper.from(setBasicDTO.sets());

                AdapterLearningSet adapterSets = new AdapterLearningSet(getActivity(), modelLearningSets, openActivityViewSet());

                homeSetsVp2.setAdapter(adapterSets);
                homeSetsVp2.setPageTransformer(Utils::applySwipeTransformer);

                homeSetsVp2.startAnimation(fadeInAnimation);

                if (modelLearningSets == null || modelLearningSets.isEmpty()) {
                    Utils.showItems(noSetsLl);
                    Utils.enableItems(noSetsLl);
                    Utils.hideItems(homeSetsVp2);
                    Utils.disableItems(homeSetsVp2);
                } else {
                    Utils.showItems(homeSetsVp2);
                    Utils.enableItems(homeSetsVp2);
                    Utils.hideItems(noSetsLl);
                    Utils.disableItems(noSetsLl);
                }
            }

            @Override
            public void onFailure(Call<SetBasicDTO> call, Throwable t) {
                if (attemptCount < MALLet.MAX_RETRY_ATTEMPTS) {
                    // Retry the operation
                    setupLearningSetsWithRestart(attemptCount + 1);

                } else {
                    // Maximum attempts reached, handle failure
                    Utils.showToast(getActivity(), "Network error");
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
                Utils.enableItems(homeGroupsVp2);

                GroupBasicDTO groupDTO = ResponseHandler.handleResponse(response);
                List<ModelGroup> modelGroups = ModelGroupMapper.from(groupDTO.groups());

                AdapterGroup adapterGroups = new AdapterGroup(getActivity(), modelGroups, openActivityViewGroup());
                homeGroupsVp2.setAdapter(adapterGroups);

                homeGroupsVp2.setPageTransformer(Utils::applySwipeTransformer);

                homeGroupsVp2.startAnimation(fadeInAnimation);

                if (modelGroups == null || modelGroups.isEmpty()) {
                    Utils.showItems(noGroupsLl);
                    Utils.enableItems(noGroupsLl);
                    Utils.hideItems(homeGroupsVp2);
                    Utils.disableItems(homeGroupsVp2);
                } else {
                    Utils.showItems(homeGroupsVp2);
                    Utils.enableItems(homeGroupsVp2);
                    Utils.hideItems(noGroupsLl);
                    Utils.disableItems(noGroupsLl);
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
                    Utils.showToast(getActivity(), "Network error");
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
            case 1 + 100 ->
                    new Terminology("Renewable Energy", "Energy derived from resources that are naturally replenished.", "Energia odnawialna");
            case 2 + 100 ->
                    new Terminology("Genetic Engineering", "Manipulating an organism's genes using biotechnology.", "Inżynieria genetyczna");
            case 3 + 100 ->
                    new Terminology("Distributed Ledger", "A consensus of replicated, shared, and synchronized digital data.", "Rozproszony rejestr");
            case 4 + 100 ->
                    new Terminology("Biometric Authentication", "Security method based on unique biological characteristics.", "Biometryczna autoryzacja");
            case 5 + 100 ->
                    new Terminology("Dark Web", "A part of the internet inaccessible to standard search engines.", "Ciemna sieć");
            case 6 + 100 ->
                    new Terminology("Genome Sequencing", "Determining the order of DNA nucleotides in a genome.", "Sekwencjonowanie genomu");
            case 7 + 100 ->
                    new Terminology("Neural Network", "A system modeled after the human brain's neural structure.", "Sieć neuronowa");
            case 8 + 100 ->
                    new Terminology("Green Computing", "Environmentally friendly use of computers and resources.", "Zielone obliczenia");
            case 9 + 100 ->
                    new Terminology("Augmented Intelligence", "Using technology to enhance human intelligence.", "Inteligencja wspomagana");
            case 10 + 100 ->
                    new Terminology("Supply Chain Management", "The handling of the entire production flow of a good or service.", "Zarządzanie łańcuchem dostaw");
            case 11 + 100 ->
                    new Terminology("Quantum Entanglement", "Quantum phenomenon where particles become connected.", "Splątanie kwantowe");
            case 12 + 100 ->
                    new Terminology("Bioinformatics", "Application of computer science to biological data.", "Bioinformatyka");
            case 13 + 100 ->
                    new Terminology("Smart Grid", "An electrical grid with advanced control and communication.", "Inteligentna sieć energetyczna");
            case 14 + 100 ->
                    new Terminology("Fintech", "Technology in the financial sector.", "Technologia finansowa");
            case 15 + 100 ->
                    new Terminology("Cyber-Physical System", "Integrated computer-based algorithms with physical processes.", "System cyber-fizyczny");
            case 16 + 100 ->
                    new Terminology("Cryptography", "The practice of secure communication.", "Kryptografia");
            case 17 + 100 ->
                    new Terminology("Precision Agriculture", "Using technology for optimized farming.", "Rolnictwo precyzyjne");
            case 18 + 100 ->
                    new Terminology("Dark Matter", "Non-luminous material in the universe.", "Ciemna materia");
            case 19 + 100 ->
                    new Terminology("Natural Language Processing", "Interpreting and generating human language by computers.", "Przetwarzanie języka naturalnego");
            case 20 + 100 ->
                    new Terminology("E-commerce", "Buying and selling goods and services over the internet.", "Handel elektroniczny");
            case 21 + 100 ->
                    new Terminology("Encryption", "Securing information by converting it into a code.", "Szyfrowanie");
            case 22 + 100 ->
                    new Terminology("Firewall", "A network security system that monitors and controls incoming and outgoing network traffic.", "Zapora ogniowa");
            case 23 + 100 ->
                    new Terminology("Algorithmic Trading", "Using algorithms to make trading decisions in financial markets.", "Handel algorytmiczny");
            case 24 + 100 ->
                    new Terminology("Open Source Software", "Software with a license allowing its source code to be freely available.", "Otwarte oprogramowanie");
            case 25 + 100 ->
                    new Terminology("User Experience (UX)", "The overall experience of a person using a product.", "Doświadczenie użytkownika (UX)");
            case 26 + 100 ->
                    new Terminology("Crowdfunding", "Raising funds from a large number of people.", "Finansowanie społecznościowe");
            case 27 + 100 ->
                    new Terminology("Agile Development", "An iterative and incremental approach to software development.", "Rozwój zwinny");
            case 28 + 100 ->
                    new Terminology("Internet Protocol (IP)", "A set of rules for sending and receiving data on the internet.", "Protokół internetowy (IP)");
            case 29 + 100 ->
                    new Terminology("Open Data", "Data that anyone can access, use, and share.", "Otwarte dane");
            case 30 + 100 ->
                    new Terminology("Smart City", "Using technology to enhance performance and well-being in urban areas.", "Inteligentne miasto");
            case 31 + 100 ->
                    new Terminology("API (Application Programming Interface)", "A set of rules allowing one software application to interact with another.", "Interfejs programowania aplikacji (API)");
            case 32 + 100 ->
                    new Terminology("Malware", "Malicious software designed to harm or exploit computers.", "Złośliwe oprogramowanie");
            case 33 + 100 ->
                    new Terminology("SaaS (Software as a Service)", "A software licensing and delivery model in which software is provided on a subscription basis.", "Oprogramowanie jako usługa");
            case 34 + 100 ->
                    new Terminology("Responsive Design", "Designing web pages to be easily viewed on a variety of devices and window or screen sizes.", "Projektowanie responsywne");
            case 35 + 100 ->
                    new Terminology("Metadata", "Data that provides information about other data.", "Metadane");
            case 36 + 100 ->
                    new Terminology("Phishing", "Fraudulent attempt to obtain sensitive information by disguising as a trustworthy entity.", "Phishing");
            case 37 + 100 ->
                    new Terminology("Latency", "The time delay between the initiation of a process and the occurrence of its effect.", "Opóźnienie");
            case 38 + 100 ->
                    new Terminology("Cyberbullying", "The use of electronic communication to bully a person.", "Cyberprzemoc");
            case 39 + 100 ->
                    new Terminology("Firewire", "A high-speed data transfer interface.", "Firewire");
            case 40 + 100 ->
                    new Terminology("Digital Signature", "An electronic signature used to authenticate the identity of a sender or document.", "Podpis cyfrowy");
            case 41 + 100 ->
                    new Terminology("Machine Vision", "Technology that enables computers to interpret visual information.", "Wizja maszynowa");
            case 42 + 100 ->
                    new Terminology("Rootkit", "A type of malicious software that provides unauthorized access to a computer.", "Rootkit");
            case 43 + 100 ->
                    new Terminology("Cloud Storage", "Storing data on remote servers accessed via the internet.", "Przechowywanie w chmurze");
            case 44 + 100 ->
                    new Terminology("Artificial General Intelligence (AGI)", "A type of artificial intelligence that can understand, learn, and apply knowledge across different domains.", "Sztuczna ogólna inteligencja (AGI)");
            case 45 + 100 ->
                    new Terminology("CAPTCHA", "A test to determine whether the user is human or a computer program.", "CAPTCHA");
            case 46 + 100 ->
                    new Terminology("Smart Home", "A residence that uses smart devices for automation and control.", "Inteligentny dom");
            case 47 + 100 ->
                    new Terminology("Machine Translation", "The use of computers to translate text from one language to another.", "Maszynowe tłumaczenie");
            case 48 + 100 ->
                    new Terminology("Data Mining", "Extracting patterns and knowledge from large sets of data.", "Eksploracja danych");
            case 49 + 100 ->
                    new Terminology("Cryptographic Hash Function", "A mathematical algorithm that transforms data into a fixed-size string of characters.", "Kryptograficzna funkcja skrótu");
            case 50 + 100 ->
                    new Terminology("Open Banking", "A financial services model that allows third-party access to financial information.", "Otwarte bankowość");
            case 51 + 100 ->
                    new Terminology("Dark Mode", "A display setting that uses a dark color scheme.", "Tryb ciemny");
            case 52 + 100 ->
                    new Terminology("Semantic Web", "An extension of the World Wide Web that enables data to be linked and understood by machines.", "Semantyczny internet");
            case 53 + 100 ->
                    new Terminology("Multi-factor Authentication", "A security process that requires multiple forms of identification for access.", "Wieloczynnikowa autoryzacja");
            case 54 + 100 ->
                    new Terminology("Edge Computing", "Processing data near the source of data generation.", "Edge Computing");
            case 55 + 100 ->
                    new Terminology("Biomimicry", "Design and production inspired by nature.", "Biomimikra");
            case 56 + 100 ->
                    new Terminology("Zero-Day Exploit", "An attack that targets a software vulnerability on the same day it becomes known.", "Atak zero-day");
            case 57 + 100 ->
                    new Terminology("Augmented Analytics", "Using machine learning and natural language processing to enhance data analytics.", "Rozszerzona analiza danych");
            case 58 + 100 ->
                    new Terminology("Zero Trust Security", "A security model that does not trust any entity inside or outside the network.", "Bezpieczeństwo Zero Trust");
            case 59 + 100 ->
                    new Terminology("Robot Process Automation (RPA)", "Using software robots to automate repetitive tasks.", "Automatyzacja procesów robotycznych");
            case 60 + 100 ->
                    new Terminology("Deep Learning", "A subset of machine learning that uses neural networks to simulate human-like decision-making.", "Głębokie uczenie");
            case 61 + 100 ->
                    new Terminology("Vulnerability", "A weakness that can be exploited by attackers.", "Podatność");
            case 62 + 100 ->
                    new Terminology("Smart Contract", "A self-executing contract with the terms directly written into code.", "Smart kontrakt");
            case 63 + 100 ->
                    new Terminology("Social Engineering", "Manipulating people to disclose confidential information.", "Inżynieria społeczna");
            case 64 + 100 ->
                    new Terminology("Quantum Encryption", "Using quantum mechanics to secure communication.", "Kwantowe szyfrowanie");
            case 65 + 100 ->
                    new Terminology("Digital Twin", "A digital representation of a physical object or system.", "Cyfrowy bliźniak");
            case 66 + 100 ->
                    new Terminology("Dark Fiber", "Unused or underused fiber-optic cable infrastructure.", "Ciemne włókno");
            case 67 + 100 ->
                    new Terminology("Autonomous Vehicles", "Self-driving vehicles that operate without human intervention.", "Pojazdy autonomiczne");
            case 68 + 100 ->
                    new Terminology("Robotic Process Automation (RPA)", "Automating repetitive business processes using software robots.", "Automatyzacja procesów robotycznych");
            case 69 + 100 ->
                    new Terminology("Digital Nomad", "A person who works remotely and is not bound to a specific location.", "Cyfrowy nomada");
            case 70 + 100 ->
                    new Terminology("Natural User Interface (NUI)", "An interface that allows users to interact using natural elements.", "Naturalny interfejs użytkownika");
            case 71 + 100 ->
                    new Terminology("Voice Recognition", "Technology that recognizes and interprets spoken language.", "Rozpoznawanie mowy");
            case 72 + 100 ->
                    new Terminology("Influencer Marketing", "Promoting products or services through individuals with a significant online following.", "Marketing influencerów");
            case 73 + 100 ->
                    new Terminology("Quantum Supremacy", "Theoretical point where quantum computers can solve problems faster than classical computers.", "Przewaga kwantowa");
            case 74 + 100 ->
                    new Terminology("Microservices", "A software architectural style where applications are composed of small, independent services.", "Mikroserwisy");
            case 75 + 100 ->
                    new Terminology("Blockchain Interoperability", "The ability of different blockchain systems to communicate and share data.", "Interoperacyjność blockchain");
            case 76 + 100 ->
                    new Terminology("Dark Web", "A part of the internet accessible only with specialized software.", "Ciemna sieć");
            case 77 + 100 ->
                    new Terminology("Containerization", "Encapsulating an application and its dependencies into a container.", "Konteneryzacja");
            case 78 + 100 ->
                    new Terminology("Cyber Hygiene", "Practices to maintain online security and protect against cyber threats.", "Cyber higiena");
            case 79 + 100 ->
                    new Terminology("Data Lake", "A large storage repository that holds raw data in its native format.", "Jezioro danych");
            case 80 + 100 ->
                    new Terminology("Quantum Key Distribution", "Using quantum mechanics to secure communication keys.", "Kwantowe rozdział klucza");
            case 81 + 100 ->
                    new Terminology("Pangolin", "A scaly mammal native to Asia and Africa.", "Pangolin");
            case 82 + 100 ->
                    new Terminology("Jupiter", "The largest planet in our solar system.", "Jowisz");
            case 83 + 100 ->
                    new Terminology("Tectonic Plates", "Large sections of the Earth's crust that move and interact.", "Płyty tektoniczne");
            case 84 + 100 ->
                    new Terminology("Orion Nebula", "A region of space where new stars are forming.", "Mgławica Oriona");
            case 85 + 100 ->
                    new Terminology("Remote Work", "Working outside of a traditional office setting.", "Praca zdalna");
            case 86 + 100 ->
                    new Terminology("Mars Rover", "A robotic vehicle exploring the surface of Mars.", "Rower marsjański");
            case 87 + 100 ->
                    new Terminology("Blue Whale", "The largest animal on Earth.", "Błękitny wieloryb");
            case 88 + 100 ->
                    new Terminology("Saturn", "A ringed planet in our solar system.", "Saturn");
            case 89 + 100 ->
                    new Terminology("Desalination", "The process of removing salt and other impurities from water.", "Odsalanie");
            case 90 + 100 ->
                    new Terminology("Space Debris", "Man-made objects in orbit around Earth that no longer serve a purpose.", "Śmieci kosmiczne");
            case 91 + 100 ->
                    new Terminology("Black Hole", "A region of spacetime exhibiting gravitational acceleration so strong that nothing can escape it.", "Czarna dziura");
            case 92 + 100 ->
                    new Terminology("Solar Flare", "A sudden, rapid, and intense variation in brightness on the Sun's surface.", "Rozerwanie słoneczne");
            case 93 + 100 ->
                    new Terminology("Antarctica", "The southernmost continent, home to the South Pole.", "Antarktyka");
            case 94 + 100 ->
                    new Terminology("Space Station", "A large spacecraft in orbit where astronauts live and work.", "Stacja kosmiczna");
            case 95 + 100 ->
                    new Terminology("Lunar Eclipse", "When the Earth passes between the Sun and the Moon, casting a shadow on the Moon.", "Zatmienie księżyca");
            case 96 + 100 ->
                    new Terminology("Ecosystem", "A community of living organisms in conjunction with non-living components.", "Ekosystem");
            case 97 + 100 ->
                    new Terminology("Neptune", "The eighth and farthest known planet from the Sun in the solar system.", "Neptun");
            case 98 + 100 ->
                    new Terminology("Greenhouse Effect", "The trapping of the Sun's warmth in a planet's lower atmosphere.", "Efekt cieplarniany");
            case 99 + 100 ->
                    new Terminology("Supernova", "A powerful and luminous stellar explosion.", "Supernowa");
            case 100 + 100 ->
                    new Terminology("Human Resources", "The department within an organization that manages personnel.", "Zasoby ludzkie");
            case 101 + 100 ->
                    new Terminology("Arctic Circle", "One of the five major circles of latitude that mark maps of the Earth.", "Koło podbiegunowe północne");
            case 102 + 100 ->
                    new Terminology("Exoplanet", "A planet located outside our solar system.", "Egzoplaneta");
            case 103 + 100 ->
                    new Terminology("Comet", "A small celestial body that orbits the Sun.", "Kometa");
            case 104 + 100 ->
                    new Terminology("Geothermal Energy", "Energy derived from the heat of the Earth's interior.", "Energia geotermalna");
            case 105 + 100 ->
                    new Terminology("Telescope", "An optical instrument that gathers and magnifies light to observe distant objects.", "Teleskop");
            case 106 + 100 ->
                    new Terminology("Galaxy", "A system of millions or billions of stars, together with gas and dust.", "Galaktyka");
            case 107 + 100 ->
                    new Terminology("Biodiversity", "The variety of life in a particular habitat or ecosystem.", "Bioróżnorodność");
            case 108 + 100 ->
                    new Terminology("Telecommuting", "Working from a location other than the office, often from home.", "Telepraca");
            case 109 + 100 ->
                    new Terminology("Quasar", "A massive and extremely remote celestial object, emitting exceptionally large amounts of energy.", "Kwazar");
            case 110 + 100 ->
                    new Terminology("Oceanography", "The study of the physical and biological aspects of the ocean.", "Oceanografia");
            case 111 + 100 ->
                    new Terminology("Demography", "The statistical study of populations, especially human populations.", "Demografia");
            case 112 + 100 ->
                    new Terminology("Gender Pay Gap", "The difference between the average earnings of men and women in the workplace.", "Różnica płac między płciami");
            case 113 + 100 ->
                    new Terminology("Cultural Diversity", "The presence of various cultural groups with distinct values and traditions.", "Różnorodność kulturowa");
            case 114 + 100 ->
                    new Terminology("Social Entrepreneurship", "Using business skills to solve social issues.", "Przedsiębiorczość społeczna");
            case 115 + 100 ->
                    new Terminology("Generational Gap", "Differences in beliefs, values, and attitudes between different generations.", "Różnice pokoleniowe");
            case 116 + 100 ->
                    new Terminology("Emotional Intelligence", "The ability to understand and manage one's own emotions and the emotions of others.", "Inteligencja emocjonalna");
            case 117 + 100 ->
                    new Terminology("Work-Life Balance", "Maintaining a balance between one's career and personal life.", "Równowaga między pracą a życiem prywatnym");
            case 118 + 100 ->
                    new Terminology("Civic Engagement", "Active participation in community and public affairs.", "Zaangażowanie obywatelskie");
            case 119 + 100 ->
                    new Terminology("Ageism", "Discrimination or prejudice based on a person's age.", "Egocentryzm wiekowy");
            case 120 + 100 ->
                    new Terminology("Cultural Competence", "The ability to understand, communicate, and interact effectively with people from different cultures.", "Kompetencje kulturowe");
            case 121 + 100 ->
                    new Terminology("Freelancer", "A person who works as a self-employed individual, often on a project basis.", "Wolny strzelec");
            case 122 + 100 ->
                    new Terminology("Microaggression", "Subtle and often unintentional acts of discrimination.", "Mikroagresja");
            case 123 + 100 ->
                    new Terminology("Allyship", "Supporting and advocating for marginalized groups.", "Sojusznicza postawa");
            case 124 + 100 ->
                    new Terminology("Nomophobia", "The fear of being without a mobile phone or being unable to use it.", "Nomofobia");
            case 125 + 100 ->
                    new Terminology("Networking", "Building and maintaining relationships for professional and social purposes.", "Nawiązywanie kontaktów");
            case 126 + 100 ->
                    new Terminology("Mentorship", "A relationship where a more experienced individual guides and supports a less experienced person.", "Mentorstwo");
            case 127 + 100 ->
                    new Terminology("Diversity and Inclusion", "Creating an environment that values and includes people of different backgrounds and experiences.", "Różnorodność i inkluzja");
            case 128 + 100 ->
                    new Terminology("Social Media Influencer", "A person who uses social media to influence and engage with a large audience.", "Influencer mediów społecznościowych");
            case 129 + 100 ->
                    new Terminology("Imposter Syndrome", "Feeling like a fraud despite evidence of success.", "Syndrom oszusta");
            case 130 + 100 ->
                    new Terminology("Soft Skills", "Personal attributes that enable someone to interact effectively with others.", "Umiejętności miękkie");
            case 131 + 100 ->
                    new Terminology("Career Development", "The process of managing one's career path and professional growth.", "Rozwój kariery");
            case 132 + 100 ->
                    new Terminology("Introvert", "A person who prefers solitary activities and tends to be reserved in social situations.", "Introwertyk");
            case 133 + 100 ->
                    new Terminology("Burnout", "A state of emotional, physical, and mental exhaustion caused by prolonged stress.", "Wypalenie zawodowe");
            case 134 + 100 ->
                    new Terminology("Glass Ceiling", "An invisible barrier preventing certain groups from advancing in their careers.", "Szkło jakby w sklepieniu");
            case 135 + 100 ->
                    new Terminology("Freemium", "A business model offering basic services for free and charging for premium features.", "Freemium");
            case 136 + 100 ->
                    new Terminology("Workplace Harassment", "Unwanted conduct that creates a hostile work environment.", "Nękanie w miejscu pracy");
            case 137 + 100 ->
                    new Terminology("Digital Nomad", "A person who works remotely and is not bound to a specific location.", "Cyfrowy nomada");
            case 138 + 100 ->
                    new Terminology("Cross-Cultural Communication", "Communication between people from different cultural backgrounds.", "Komunikacja międzykulturowa");
            case 139 + 100 ->
                    new Terminology("Intrapreneurship", "Entrepreneurship within a large corporation, where employees act like entrepreneurs.", "Intrapreneurship");
            case 140 + 100 ->
                    new Terminology("Unconscious Bias", "Prejudice or judgments that occur without awareness or intention.", "Beświadoma uprzedzenie");
            case 141 + 100 ->
                    new Terminology("Professional Development", "Activities to improve skills and knowledge for career advancement.", "Rozwój zawodowy");
            case 142 + 100 ->
                    new Terminology("Telecommuting", "Working from a location other than the office, often from home.", "Telepraca");
            case 143 + 100 ->
                    new Terminology("Social Capital", "The networks and relationships that enable social cooperation and collaboration.", "Kapitał społeczny");
            case 144 + 100 ->
                    new Terminology("Holacracy", "A management philosophy emphasizing self-management and decentralized decision-making.", "Holakracja");
            case 145 + 100 ->
                    new Terminology("Personal Branding", "The process of creating and managing an individual's public image.", "Kreowanie marki osobistej");
            case 146 + 100 ->
                    new Terminology("Digital Literacy", "The ability to use, understand, and evaluate digital information.", "Umiejętność korzystania z technologii cyfrowych");
            case 147 + 100 ->
                    new Terminology("Reverse Mentoring", "A mentoring relationship where a younger or less experienced person mentors an older or more experienced individual.", "Mentorstwo odwrócone");
            case 148 + 100 ->
                    new Terminology("Workforce Automation", "The use of technology to perform tasks without human intervention.", "Automatyzacja siły roboczej");
            case 149 + 100 ->
                    new Terminology("Corporate Social Responsibility (CSR)", "A company's commitment to contributing to the well-being of society.", "Społeczna odpowiedzialność biznesu");
            case 150 + 100 ->
                    new Terminology("Farm-to-Table", "A food movement promoting locally sourced produce.", "Z pole na stół");
            case 151 + 100 ->
                    new Terminology("Hybrid Car", "A vehicle with a combination of a gasoline engine and an electric motor.", "Samochód hybrydowy");
            case 152 + 100 ->
                    new Terminology("Foodie", "A person who has an avid interest in food, especially in tasting different cuisines.", "Miłośnik jedzenia");
            case 153 + 100 ->
                    new Terminology("Electric Vehicle (EV)", "A vehicle powered by electricity stored in batteries.", "Samochód elektryczny");
            case 154 + 100 ->
                    new Terminology("Sustainable Agriculture", "Farming practices that consider environmental impact and resource conservation.", "Rolnictwo zrównoważone");
            case 155 + 100 ->
                    new Terminology("Carburetor", "A device that blends air and fuel for an internal combustion engine.", "Gaźnik");
            case 156 + 100 ->
                    new Terminology("Locavore", "A person who primarily eats food that is locally produced.", "Lokalista");
            case 157 + 100 ->
                    new Terminology("Autonomous Vehicle", "A vehicle capable of navigating without human input.", "Pojazd autonomiczny");
            case 158 + 100 ->
                    new Terminology("Food Truck", "A mobile kitchen that sells food on the street.", "Food truck");
            case 159 + 100 ->
                    new Terminology("Hatchback", "A car body style with a rear door that swings upward.", "Hatchback");
            case 160 + 100 ->
                    new Terminology("Food Pairing", "The practice of combining different foods to enhance their flavors.", "Dobieranie potraw");
            case 161 + 100 ->
                    new Terminology("Convertible", "A car with a folding or retracting roof.", "Kabriolet");
            case 162 + 100 ->
                    new Terminology("Gastronomy", "The practice of choosing, cooking, and eating good food.", "Gastronomia");
            case 163 + 100 ->
                    new Terminology("SUV (Sport Utility Vehicle)", "A large vehicle designed for off-road use with four-wheel drive.", "Samochód typu SUV");
            case 164 + 100 ->
                    new Terminology("Umami", "A category of taste corresponding to the flavor of savory foods.", "Umami");
            case 165 + 100 ->
                    new Terminology("Fuel Injection", "A system that delivers fuel into an internal combustion engine.", "Wtrysk paliwa");
            case 166 + 100 ->
                    new Terminology("Farmers' Market", "A place where local farmers sell their produce directly to consumers.", "Targ rolny");
            case 167 + 100 ->
                    new Terminology("Crossover", "A vehicle that combines features of a car and an SUV.", "Crossover");
            case 168 + 100 ->
                    new Terminology("Food Critic", "A person who evaluates and critiques food, often professionally.", "Krytyk kulinarny");
            case 169 + 100 ->
                    new Terminology("MPG (Miles Per Gallon)", "A measure of a car's fuel efficiency.", "MPG (Mile na galon)");
            case 170 + 100 ->
                    new Terminology("Street Food", "Ready-to-eat food sold on the streets or in public places.", "Jedzenie uliczne");
            case 171 + 100 ->
                    new Terminology("Compact Car", "A small-sized car typically with better fuel efficiency.", "Samochód kompaktowy");
            case 172 + 100 ->
                    new Terminology("Locally Sourced", "Products that come from nearby or local producers.", "Lokalnie pozyskiwane");
            case 173 + 100 ->
                    new Terminology("Traffic Jam", "A situation where vehicles are heavily congested and moving slowly.", "Korek uliczny");
            case 174 + 100 ->
                    new Terminology("Fast Casual", "A dining style that offers higher-quality food than fast food restaurants but is still quick.", "Szybka kawiarnia");
            case 175 + 100 ->
                    new Terminology("Compact SUV", "A smaller-sized SUV with a focus on fuel efficiency and city driving.", "Kompaktowy SUV");
            case 176 + 100 ->
                    new Terminology("Food Allergen", "A substance that causes an allergic reaction in some individuals.", "Alergen pokarmowy");
            case 177 + 100 ->
                    new Terminology("Road Trip", "A long journey on the road, typically for pleasure.", "Wycieczka samochodowa");
            case 178 + 100 ->
                    new Terminology("Food Pyramid", "A visual representation of a balanced diet.", "Piramida żywienia");
            case 179 + 100 ->
                    new Terminology("Car Maintenance", "Regular upkeep and repairs to keep a vehicle in good condition.", "Konserwacja samochodu");
            case 180 + 100 ->
                    new Terminology("City Planning", "The design and organization of urban areas for sustainable living.", "Planowanie miejskie");
            case 181 + 100 ->
                    new Terminology("Street Art", "Visual art created in public locations, often without official permission.", "Sztuka uliczna");
            case 182 + 100 ->
                    new Terminology("Food Wastage", "The discarding of food that could have been eaten.", "Marnotrawstwo jedzenia");
            case 183 + 100 ->
                    new Terminology("Car Insurance", "A policy that provides financial protection in case of a car accident.", "Ubezpieczenie samochodu");
            case 184 + 100 ->
                    new Terminology("Urbanization", "The increasing population and infrastructure development in urban areas.", "Urbanizacja");
            case 185 + 100 ->
                    new Terminology("Gourmet", "High-quality and sophisticated food.", "Gurmand");
            case 186 + 100 ->
                    new Terminology("Automotive Industry", "The industry involved in the design, development, and production of vehicles.", "Branża motoryzacyjna");
            case 187 + 100 ->
                    new Terminology("Cityscape", "The visual appearance of a city, including its buildings and landmarks.", "Panorama miejska");
            case 188 + 100 ->
                    new Terminology("Comfort Food", "Food that provides a sense of well-being and is often associated with nostalgia.", "Jedzenie kojące");
            case 189 + 100 ->
                    new Terminology("Hydrogen Fuel Cell", "A technology that uses hydrogen to generate electricity for a vehicle.", "Ogniwo paliwowe na wodór");
            case 190 + 100 ->
                    new Terminology("Urban Renewal", "The redevelopment of areas within a city to improve infrastructure and living conditions.", "Rewitalizacja miejska");
            default ->
                    new Terminology("Urban Renewal", "The redevelopment of areas within a city to improve infrastructure and living conditions.", "Rewitalizacja miejska");
        };
    }
}