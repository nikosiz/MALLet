package com.example.mallet.client.error;

import com.agh.api.ErrorResponseDTO;
import com.example.mallet.exception.MalletException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ResponseHandler {

    private static final String UNKNOWN_ERROR_OCCURED_ERROR_MSG = "Unknown error occured";

    private final Gson gson;

    public ResponseHandler() {
        this.gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

    public <T> T handleResponse(Response<T> response) throws MalletException {
        if (response.isSuccessful()) {
            return response.body();
        }

        ResponseBody errorBody = response.errorBody();
        if (errorBody != null) {
            ErrorResponseDTO errorResponseDTO = parseException(errorBody);
            throw new MalletException(errorResponseDTO.getMessage());
        }

        throw new MalletException(UNKNOWN_ERROR_OCCURED_ERROR_MSG);
    }

    private ErrorResponseDTO parseException(ResponseBody responseBody) {
        try {
            return gson.fromJson(responseBody.string(), ErrorResponseDTO.class);
        } catch (IOException e) {
            //todo
            throw new RuntimeException(e);
        }
    }

}
