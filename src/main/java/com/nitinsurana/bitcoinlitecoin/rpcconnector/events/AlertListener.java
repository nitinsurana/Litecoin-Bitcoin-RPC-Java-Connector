package com.nitinsurana.bitcoinlitecoin.rpcconnector.events;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;


public class AlertListener extends Observable implements Observer {

	final private Observable alertListener;
	public Thread listener = null;

	public AlertListener(int port) throws IOException {
		alertListener = new BitcoinDListener(port);
	}

	@Override
	public synchronized void addObserver(Observer o) {
		if (null == listener) {
			alertListener.addObserver(this);
			listener = new Thread((Runnable) alertListener, "alertListener");
			listener.start();
		}
		super.addObserver(o);
	}

	@Override
	public void update(Observable o, Object arg) {
		final String value = ((String) arg).trim();
		setChanged();
		notifyObservers(value);
	}
	
	public void stop(){
		listener.interrupt();
	}

}
