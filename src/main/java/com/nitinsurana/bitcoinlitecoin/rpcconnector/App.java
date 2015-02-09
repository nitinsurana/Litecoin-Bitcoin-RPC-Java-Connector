package com.nitinsurana.bitcoinlitecoin.rpcconnector;

//import com.nitinsurana.litecoinrpcconnector.responses.JSONResponse;
//import com.nitinsurana.litecoinrpcconnector.responses.ArrayResponse;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;

public class App {

    public static final Logger LOG = Logger.getLogger(App.class);

//    static final String rpcUser = "Nitin";
//    static final String rpcPassword = "magicmaker07";
//    static final String rpcHost = "localhost";
//    static final String rpcPort = "9332";
    WebClient client;
    String baseUrl;
    private App myself;

    public App(String rpcUser, String rpcPassword, String rpcHost, String rpcPort) throws AuthenticationException {
        client = new WebClient(BrowserVersion.FIREFOX_17);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getOptions().setPrintContentOnFailingStatusCode(false);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);

        baseUrl = new String("http://" + rpcUser + ":" + rpcPassword + "@" + rpcHost + ":" + rpcPort + "/");
        LOG.info("Base RPC URL : " + baseUrl);

        try {
            if (client.getPage(baseUrl).getWebResponse().getStatusCode() == 401) {  //401 is Http Unauthorized
                throw new AuthenticationException();
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        myself = this;
    }

    /**
     * Safely copies wallet.dat to destination, which can be a directory or a
     * path with filename.
     *
     * @param destination
     * @return
     * @throws Exception
     */
    public boolean backupWallet(String destination) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.BACKUP_WALLET, destination);

//        ArrayResponse response = new Gson().fromJson(responseString, ArrayResponse.class);
//        LOG.info("Backup Wallet : " + ToStringBuilder.reflectionToString(response, ToStringStyle.DEFAULT_STYLE));
//        if (response.getError() == null) {
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
     * @throws Exception
     */
    public JsonObject decodeRawTransaction(String hex) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.DECODE_RAW_TRANSACTION, hex);

//        ArrayResponse response = new Gson().fromJson(responseString, ArrayResponse.class);
//        LOG.info("Decode Raw Transaction : " + ToStringBuilder.reflectionToString(response, ToStringStyle.DEFAULT_STYLE));
        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }

        return jsonObj.get("result").getAsJsonObject();
    }

    /**
     * Reveals the private key corresponding to <address>
     *
     * @param address
     * @return
     * @throws Exception
     */
    public String dumpPrivateKey(String address) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.DUMP_PRIVATE_KEY, address);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns raw transaction representation for given transaction id.
     *
     * @param txid
     * @return returns the hex string for the given transaction id
     * @throws Exception
     */
    public String getRawTransaction(String txid) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_RAW_TRANSACTION, txid);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsString();
    }

    /**
     * Encrypts the wallet with <passphrase>.
     *
     * @param passphrase
     * @return
     * @throws Exception
     */
//    public String encryptWallet(String passphrase) throws Exception {
//        JsonObject jsonObj = callAPIMethod(APICalls.ENCRYPT_WALLET, passphrase);
//
//        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
//            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
//            throw new RpcInvalidResponseException(message);
//        }
//        return jsonObj.get("result").getAsString();
//    }
    /**
     * Returns the account associated with the given address.
     *
     * @param address
     * @return
     * @throws Exception
     */
    public String getAccount(String address) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_ACCOUNT, address);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns the current Litecoin address for receiving payments to this
     * account.
     *
     * @param account
     * @return
     * @throws Exception
     */
    public String getAccountAddress(String account) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_ACCOUNT_ADDRESS, account);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns the list of addresses for the given account.
     *
     * @param account
     * @return
     * @throws Exception
     */
    public JsonArray getAddressesByAccount(String account) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_ADDRESSES_BY_ACCOUNT, account);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsJsonArray();
    }

    /**
     * Returns the balance in the account.
     *
     * @param account
     * @return
     * @throws Exception
     */
    public double getBalance(String account) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_BALANCE, account);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsDouble();
    }

    /**
     * Returns the wallet's total available balance.
     *
     * @return
     * @throws Exception
     */
    public double getBalance() throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_BALANCE);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsDouble();
    }

    /**
     * return will include all transactions to all accounts
     *
     * @return
     * @throws Exception
     */
