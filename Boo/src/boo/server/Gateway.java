package boo.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Gateway extends UnicastRemoteObject implements IGateway {

	public Gateway() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

}
