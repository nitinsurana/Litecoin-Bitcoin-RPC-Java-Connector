package com.nitinsurana.bitcoinlitecoin.rpcconnector.pojo;

/**
 * Created by d.romantsov on 25.05.2016.
 */
public class OmniTransaction extends Transaction {
    private String sendingaddress;
    private String referenceaddress;

    public String getReferenceaddress() {
        return referenceaddress;
    }

    public void setReferenceaddress(String referenceaddress) {
        this.referenceaddress = referenceaddress;
    }

    public String getSendingaddress() {
        return sendingaddress;
    }

    public void setSendingaddress(String sendingaddress) {
        this.sendingaddress = sendingaddress;
    }
}
