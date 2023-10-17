package com.example.mallet.client.test;

import androidx.annotation.NonNull;

import com.example.mallet.client.error.ResponseHandler;
import com.example.mallet.client.user.UserServiceImpl;
import com.example.mallet.exception.MalletException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Test {

    private final ResponseHandler responseHandler;

    public Test() {
        this.responseHandler = new ResponseHandler();
    }

    public void initMethod() {
        UserServiceImpl userService = new UserServiceImpl();
        userService.signUp("lukaszek", "chuj", "nikodemb@student.agh.edu.pl", new Callback<>() {
            //todo w tym callbacku ogarniasz rzeczy ktore maja sie zrobic jak request przejdzie i jak sie wywali cos
            // onFailure to cos z polaczeniem zazwyczaj wiec mozna dac jakiegos Toast na przyklad
            // onResponse to jest ze response przyszeedl ale moze byc z bledami
            // w tym przypadku np ze juz taki uzytkowink istnieje
            // i tam zrobilem taki ResponseHandler i on rzuca wyjatek MalletException z message tym ktore przychodzi z API
            // no i w sumie taki message jaki przychodzi to chyba mozna dawać też do Toast
            // np jak uzytkownik istnieje juz to msg wyglada tak:
            // User with provided email: {0} already exists
            // a jak nie bedzie rzucony wyjatek to znaczy ze sie udalo wszystko i np mozna tam wywolac metode
            // ktora otworzy kolejne okienko np. z logowania do tego glownego widoku.
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                try {
                    responseHandler.handleResponse(response);
                } catch (MalletException e) {
                    System.out.println("There was an error. Please, try again");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                System.out.println("FAILURE");
            }
        });
    }

}