

package Trash;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MyClient {
	
	
	public void startClient(){
		
		GameMethods gm = null;
		
		try {
			Registry reg = LocateRegistry.getRegistry("localhost",9000);
			
			gm = (GameMethods) reg.lookup("GameImplementation");
			
			
			
			gm.sayHello("Astha");
			
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
