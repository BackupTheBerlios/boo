package boo.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import boo.client.IClient;

public interface ILoginManager extends Remote {

	public static final int PORT = 20100;
	public static final String SERVICE_NAME = "Boo";
	
	public byte[] getSalt(String userName)
			throws RemoteException;
	
	public boolean login(String userName, byte[] password, IClient client)
			throws RemoteException;

}