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

	public IGateway login(String userName, String password, IClient client)
			throws RemoteException {
		if (userName.equals("Domenico") && password.equals("ciao")) {
			return gateway;
		} else {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		LocateRegistry.createRegistry(PORT).bind("Boo", new LoginManager());
	}

}
