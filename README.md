Introduction
============

This project is a java library that is intended to provide a wrapper around JSON-RPC provided by Bitcoin/Litecoin.

It has been tested on Litecoin. I wrote it for one of my projects.

The main dependencies of this project are 

    *   **Htmlunit**	
    *   **Google Gson**

Since this was intended for use in a proprietary project, I've tried to write as much javadoc as possible as to what the method does.

JSON-RPC calls for Litecoin & Bitcoin are same as you can see below, so this work shall work with both. If it doesn't please let me know by filing an issue.

[Litecoin RPC API Calls](https://litecoin.info/Litecoin_API)

[Bitcoin RPC API Calls](https://en.bitcoin.it/wiki/API_reference_%28JSON-RPC%29)

This project assumes that the one who uses it has a preliminary knowledge about Bitcoin/Litecoin and how to start the RPC server.

[Running Litecoin](https://litecoin.info/Litecoin.conf)

[Running Bitcoin](https://en.bitcoin.it/wiki/Running_Bitcoin)



Sample Usage
===========

    public static void main(String[] args) throws Exception {
        final String rpcUser = "rpcuser";
        final String rpcPassword = "password";
        final String rpcHost = "localhost";
        final String rpcPort = "9332";
        CryptoCurrencyRPC cryptoCurrencyRPC = new CryptoCurrencyRPC(rpcUser, rpcPassword, rpcHost, rpcPort);

        cryptoCurrencyRPC.setAccount("account","LCYEnDaddressENBYCEYD235NSD");
        
        //Similarly, all the API methods can be called.
    }

### Blockchain Events 
(part of the code was taken from https://github.com/johannbarbie/BitcoindClient4J)

The library also captures notifications from Bitcoind using the startup configuration. Launch your deamon with those parameters:
```bash
  ./bitcoind  -walletnotify="echo '%s' | nc 127.0.0.1 4002" 
              -alertnotify="echo '%s' | nc 127.0.0.1 4003" 
              -daemon
```

You can register observers to capture events of wallets, and alerts:
```java
    WalletListener walletListener = new WalletListener(cryptoCurrencyRPC, 4002);
    walletListener.addObserver(new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            System.out.println("Amount of transaction: " + ((Transaction)arg).getAmount());
        }
    });

```

Make sure to close sockets later:
```java
  walletListener.stop();
```

> Written with [StackEdit](https://stackedit.io/).
