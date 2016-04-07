package com.nitinsurana.bitcoinlitecoin.rpcconnector;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CallApiCryptoCurrencyRpcException;
import com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException;
import com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcExceptionHandler;
import com.nitinsurana.bitcoinlitecoin.rpcconnector.pojo.Transaction;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class CryptoCurrencyRPC {

    public static final Logger LOG = Logger.getLogger("rpcLogger");

    private CryptoCurrencyRpcExceptionHandler cryptoCurrencyRpcExceptionHandler = new CryptoCurrencyRpcExceptionHandler();
    private Gson gson = new Gson();
    private JsonParser jsonParser = new JsonParser();

    private static final String CHARACTER_ENCODING = "UTF-8";
    private String uri;
    private CloseableHttpClient httpClient;
    private HttpHost targetHost;
    private HttpClientContext context;
    private AtomicLong id = new AtomicLong(1L);

    public CryptoCurrencyRPC(final String rpcUser, final String rpcPassword, String rpcHost, String rpcPort) {
        this.uri = "/";

        httpClient = HttpClients.createDefault();
        targetHost = new HttpHost(rpcHost, Integer.parseInt(rpcPort), "http");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                new UsernamePasswordCredentials(rpcUser, rpcPassword));

        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);

    }

    /**
     * Safely copies wallet.dat to destination, which can be a directory or a
     * path with filename.
     *
     * @param destination
     * @return
     * @throws Exception
     */
    public boolean backupWallet(String destination) throws CryptoCurrencyRpcException {
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
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public JsonObject decodeRawTransaction(String hex) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.DECODE_RAW_TRANSACTION, hex);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsJsonObject();
    }

    /**
     * Reveals the private key corresponding to <address>
     *
     * @param address
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public String dumpPrivateKey(String address) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.DUMP_PRIVATE_KEY, address);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns raw transaction representation for given transaction id.
     *
     * @param txid
     * @return returns the hex string for the given transaction id
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public String getRawTransaction(String txid) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_RAW_TRANSACTION, txid);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns the account associated with the given address.
     *
     * @param address
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public String getAccount(String address) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_ACCOUNT, address);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns the current Litecoin address for receiving payments to this
     * account.
     *
     * @param account
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public String getAccountAddress(String account) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_ACCOUNT_ADDRESS, account);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns the list of addresses for the given account.
     *
     * @param account
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public JsonArray getAddressesByAccount(String account) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_ADDRESSES_BY_ACCOUNT, account);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsJsonArray();
    }

    /**
     * Returns the balance in the account.
     *
     * @param account
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public BigDecimal getBalance(String account) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_BALANCE, account);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsBigDecimal();
    }

    /**
     * Returns the wallet's total available balance.
     *
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public BigDecimal getBalance() throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_BALANCE);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsBigDecimal();
    }

    /**
     * Returns the total amount received by addresses with [account] in
     * transactions
     *
     * @param account
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public BigDecimal getReceivedByAccount(String account) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_RECEIVED_BY_ACCOUNT, account);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsBigDecimal();
    }

    /**
     * Returns a new address for receiving payments.
     *
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public String getNewAddress() throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_NEW_ADDRESS);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsString();
    }


    /**
     * Returns an object containing various state info.
     *
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public String getInfo() throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_INFO);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.toString();
    }

    /**
     * Returns a new address for receiving payments.
     *
     * @param account
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public String getNewAddress(String account) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_NEW_ADDRESS, account);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsString();
    }

    /**
     * Returns the total amount received by <address> in transactions
     *
     * @param address
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public BigDecimal getReceivedByAddress(String address) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_RECEIVED_BY_ADDRESS, address);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsBigDecimal();
    }

    /**
     * Returns an object about the given transaction containing: amount,
     * confirmations, txid, time[1], details (an array of objects containing:
     * account, address, category, amount, fee)
     *
     * @param txid
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public Transaction getTransaction(String txid) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.GET_TRANSACTION, txid);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return gson.fromJson(jsonObj.get("result").getAsJsonObject(), Transaction.class);
    }

    /**
     * Returns Object that has account names as keys, account balances as
     * values.
     *
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public JsonObject listAccounts() throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_ACCOUNTS);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsJsonObject();
    }

    /**
     * Returns an array of objects containing: account, amount, confirmations
     *
     * @return
     */
    public JsonArray listReceivedByAccount() throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_RECEIVED_BY_ACCOUNT);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsJsonArray();
    }

    /**
     * Returns an array of objects containing: address, account, amount,
     * confirmations
     *
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public JsonArray listReceivedByAddress() throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_RECEIVED_BY_ADDRESS);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
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
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public String sendFrom(String fromAccount, String toAddress, BigDecimal amount) throws CryptoCurrencyRpcException {
        JsonObject response = callAPIMethod(APICalls.SEND_FROM, fromAccount, toAddress, amount);
        cryptoCurrencyRpcExceptionHandler.checkException(response);
        return response.get("result").getAsString();
    }

    /**
     *	Move from one account in your wallet to another
     *
     * @param fromAccount
     * @param toAccount
     * @param amount
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public boolean move(String fromAccount, String toAccount, BigDecimal amount, String comment) throws CryptoCurrencyRpcException {
        JsonObject response = callAPIMethod(APICalls.MOVE, fromAccount, toAccount, amount, 1, comment);
        cryptoCurrencyRpcExceptionHandler.checkException(response);
        return response.get("result").getAsBoolean();
    }

    /**
     * < amount > is a real and is rounded to the nearest 0.00000001
     *
     * @param toAddress
     * @param amount
     * @return TransactionID
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public String sendToAddress(String toAddress, BigDecimal amount) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.SEND_TO_ADDRESS, toAddress, amount);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsString();
    }

    public boolean validateAddress(String address) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.VALIDATE_ADDRESS, address);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
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
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public void setAccount(String address, String account) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.SET_ACCOUNT, address, account);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
    }

    /**
     * Returns up to [count] most recent transactions skipping the first [from]
     * transactions for account [account].
     * Sorting does not work correctly. https://github.com/bitcoin/bitcoin/issues/2853
     *
     * @param account
     * @param count
     * @param from
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public List<Transaction> listTransactions(String account, int count, int from) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_TRANSACTIONS, account, count, from);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);

        return Arrays.asList(gson.fromJson(jsonObj.get("result").getAsJsonArray(), Transaction[].class));
    }

    /**
     * Returns all unspent outputs with at least [minconf] and at most [maxconf]
     * confirmations.
     *
     * @param minconf
     * @param maxconf
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public JsonArray listUnspent(int minconf, int maxconf) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_UNSPENT, minconf, maxconf);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);

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
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public JsonArray listUnspent(int minconf, String[] address) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_UNSPENT, minconf, address);

                cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
        return jsonObj.get("result").getAsJsonArray();
    }


    /**
     * Returns all unspent outputs with at least [minconf] and at most 9999999
     * confirmations.
     * 
     * @param minconf
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public JsonArray listUnspent(int minconf) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_UNSPENT, minconf);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);
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
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public JsonArray listUnspent(int minconf, int maxconf, String[] address) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.LIST_UNSPENT, minconf, maxconf, address);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);

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
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public Transaction createRawTransaction(JsonObject[] prevOut, JsonObject out) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.CREATE_RAW_TRANSACTION, prevOut, out);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);

        return gson.fromJson(jsonObj.get("result").getAsJsonObject(), Transaction.class);
    }

    /**
     * Returns a signed transaction in hex format using private keys stored in
     * the wallet and the output from createRawTransaction()
     *
     * @param hexString
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public Transaction signRawTransaction(String hexString) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.SIGN_RAW_TRANSACTION,hexString);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);

        return gson.fromJson(jsonObj.get("result").getAsJsonObject(), Transaction.class);
    }

    /**
     * Validates a signed transaction in hex format and broadcasts it to the
     * network.
     *
     * @param hexString
     * @return
     * @throws com.nitinsurana.bitcoinlitecoin.rpcconnector.exception.CryptoCurrencyRpcException
     */
    public String sendRawTransaction(String hexString) throws CryptoCurrencyRpcException {
        JsonObject jsonObj = callAPIMethod(APICalls.SEND_RAW_TRANSACTION,hexString);
        cryptoCurrencyRpcExceptionHandler.checkException(jsonObj);

        return jsonObj.get("result").getAsString();
    }


    //Implementation from https://github.com/SulacoSoft/BitcoindConnector4J repository
    private JsonObject callAPIMethod(APICalls callMethod, Object... params) throws CallApiCryptoCurrencyRpcException {
        try {
            String jsonRequest = String.format("{\"jsonrpc\": \"2.0\", \"method\": \"%s\", \"params\": [%s], \"id\": %s}",
                    callMethod.toString(), buildParamsString(params), id.getAndIncrement());

            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(new ByteArrayEntity(jsonRequest.getBytes(CHARACTER_ENCODING)));
            CloseableHttpResponse response = httpClient.execute(targetHost, httpPost, context);
            try {
                checkHttpErrors(response.getStatusLine().getStatusCode());

                String jsonResponse = IOUtils.toString(response.getEntity().getContent(), CHARACTER_ENCODING);
                logRequest(callMethod.toString(), jsonResponse, params);

                return jsonParser.parse(jsonResponse).getAsJsonObject();
            } finally {
                response.close();
            }
        } catch (Throwable e) {
            throw new CallApiCryptoCurrencyRpcException(e.getMessage());
        }

    }

    private String buildParamsString(Object[] args) {
        StringBuilder params = new StringBuilder();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (i > 0)
                    params.append(",");
                Object arg = args[i];
                if (arg instanceof String)
                    params.append(String.format("\"%s\"", arg));
                else
                    params.append(String.format("%s", arg));
            }
        }
        return params.toString();
    }

    private void checkHttpErrors(int statusCode) {
        if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            if (statusCode == HttpStatus.SC_UNAUTHORIZED)
                throw new CryptoCurrencyRpcException(String.format(
                        "Bitcoind JSON-RPC HTTP error (Probably an incorrect username or password). HTTP Status-Code %s",
                        statusCode));
            else
                throw new CryptoCurrencyRpcException(String.format("Bitcoind JSON-RPC HTTP error. HTTP Status-Code %s",
                        statusCode));
        }
    }

    private void logRequest(String callMethod, String jsonResponse, Object[] params) {
        if (!callMethod.equals(APICalls.LIST_TRANSACTIONS.toString())) {
            StringBuffer buffer = new StringBuffer("");
            for (Object item : params) {
                buffer.append(item).append(" | ");
            }
            LOG.info("Bitcoin RPC Request: Method: " + callMethod + " Params: " + buffer.toString() +
                    "\nBitcoin RPC Response : " + jsonResponse);
        }
    }
}
