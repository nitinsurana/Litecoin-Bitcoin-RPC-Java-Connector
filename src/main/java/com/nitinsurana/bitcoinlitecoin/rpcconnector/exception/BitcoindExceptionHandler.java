package com.nitinsurana.bitcoinlitecoin.rpcconnector.exception;

import com.google.gson.JsonObject;

/**
 * Created by d.romantsov on 22.05.2015.
 */
public class BitcoindExceptionHandler {

    public void checkException(JsonObject response) throws BitcoindException {
        if (response.get("error") != null && response.get("error").isJsonObject() == true) {
            JsonObject errorJson = response.get("error").getAsJsonObject();
            String message = errorJson.get("message").getAsString();

            int code = errorJson.get("code").getAsInt();
            switch (code) {
                case -6:
                    throw new InsufficientFundsException(message);
                default:
                    throw new BitcoindException(message);
            }
        }
    }
}
