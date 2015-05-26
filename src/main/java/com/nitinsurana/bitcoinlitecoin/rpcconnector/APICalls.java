/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nitinsurana.bitcoinlitecoin.rpcconnector;

/**
 *
 * @author hp
 */
public enum APICalls {

    GET_NEW_ADDRESS("getnewaddress"),
    DUMP_PRIVATE_KEY("dumpprivkey"),
    GET_ACCOUNT("getaccount"),
    GET_ACCOUNT_ADDRESS("getaccountaddress"),
    LIST_ACCOUNTS("listaccounts"),
    LIST_ADDRESS_GROUPINGS("listaddressgroupings"),
    LIST_RECEIVED_BY_ACCOUNT("listreceivedbyaccount"),
    LIST_RECEIVED_BY_ADDRESS("listreceivedbyaddress"),
    SEND_FROM("sendfrom"),
    SEND_RAW_TRANSACTION("sendrawtransaction"),
    SET_ACCOUNT("setaccount"),
    SEND_TO_ADDRESS("sendtoaddress"),
    GET_ADDRESSES_BY_ACCOUNT("getaddressesbyaccount"),
    GET_RECEIVED_BY_ACCOUNT("getreceivedbyaccount"),
    GET_RECEIVED_BY_ADDRESS("getreceivedbyaddress"),
    GET_BALANCE("getbalance"),
    GET_TRANSACTION("gettransaction"),
    GET_CONNECTION_COUNT("getconnectioncount"),
    BACKUP_WALLET("backupwallet"),
    DECODE_RAW_TRANSACTION("decoderawtransaction"),
    GET_RAW_TRANSACTION("getrawtransaction"),
    LIST_TRANSACTIONS("listtransactions"),
    LIST_UNSPENT("listunspent"),
    CREATE_RAW_TRANSACTION("createrawtransaction"),
    SIGN_RAW_TRANSACTION("signrawtransaction"),
    VALIDATE_ADDRESS("validateaddress"),
    ENCRYPT_WALLET("encryptwallet");

    private String value;

    private APICalls(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        //Using in BitcoinRPC#callAPIMethod(..)
        return value;
    }

}
