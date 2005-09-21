package boo.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import boo.client.IClient;
import boo.util.Utilities;

public class LoginManager extends UnicastRemoteObject implements ILoginManager {

	private Gateway gateway;
	private Map salts;
	private Random prng;
	
	public LoginManager() throws RemoteException {
		super();
		gateway = new Gateway();
		salts = new HashMap();
		prng = new Random();
	}

	private static void log(String message) {
		System.err.println(message);
	}
	
	public synchronized byte[] getSalt(String userName)
			throws RemoteException {
		byte[] salt = Utilities.longToByteArray(prng.nextLong());
		salts.put(userName, salt);
		return salt;
	}
	
	public synchronized boolean login(String userName, byte[] passcode, IClient client)
			throws RemoteException {
		if (!gateway.isConnected(userName) && salts.containsKey(userName) &&
				checkPasscode(userName, passcode)) {
			gateway.addClient(userName, client);
			salts.remove(userName);
			return true;
		} else {
			salts.remove(userName);
			return false;
		}
	}
	
	private String passwordForUser(String userName) {
		return "prova";
	}
	
	private boolean checkPasscode(String userName, byte[] passcode) {
		String password = passwordForUser(userName);
		byte[] salt = (byte[]) salts.get(userName);
		return Arrays.equals(passcode, Utilities.passwordToPasscode(password, salt));
	}
	
	public static void main(String[] args) throws Exception {
		Utilities.rmiSetup();
		
		LocateRegistry.createRegistry(PORT).bind(SERVICE_NAME, new LoginManager());
		log("Service " + SERVICE_NAME + " registered on port " + PORT + ".");
	}

}
