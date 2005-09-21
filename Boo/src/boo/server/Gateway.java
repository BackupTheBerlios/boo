package boo.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import boo.client.IClient;

public class Gateway extends UnicastRemoteObject implements IGateway {

	private Map clients;

	public Gateway() throws RemoteException {
		super();
		clients = new HashMap();
		new Timer(true).schedule(new KeepAliveTask(), new Date(), 5000);
	}
	
	class KeepAliveTask extends TimerTask {
		public void run() {
			log("Connected clients: " + clients.size());
			Iterator i = clients.entrySet().iterator();
			while (i.hasNext()) {
				Entry e = (Entry) i.next();
				String userName = (String) e.getKey();
				IClient client = (IClient) e.getValue();
				try {
					client.ping();
					log(userName + ": still alive");
				} catch (RemoteException x) {
					log(userName + ": broken connection");
					i.remove();
				}
			}
		}
	}
	
	private static void log(String message) {
		System.err.println(message);
	}

	public boolean addClient(String userName, IClient client) {
		if (clients.containsKey(userName))
			return false;
		clients.put(userName, client);
		return true;
	}

	public boolean isConnected(String userName) {
		return clients.containsKey(userName);
	}

	public void removeClient(String userName) {
		clients.remove(userName);
	}

}