//    public double getReceivedByAccount() throws Exception {
//        JsonObject jsonObj = callAPIMethod(APICalls.GET_RECEIVED_BY_ACCOUNT);
//
//        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
//            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
//            throw new RpcInvalidResponseException(message);
//        }
//        return jsonObj.get("result").getAsDouble();
//    }
    /**
     * Returns the total amount received by addresses with [account] in
     * transactions
     *
     * @param account
     * @return
     * @throws Exception
     */
    public double getReceivedByAccount(String account) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_RECEIVED_BY_ACCOUNT, account);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsDouble();
    }

    /**
     * Returns a new address for receiving payments.
     *
     * @return
     * @throws Exception
     */
    public String getNewAddress() throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_NEW_ADDRESS);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns a new address for receiving payments.
     *
     * @param account
     * @return
     * @throws Exception
     */
    public String getNewAddress(String account) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_NEW_ADDRESS, account);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns the total amount received by <address> in transactions
     *
     * @param address
     * @return
     * @throws Exception
     */
    public double getReceivedByAddress(String address) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_RECEIVED_BY_ADDRESS, address);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsDouble();
    }

    /**
     * Returns an object about the given transaction containing: amount,
     * confirmations, txid, time[1], details (an array of objects containing:
     * account, address, category, amount, fee)
     *
     * @param txid
     * @return
     * @throws Exception
     */
    public JsonObject getTransaction(String txid) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_TRANSACTION, txid);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsJsonObject();
    }

    /**
     * Returns Object that has account names as keys, account balances as
     * values.
     *
     * @return
     * @throws Exception
     */
    public JsonObject listAccounts() throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_ACCOUNTS);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsJsonObject();
    }

    /**
     * Returns an array of objects containing: account, amount, confirmations
     *
     * @return
     */
    public JsonArray listReceivedByAccount() throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_RECEIVED_BY_ACCOUNT);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsJsonArray();
    }

    /**
     * Returns an array of objects containing: address, account, amount,
     * confirmations
     *
     * @return
     * @throws Exception
     */
    public JsonArray listReceivedByAddress() throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_RECEIVED_BY_ADDRESS);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
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
     * @throws Exception
     */
    public String sendFrom(String fromAccount, String toAddress, double amount) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.SEND_FROM, fromAccount, toAddress, amount);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsString();
    }

    /**
     * < amount > is a real and is rounded to the nearest 0.00000001
     *
     * @param toAddress
     * @param amount
     * @return TransactionID
     * @throws Exception
     */
    public String sendToAddress(String toAddress, double amount) throws Exception{
        JsonObject jsonObj = callAPIMethod(APICalls.SEND_TO_ADDRESS, toAddress, amount);

        if(jsonObj.get("error")!=null&&jsonObj.get("error").isJsonObject()){
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }

        return jsonObj.get("result").getAsString();
    }

    public boolean validateAddress(String address) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.VALIDATE_ADDRESS, address);

        if(jsonObj.get("error")!=null&&jsonObj.get("error").isJsonObject()){
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }

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
     * @throws Exception
     */
    public void setAccount(String address, String account) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.SET_ACCOUNT, address, account);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
