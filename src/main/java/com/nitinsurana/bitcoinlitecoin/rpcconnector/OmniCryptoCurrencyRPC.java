package com.nitinsurana.bitcoinlitecoin.rpcconnector;

import com.google.gson.JsonObject;
import com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException;
import com.nitinsurana.bitcoinlitecoin.rpcconnector.pojo.OmniTransaction;
import com.nitinsurana.bitcoinlitecoin.rpcconnector.pojo.Transaction;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Created by d.romantsov on 25.05.2016.
 */
public class OmniCryptoCurrencyRPC extends CryptoCurrencyRPC {
    private Long tokenId;

    public OmniCryptoCurrencyRPC(String rpcUser, String rpcPassword, String rpcHost, String rpcPort, long tokenId) {
        super(rpcUser, rpcPassword, rpcHost, rpcPort);
        this.tokenId = tokenId;
    }

    @Override
    public String sendFrom(String fromAccount, String toAddress, BigDecimal amount) throws CryptoCurrencyRpcException {
        JsonObject response = callAPIMethod(APICalls.OMNI_SEND, fromAccount, toAddress, tokenId, amount);
        cryptoCurrencyRpcExceptionHandler.checkException(response);
        return response.get("result").getAsString();
    }

    @Override
    public String getInfo() throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.OMNI_GETINFO);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.toString();
    }

    @Override
    public BigDecimal getBalance(String account) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.OMNI_GETBALANCE, account, tokenId);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsBigDecimal();
    }

    @Override
    public BigDecimal getBalance() throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.OMNI_GETALLBALANCESFORID, tokenId);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsBigDecimal();
    }

    @Override
    public OmniTransaction getTransaction(String txid) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.OMNI_GETTRANSACTION, txid);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return gson.fromJson(jsonObj.get("result").getAsJsonObject(), OmniTransaction.class);
    }

    @Override
    public List listTransactions(String account, int count, int from) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.OMNI_LISTTRANSACTIONS, account, count, from);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return Arrays.asList(gson.fromJson(jsonObj.get("result").getAsJsonArray(), OmniTransaction[].class));
    }

    @Override
    public String sendRawTransaction(String hexString) throws CryptoCurrencyRpcException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String sendToAddress(String toAddress, BigDecimal amount) throws CryptoCurrencyRpcException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Transaction signRawTransaction(String hexString) throws CryptoCurrencyRpcException {
        throw new UnsupportedOperationException();
    }
}
