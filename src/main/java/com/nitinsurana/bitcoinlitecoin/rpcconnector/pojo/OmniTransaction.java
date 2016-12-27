package com.nitinsurana.bitcoinlitecoin.rpcconnector.pojo;

/**
 * Created by d.romantsov on 25.05.2016.
 */
public class OmniTransaction extends Transaction {
    private String sendingaddress;
    private String referenceaddress;
    private Long propertyid;

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

    public Long getPropertyid() {
        return propertyid;
    }

    public void setPropertyid(Long propertyid) {
        this.propertyid = propertyid;
    }
}
