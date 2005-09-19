package boo.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
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
	
	private static void log(String message) {
		System.err.println(message);
	}
	
	/**
	 * Tenta di effettuare il login sul server specificato. Solleva un'eccezione
	 * se il login non va a buon fine.
	 * 
	 * @param host
	 * 			Indirizzo del server a cui connettersi.
	 * @throws RemoteException
	 */
	public void login(String host) throws NotBoundException, RemoteException {
		ILoginManager lm = null;
		
		// localizza
		try {
			lm = (ILoginManager) LocateRegistry.getRegistry(host,
					ILoginManager.PORT).lookup(ILoginManager.SERVICE_NAME);
		} catch (NotBoundException e) {
			log("LoginManager service not found at the address " +
					host + ":" + ILoginManager.PORT);
			throw e;
		}
		
		byte[] salt = lm.getSalt(userName);
		byte[] passcode = Utilities.passwordToPasscode(password, salt);
		
		log("salt: " + Utilities.reprByteArray(salt));
		log("passcode: " + Utilities.reprByteArray(passcode));
		
		gateway = lm.login(userName, passcode, this);
		
		if (gateway != null) {
			log(userName + " logged in.");
		} else {
			log(userName + " not logged in.");
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
