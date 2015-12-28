package Trash;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameMethods extends Remote{
	
	public void sayHello(String name) throws RemoteException;

}
