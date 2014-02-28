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
public class JSONRequestBody {

    public static final Logger LOG = Logger.getLogger(JSONRequestBody.class.getName());

//  $request = array(
//						'method' => $method,
//						'params' => $params,
//						'id' => $currentId
//						);
    private String method, id;
    private Object[] params = new Object[]{};

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

}
