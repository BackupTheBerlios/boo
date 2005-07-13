package boo.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import boo.server.IGateway;
import boo.server.ILoginManager;

public class Client extends UnicastRemoteObject implements IClient {

	private IGateway gateway;

	private String userName;

	private String password;

	public Client(String userName, String password) throws RemoteException {
		super();
		this.userName = userName;
		this.password = password;
	}

	private static void log(String message) {
		System.err.println(message);
	}

	public void login(String host) throws RemoteException {
		ILoginManager lm = null;
		try {
			lm = (ILoginManager) LocateRegistry.getRegistry(host,
					ILoginManager.PORT).lookup(ILoginManager.SERVICE_NAME);
		} catch (NotBoundException e) {
			log("LoginManager not found at " + host + ":" + ILoginManager.PORT);
		}
		gateway = lm.login(userName, password, this);
		if (gateway != null) {
			log("Logged in.");
		} else {
			log("Not logged in. Something didn't work.");
		}
	}

	public void provaCallback(String parametro) throws RemoteException {
		log(parametro);
	}

	public static void main(String[] args) throws Exception {
		int numClients = 4;
		String serverAddress = "127.0.0.1";
		
		if (args.length > 0)
			serverAddress = args[0];

		for (int i = 0; i < numClients; i++) {
			Client c = new Client("Utente" + (i < 10 ? "0" : "") + i, "prova");
			c.login(serverAddress);
		}
		while (true) {
			// just wait...
		}
	}

}
