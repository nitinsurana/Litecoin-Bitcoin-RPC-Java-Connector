/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nitinsurana.bitcoinlitecoin.rpcconnector.exception;

/**
 *
 * @author hp
 */
public class AuthenticationException extends CryptoCurrencyRpcException {

    public AuthenticationException() {
        super("Invalid RPC Credentials");
    }

}
