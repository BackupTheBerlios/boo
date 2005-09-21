package boo.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {

	public void ping() throws RemoteException;

}