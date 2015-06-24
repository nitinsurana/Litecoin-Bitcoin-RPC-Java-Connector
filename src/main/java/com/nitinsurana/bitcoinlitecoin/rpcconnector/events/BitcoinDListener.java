package com.nitinsurana.bitcoinlitecoin.rpcconnector.events;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

/**
 * Basic class for implementation CryptoCurrencyListener's. Contains work with ports and streams.
 */
public class BitcoinDListener extends Observable implements Runnable {
	public static final Logger LOG = Logger.getLogger(BitcoinDListener.class);
	private final ServerSocket server;
	private int port;

	public BitcoinDListener(int port) throws IOException {
		server = new ServerSocket(port);
		this.port = port;
	}
	
	public BitcoinDListener(ServerSocket server) throws IOException {
		this.server = server;
		this.port = server.getLocalPort();
	}

	@Override
	public void run() {
		LOG.info("Thread " + Thread.currentThread().getName()
				+ "started lintening on " + port);
		while (!Thread.currentThread().isInterrupted()) {
			Socket connection = null;
			try {
				connection = server.accept();
			} catch (IOException e1) {
				e1.printStackTrace();
				break;
			}
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			    String line;
			    if ((line = reader.readLine()) != null) {
				    setChanged();
					notifyObservers(line);
					connection.close();
			    }
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// sockets are closed when complete.
				try {
					if (connection != null)
						connection.close();
				} catch (IOException e) {
				}
			}
		}
		LOG.warn("Thread " + Thread.currentThread().getName() + " exited");
	}
	
	@Override
	protected void finalize() throws Throwable {
		server.close();
		LOG.info("Thread " + Thread.currentThread().getName() + " shutting down.");
		super.finalize();
	}
	
}
