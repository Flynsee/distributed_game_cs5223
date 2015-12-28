package Trash;


import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MyServer {
	
	
	public void startServer(){
		

		Registry registry  = null;								
		
		try {
			
			GameImplementation gs = new GameImplementation();
			registry = LocateRegistry.createRegistry(9000);
			registry.bind("GameImplementation", gs);
			System.out.println("Server Started");
			
		} catch (RemoteException re) {
			
			System.err.println("Server Cannot be Started");
			re.printStackTrace();
		} catch(AlreadyBoundException abe){
			
			System.err.println("Already Bounded");
			abe.printStackTrace();
		}	    		
		 
	}

}