//        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns up to [count] most recent transactions skipping the first [from]
     * transactions for account [account].
     *
     * @param account
     * @param count
     * @param from
     * @return
     * @throws Exception
     */
    public JsonArray listTransactions(String account, int count, int from) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_TRANSACTIONS, account, count, from);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsJsonArray();
    }

    /**
     * Returns all unspent outputs with at least [minconf] and at most [maxconf]
     * confirmations.
     *
     * @param minconf
     * @param maxconf
     * @return
     * @throws Exception
     */
    public JsonArray listUnspent(int minconf, int maxconf) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_UNSPENT, minconf, maxconf);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
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
     * @throws Exception
     */
    public JsonArray listUnspent(int minconf, String[] address) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_UNSPENT, minconf, address);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsJsonArray();
    }


    /**
     * Returns all unspent outputs with at least [minconf] and at most 9999999
     * confirmations.
     * 
     * @param minconf
     * @return
     * @throws Exception
     */
    public JsonArray listUnspent(int minconf) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_UNSPENT, minconf);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
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
     * @throws Exception
     */
    public JsonArray listUnspent(int minconf, int maxconf, String[] address) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_UNSPENT, minconf, maxconf, address);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
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
     * @throws Exception
     */
    public String createRawTransaction(JsonObject[] prevOut, JsonObject out) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.CREATE_RAW_TRANSACTION, prevOut, out);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns a signed transaction in hex format using private keys stored in
     * the wallet and the output from createRawTransaction()
     *
     * @param hexString
     * @return
     * @throws Exception
     */
    public JsonObject signRawTransaction(String hexString) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.SIGN_RAW_TRANSACTION,hexString);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsJsonObject();
    }

    /**
     * Validates a signed transaction in hex format and broadcasts it to the
     * network.
     *
     * @param hexString
     * @return
     * @throws Exception
     */
    public String sendRawTransaction(String hexString) throws Exception {
        JsonObject jsonObj = callAPIMethod(APICalls.SEND_RAW_TRANSACTION,hexString);

        if (jsonObj.get("error") != null && jsonObj.get("error").isJsonObject() == true) {
            String message = jsonObj.get("error").getAsJsonObject().get("message").getAsString();
            throw new RpcInvalidResponseException(message);
        }
        return jsonObj.get("result").getAsString();
    }

    public static void main(String[] args) throws Exception {
        final String rpcUser = "Nitin";
        final String rpcPassword = "magicmaker07";
        final String rpcHost = "localhost";
        final String rpcPort = "9332";
        App app = new App(rpcUser, rpcPassword, rpcHost, rpcPort);

        app.listTransactions("nn", 11, 0);
        app.listTransactions("", 11, 0);
        app.listTransactions("nnn", 11, 0);
    }

    private JsonObject callAPIMethod(APICalls callMethod, Object... params) throws Exception {
        JsonObject jsonObj = null;
//        JSONResponse jsonResponse = null;
        WebRequest req = new WebRequest(new URL(baseUrl));
        req.setAdditionalHeader("Content-type", "application/json");
        req.setHttpMethod(HttpMethod.POST);
        JSONRequestBody body = new JSONRequestBody();
//        body.setMethod("getnewaddress");
        body.setMethod(callMethod.toString());
        if (params != null && params.length > 0) {
            body.setParams(params);
        }
        req.setRequestBody(new Gson().toJson(body, JSONRequestBody.class));
        WebResponse resp = client.getPage(req).getWebResponse();
        jsonObj = new JsonParser().parse(resp.getContentAsString()).getAsJsonObject();
//            jsonResponse = new Gson().fromJson(responseString, JSONResponse.class);
        LOG.info("RPC Response : " + jsonObj);
//        return jsonResponse.getResult();
//        return jsonResponse;
        return jsonObj;
    }

    public static void mainx(String[] args) throws Exception {
        final String rpcUser = "Nitin";
        final String rpcPassword = "magicmaker07";
        final String rpcHost = "localhost";
        final String rpcPort = "9332";
        App app = new App(rpcUser, rpcPassword, rpcHost, rpcPort);

//        String responseString = app.callAPIMethod(APICalls.GET_ADDRESSES_BY_ACCOUNT, "Nitin-Account");
//        ArrayResponse response = new Gson().fromJson(responseString, ArrayResponse.class);
//        Type listType = new TypeToken<List<String>>() {
//        }.getType();
//        List<String> addresses = (List<String>) new Gson().fromJson(responseString, listType);
//        LOG.info("Addresses By Account : " + response.getResult());
    }

    public static void mainq(String[] args) throws Exception {
        final String rpcUser = "Nitin";
        final String rpcPassword = "magicmaker07";
        final String rpcHost = "localhost";
        final String rpcPort = "9332";
        App app = new App(rpcUser, rpcPassword, rpcHost, rpcPort);

        String txnId = "fa833b496a2a9467b3ca148c1bf223e6bc02d389e151555e9600e4aee360727f";
//        String responseString = app.callAPIMethod(APICalls.GET_TRANSACTION, txnId);
//        LOG.info("Get Transaction : " + responseString);
    }

    public static void mainw(String[] args) throws Exception {
        final String rpcUser = "Nitin";
        final String rpcPassword = "magicmaker07";
        final String rpcHost = "localhost";
        final String rpcPort = "9332";
        App app = new App(rpcUser, rpcPassword, rpcHost, rpcPort);

        String account = "Nitin-Account";
        String toAddress = "LMNtL3ta9Ff69tecAuZ1LrW63R7fJ2TBD1";
        Double amount = 0.00005;
//        String responseString = app.callAPIMethod(APICalls.SEND_FROM, account, toAddress, amount);
//        LOG.info("Send From : " + responseString);

        Thread.sleep(5000);     //Sleeping for 5 seconds, to verify the balance transfer

//        responseString = app.callAPIMethod(APICalls.GET_BALANCE, account, 1);       //Confirmed Transfer
//        LOG.info("Get Balance  1 : " + responseString);
//        responseString = app.callAPIMethod(APICalls.GET_BALANCE, account, 0);
//        LOG.info("Get Balance : " + responseString);
//
//        responseString = app.callAPIMethod(APICalls.GET_BALANCE, "nitin.cool4urchat@gmail.com", 1);
//        LOG.info("Get Balance 1 : " + responseString);
//
//        responseString = app.callAPIMethod(APICalls.GET_BALANCE, "nitin.cool4urchat@gmail.com", 0);     //Unconfirmed Transfer
//        LOG.info("Get Balance : " + responseString);
    }

    public static void mainy(String[] args) throws Exception {
        final String rpcUser = "Nitin";
        final String rpcPassword = "magicmaker07";
        final String rpcHost = "localhost";
        final String rpcPort = "9332";
        App app = new App(rpcUser, rpcPassword, rpcHost, rpcPort);

        String address = "LLPHV1q1frWjD63jrs7fVp8Wrjz2qcft9b";
//        String responseString = app.callAPIMethod(APICalls.SET_ACCOUNT, address, "nitin.cool4urchat@gmail.com");
//        JSONResponse response = new Gson().fromJson(responseString, JSONResponse.class);
//        LOG.info("Set Account : " + response.getResult());

        address = "LMNtL3ta9Ff69tecAuZ1LrW63R7fJ2TBD1";
//        responseString = app.callAPIMethod(APICalls.SET_ACCOUNT, address, "nitin.cool4urchat@gmail.com");
//        response = new Gson().fromJson(responseString, JSONResponse.class);
//        LOG.info("Set Account : " + response.getResult());
    }

    public static void mainz(String[] args) throws Exception {
        final String rpcUser = "Nitin";
        final String rpcPassword = "magicmaker07";
        final String rpcHost = "localhost";
        final String rpcPort = "9332";
        App app = new App(rpcUser, rpcPassword, rpcHost, rpcPort);

//        String responseString = app.callAPIMethod(APICalls.GET_NEW_ADDRESS);        //Can take an account
//        JSONResponse response = new Gson().fromJson(responseString, JSONResponse.class);
//        LOG.info("New Address : " + response.getResult());
//
//        responseString = app.callAPIMethod(APICalls.DUMP_PRIVATE_KEY, response.getResult());
//        response = new Gson().fromJson(responseString, JSONResponse.class);
//        LOG.info("Dump Private Key : " + response.getResult());
    }
}
