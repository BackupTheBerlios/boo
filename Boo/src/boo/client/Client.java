package boo.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import boo.server.IGateway;
import boo.server.ILoginManager;
import boo.util.Utilities;

public class Client extends UnicastRemoteObject implements IClient {
	
	private IGateway gateway;
	private String userName;
	private String password;
	
	public Client(String userName, String password) throws RemoteException {
		super();
		this.userName = userName;
		this.password = password;
	}
	
	private void log(String message) {
		System.err.println(userName + " |  " + message);
	}
	
	/**
	 * Tenta di effettuare il login sul server specificato. Solleva un'eccezione
	 * se il login non va a buon fine.
	 * 
	 * @param host
	 * 			Indirizzo del server a cui connettersi.
	 * @throws RemoteException
	 */
	public void login(String host) throws RemoteException {
		
		ILoginManager lm;
		
		String url = "//" + host + ":" + ILoginManager.PORT
				+ "/" + ILoginManager.SERVICE_NAME;
		
		log("connecting to " + url);
		
		try {
			lm = (ILoginManager) Naming.lookup(url);
		} catch (NotBoundException _) {
			log("service not found");
			return;
		} catch (MalformedURLException _) {
			log("malformed url");
			return;
		}
		
		byte[] salt = null;
		salt = lm.getSalt(userName);
		
		log("salt: " + Utilities.reprByteArray(salt));
	
		byte[] passcode = Utilities.passwordToPasscode(password, salt);
		log("passcode: " + Utilities.reprByteArray(passcode));
		
		log("logging in...");
		
		boolean rv = false;
		
		rv = lm.login(userName, passcode, this);
		
		if (rv) {
			log("logged in.");
		} else {
			log("not logged in.");
		}
		
	}

	public void ping() throws RemoteException {
		log("pong");
	}

	public static void main(String[] args) throws Exception {
		Utilities.rmiSetup();
		
		int cntClients = 1;
		String serverAddress = "10.0.0.55";
		
		if (args.length > 0)
			serverAddress = args[0];
		
		if (args.length > 1)
			cntClients = Integer.parseInt(args[1]);
		
		for (int i = 0; i < cntClients; i++) {
			Client c = new Client("User" + (i < 10 ? "0" : "") + i, "prova");
			c.login(serverAddress);
		}
		
		System.err.println("hello world.");
		while (true) {
			// just wait...
		}
	}

}
