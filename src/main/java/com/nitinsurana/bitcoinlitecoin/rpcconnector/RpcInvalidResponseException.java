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
public class RpcInvalidResponseException extends Exception {
    
    public static final Logger LOG = Logger.getLogger(RpcInvalidResponseException.class.getName());
    
    public RpcInvalidResponseException(String message) {
        super(message);
    }
}
