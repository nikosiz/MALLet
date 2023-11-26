package com.example.mallet.backend.client.configuration;

import com.agh.api.ErrorResponseDTO;
import com.example.mallet.backend.exception.MalletException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ResponseHandler {

    private static final String UNKNOWN_ERROR_OCCURRED_ERROR_MSG = "Unknown error occurred";

    private static final Gson gson;

    static {
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
    }

    public static <T> T handleResponse(Response<T> response) throws MalletException {
        if (response.isSuccessful()) {
            return response.body();
        }

        return handleError(response);
    }

    private static <T> T handleError(Response<T> response) throws MalletException {
        ResponseBody errorBody = response.errorBody();

        if (Objects.nonNull(errorBody)) {
            ErrorResponseDTO errorResponseDTO = parseException(errorBody);
            if (Objects.nonNull(errorResponseDTO.message())) {
                throw new MalletException(errorResponseDTO.message());
            }
        }
        throw new MalletException(UNKNOWN_ERROR_OCCURRED_ERROR_MSG);
    }

    private static ErrorResponseDTO parseException(ResponseBody errorBody) {
        try {
            return gson.fromJson(errorBody.string(), ErrorResponseDTO.class);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
