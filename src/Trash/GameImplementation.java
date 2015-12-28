package Trash;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GameImplementation extends UnicastRemoteObject implements GameMethods {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 11232L;

	public GameImplementation() throws RemoteException{
		
		
	}
	
	public synchronized void sayHello(String name) throws RemoteException{
		
		System.out.println("Hello "+name);
	}
	
}
