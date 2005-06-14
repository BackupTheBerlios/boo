package boo.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import boo.server.IGateway;
import boo.server.ILoginManager;

public class Client extends UnicastRemoteObject implements IClient {

	private IGateway gateway;

	public Client() throws RemoteException {
		super();
	}

	private void log(String message) {
		System.err.println(message);
	}

	public void login(String host) throws RemoteException {
		ILoginManager lm = null;
		try {
			lm = (ILoginManager) LocateRegistry.getRegistry(host,
					ILoginManager.PORT).lookup("Boo");
		} catch (NotBoundException e) {
			log("LoginManager not found at " + host + ":5000");
		}
		gateway = lm.login("Domenico", "prova", this);
		if (gateway != null) {
			log("Logged in.");
		} else {
			log("Not logged in. Something didn't work.");
		}
	}

}
