package boo.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import boo.client.IClient;

public interface ILoginManager extends Remote {

	public static final int PORT = 5000;

	public IGateway login(String userName, String password, IClient client)
			throws RemoteException;

}