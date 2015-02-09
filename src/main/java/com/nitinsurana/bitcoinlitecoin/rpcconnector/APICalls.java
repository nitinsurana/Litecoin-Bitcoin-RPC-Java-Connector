/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nitinsurana.bitcoinlitecoin.rpcconnector;

import java.util.logging.Logger;

/**
 *
 * @author hp
 */
public enum APICalls {

//    public static final Logger LOG = Logger.getLogger(APICalls.class.getName());
//
//    public static final String GET_NEW_ADDRESS = "";
//    public static final String DUMP_PRIVATE_KEY = "";
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
    VALIDATE_ADDRESS("validateaddress"),
    ENCRYPT_WALLET("encryptwallet");

    private String value;

    private APICalls(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
//        return super.toString(); //To change body of generated methods, choose Tools | Templates.
        return value;
    }

}
