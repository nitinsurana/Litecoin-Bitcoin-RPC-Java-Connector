package com.nitinsurana.bitcoinlitecoin.rpcconnector;

import com.gargoylesoftware.htmlunit.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.AuthenticationException;
import com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.BitcoindException;
import com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.BitcoindExceptionHandler;
import com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CallApiBitcoindException;
import com.nitinsurana.bitcoinlitecoin.rpcconnector.pojo.Transaction;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class BitcoindRPC {

    public static final Logger LOG = Logger.getLogger(BitcoindRPC.class);

    private WebClient client;
    private String baseUrl;
    private BitcoindExceptionHandler bitcoindExceptionHandler = new BitcoindExceptionHandler();
    private Gson gson = new Gson();

    public BitcoindRPC(String rpcUser, String rpcPassword, String rpcHost, String rpcPort) throws AuthenticationException {
        client = new WebClient(BrowserVersion.CHROME);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getOptions().setPrintContentOnFailingStatusCode(false);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);

        baseUrl = new String("http://" + rpcUser + ":" + rpcPassword + "@" + rpcHost + ":" + rpcPort + "/");
        //LOG.info("Base RPC URL : " + baseUrl);

        try {
            if (client.getPage(baseUrl).getWebResponse().getStatusCode() == 401) {  //401 is Http Unauthorized
                throw new AuthenticationException();
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * Safely copies wallet.dat to destination, which can be a directory or a
     * path with filename.
     *
     * @param destination
     * @return
     * @throws Exception
     */
    public boolean backupWallet(String destination) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.BACKUP_WALLET, destination);
        if (jsonObj.get("error") == null) {
            return true;
        }
        return false;
    }

    /**
     * Produces a human-readable JSON object for a raw transaction.
     *
     * @param hex
     * @return
     * @throws BitcoindException
     */
    public JsonObject decodeRawTransaction(String hex) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.DECODE_RAW_TRANSACTION, hex);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsJsonObject();
    }

    /**
     * Reveals the private key corresponding to <address>
     *
     * @param address
     * @return
     * @throws BitcoindException
     */
    public String dumpPrivateKey(String address) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.DUMP_PRIVATE_KEY, address);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns raw transaction representation for given transaction id.
     *
     * @param txid
     * @return returns the hex string for the given transaction id
     * @throws BitcoindException
     */
    public String getRawTransaction(String txid) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_RAW_TRANSACTION, txid);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns the account associated with the given address.
     *
     * @param address
     * @return
     * @throws BitcoindException
     */
    public String getAccount(String address) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_ACCOUNT, address);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns the current Litecoin address for receiving payments to this
     * account.
     *
     * @param account
     * @return
     * @throws BitcoindException
     */
    public String getAccountAddress(String account) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_ACCOUNT_ADDRESS, account);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns the list of addresses for the given account.
     *
     * @param account
     * @return
     * @throws BitcoindException
     */
    public JsonArray getAddressesByAccount(String account) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_ADDRESSES_BY_ACCOUNT, account);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsJsonArray();
    }

    /**
     * Returns the balance in the account.
     *
     * @param account
     * @return
     * @throws BitcoindException
     */
    public BigDecimal getBalance(String account) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_BALANCE, account);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsBigDecimal();
    }

    /**
     * Returns the wallet's total available balance.
     *
     * @return
     * @throws BitcoindException
     */
    public BigDecimal getBalance() throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_BALANCE);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsBigDecimal();
    }

    /**
     * Returns the total amount received by addresses with [account] in
     * transactions
     *
     * @param account
     * @return
     * @throws BitcoindException
     */
    public BigDecimal getReceivedByAccount(String account) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_RECEIVED_BY_ACCOUNT, account);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsBigDecimal();
    }

    /**
     * Returns a new address for receiving payments.
     *
     * @return
     * @throws BitcoindException
     */
    public String getNewAddress() throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_NEW_ADDRESS);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns a new address for receiving payments.
     *
     * @param account
     * @return
     * @throws BitcoindException
     */
    public String getNewAddress(String account) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_NEW_ADDRESS, account);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns the total amount received by <address> in transactions
     *
     * @param address
     * @return
     * @throws BitcoindException
     */
    public BigDecimal getReceivedByAddress(String address) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_RECEIVED_BY_ADDRESS, address);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsBigDecimal();
    }

    /**
     * Returns an object about the given transaction containing: amount,
     * confirmations, txid, time[1], details (an array of objects containing:
     * account, address, category, amount, fee)
     *
     * @param txid
     * @return
     * @throws BitcoindException
     */
    public Transaction getTransaction(String txid) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_TRANSACTION, txid);
        bitcoindExceptionHandler.checkException(jsonObj);
        return gson.fromJson(jsonObj.get("result").getAsJsonObject(), Transaction.class);
    }

    /**
     * Returns Object that has account names as keys, account balances as
     * values.
     *
     * @return
     * @throws BitcoindException
     */
    public JsonObject listAccounts() throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_ACCOUNTS);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsJsonObject();
    }

    /**
     * Returns an array of objects containing: account, amount, confirmations
     *
     * @return
     */
    public JsonArray listReceivedByAccount() throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_RECEIVED_BY_ACCOUNT);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsJsonArray();
    }

    /**
     * Returns an array of objects containing: address, account, amount,
     * confirmations
     *
     * @return
     * @throws BitcoindException
     */
    public JsonArray listReceivedByAddress() throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_RECEIVED_BY_ADDRESS);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsJsonArray();
    }

    /**
     * <amount> is a real and is rounded to 8 decimal places. Will send the
     * given amount to the given address, ensuring the account has a valid
     * balance using [minconf] confirmations. Returns the transaction ID if
     * successful
     *
     * @param fromAccount
     * @param toAddress
     * @param amount
     * @return
     * @throws BitcoindException
     */
    public String sendFrom(String fromAccount, String toAddress, BigDecimal amount) throws BitcoindException {
        JsonObject response = callAPIMethod(APICalls.SEND_FROM, fromAccount, toAddress, amount);
        bitcoindExceptionHandler.checkException(response);
        return response.get("result").getAsString();
    }

    /**
     * < amount > is a real and is rounded to the nearest 0.00000001
     *
     * @param toAddress
     * @param amount
     * @return TransactionID
     * @throws BitcoindException
     */
    public String sendToAddress(String toAddress, BigDecimal amount) throws BitcoindException{
        JsonObject jsonObj = callAPIMethod(APICalls.SEND_TO_ADDRESS, toAddress, amount);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsString();
    }

    public boolean validateAddress(String address) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.VALIDATE_ADDRESS, address);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsJsonObject().get("isvalid").getAsBoolean();
    }

    /**
     * Sets the account associated with the given address. Assigning address
     * that is already assigned to the same account will create a new address
     * associated with that account.
     *
     * @param address
     * @param account
     *
     * @throws BitcoindException
     */
    public void setAccount(String address, String account) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.SET_ACCOUNT, address, account);
        bitcoindExceptionHandler.checkException(jsonObj);
    }

    /**
     * Returns up to [count] most recent transactions skipping the first [from]
     * transactions for account [account].
     *
     * @param account
     * @param count
     * @param from
     * @return
     * @throws BitcoindException
     */
    public List<Transaction> listTransactions(String account, int count, int from) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_TRANSACTIONS, account, count, from);
        bitcoindExceptionHandler.checkException(jsonObj);

        return Arrays.asList(gson.fromJson(jsonObj.get("result").getAsJsonArray(), Transaction[].class));
    }

    /**
     * Returns all unspent outputs with at least [minconf] and at most [maxconf]
     * confirmations.
     *
     * @param minconf
     * @param maxconf
     * @return
     * @throws BitcoindException
     */
    public JsonArray listUnspent(int minconf, int maxconf) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_UNSPENT, minconf, maxconf);
        bitcoindExceptionHandler.checkException(jsonObj);

        return jsonObj.get("result").getAsJsonArray();
    }

    /**
     * Returns all unspent outputs with at least [minconf] and at most 9999999
     * confirmations; Further limited to outputs that pay at least one of the
     * given addresses in the [address] array.
     *
     * @param minconf
     * @param address
     * @return
     * @throws BitcoindException
     */
    public JsonArray listUnspent(int minconf, String[] address) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_UNSPENT, minconf, address);

                bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsJsonArray();
    }


    /**
     * Returns all unspent outputs with at least [minconf] and at most 9999999
     * confirmations.
     * 
     * @param minconf
     * @return
     * @throws BitcoindException
     */
    public JsonArray listUnspent(int minconf) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_UNSPENT, minconf);
        bitcoindExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsJsonArray();
    }

    /**
     * Returns all unspent outputs with at least [minconf] and at most [maxconf]
     * confirmations; Further limited to outputs that pay at least one of the
     * given addresses in the [address] array.
     *
     * @param minconf
     * @param maxconf
     * @param address
     * @return
     * @throws BitcoindException
     */
    public JsonArray listUnspent(int minconf, int maxconf, String[] address) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_UNSPENT, minconf, maxconf, address);
        bitcoindExceptionHandler.checkException(jsonObj);

        return jsonObj.get("result").getAsJsonArray();
    }

    /**
     * Returns an unsigned transaction that spends the outputs [prevOut] to new
     * outputs [Out] and encodes it as hex format.
     *
     * @param prevOut is an array of JsonObjects, each with the properties
     * "txid" and "vout".
     * @param out is an JsonObject with the receiving addresses as properties
     * and the receiving amount as value of each property(=address)
     * @return
     * @throws BitcoindException
     */
    public Transaction createRawTransaction(JsonObject[] prevOut, JsonObject out) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.CREATE_RAW_TRANSACTION, prevOut, out);
        bitcoindExceptionHandler.checkException(jsonObj);

        return gson.fromJson(jsonObj.get("result").getAsJsonObject(), Transaction.class);
    }

    /**
     * Returns a signed transaction in hex format using private keys stored in
     * the wallet and the output from createRawTransaction()
     *
     * @param hexString
     * @return
     * @throws BitcoindException
     */
    public Transaction signRawTransaction(String hexString) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.SIGN_RAW_TRANSACTION,hexString);
        bitcoindExceptionHandler.checkException(jsonObj);

        return gson.fromJson(jsonObj.get("result").getAsJsonObject(), Transaction.class);
    }

    /**
     * Validates a signed transaction in hex format and broadcasts it to the
     * network.
     *
     * @param hexString
     * @return
     * @throws BitcoindException
     */
    public String sendRawTransaction(String hexString) throws BitcoindException {
        JsonObject jsonObj = callAPIMethod(APICalls.SEND_RAW_TRANSACTION,hexString);
        bitcoindExceptionHandler.checkException(jsonObj);

        return jsonObj.get("result").getAsString();
    }

    public static void main(String[] args) throws Exception {
        final String rpcUser = "Nitin";
        final String rpcPassword = "magicmaker07";
        final String rpcHost = "localhost";
        final String rpcPort = "9332";
        BitcoindRPC bitcoindRPC = new BitcoindRPC(rpcUser, rpcPassword, rpcHost, rpcPort);

        bitcoindRPC.listTransactions("nn", 11, 0);
        bitcoindRPC.listTransactions("", 11, 0);
        bitcoindRPC.listTransactions("nnn", 11, 0);
    }

    private JsonObject callAPIMethod(APICalls callMethod, Object... params) throws CallApiBitcoindException {
        try {
            JsonObject jsonObj = null;
            WebRequest req = new WebRequest(new URL(baseUrl));
            req.setAdditionalHeader("Content-type", "application/json");
            req.setHttpMethod(HttpMethod.POST);
            JSONRequestBody body = new JSONRequestBody();
            body.setMethod(callMethod.toString());
            if (params != null && params.length > 0) {
                body.setParams(params);
            }
            req.setRequestBody(new Gson().toJson(body, JSONRequestBody.class));
            WebResponse resp = client.getPage(req).getWebResponse();
            jsonObj = new JsonParser().parse(resp.getContentAsString()).getAsJsonObject();
            LOG.info("RPC Response : " + jsonObj);
            return jsonObj;
        } catch (Exception e) {
            throw new CallApiBitcoindException(e.getMessage());
        }
    }
}
