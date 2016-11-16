package com.nitinsurana.litecoinrpcconnector;

import com.nitinsurana.bitcoinlitecoin.rpcconnector.CryptoCurrencyRPC;
import org.junit.Test;

/**
 * Created by d.romantsov on 16.11.2016.
 */
public class ApiTest {
    CryptoCurrencyRPC rpc = new CryptoCurrencyRPC("rpcuser", "rpcpassword", "localhost", "18332");

    @Test
    public void testWalletPassphrase() throws Exception {
        //rpc.wallePassphrase("dsd", 32);
    }
}
