package boo.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import boo.client.IClient;

public class LoginManager extends UnicastRemoteObject implements ILoginManager {

	private Gateway gateway;

	public LoginManager() throws RemoteException {
		super();
		gateway = new Gateway();
	}

	private static void log(String message) {
		System.err.println(message);
	}

	public synchronized IGateway login(String userName, String password, IClient client)
			throws RemoteException {
		if (!gateway.isConnected(userName) && password.equals("prova")) {
			gateway.addClient(userName, client);
			return gateway;
		} else {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		LocateRegistry.createRegistry(PORT).bind(SERVICE_NAME,
				new LoginManager());
		log("Service " + SERVICE_NAME + " registered on port " + PORT + ".");
	}

}
