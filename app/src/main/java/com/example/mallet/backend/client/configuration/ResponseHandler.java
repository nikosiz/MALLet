package com.example.mallet.backend.client.configuration;

/*import com.agh.api.ErrorResponseDTO;
import com.example.mallet.backend.exception.MalletException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ResponseHandler {

    private static final String UNKNOWN_ERROR_OCCURRED_ERROR_MSG = "Unknown error occured";

    private final Gson gson;

    public ResponseHandler() {
        this.gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
    }

    public <T> T handleResponse(Response<T> response) throws MalletException {
        if (response.isSuccessful()) {
            return response.body();
        }

        ResponseBody errorBody = response.errorBody();
        if (Objects.nonNull(errorBody)) {
            ErrorResponseDTO errorResponseDTO = parseException(errorBody);
            throw new MalletException(errorResponseDTO.message());
        }

        throw new MalletException(UNKNOWN_ERROR_OCCURRED_ERROR_MSG);
    }

    private ErrorResponseDTO parseException(ResponseBody errorBody) {
        try {
            return gson.fromJson(errorBody.string(), ErrorResponseDTO.class);
        } catch (IOException e) {
            //todo
            throw new RuntimeException(e);
        }
    }

}*/
