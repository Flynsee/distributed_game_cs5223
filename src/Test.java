
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import Exceptions.ImpossibleAction;
import GUI.GameWindow;
import Game.Direction;
import P2P.BootstrapServer;
import P2P.Client;
import Structures.Address;

public class Test {

	public static void main(String[] args) throws InterruptedException, RemoteException, NotBoundException, AlreadyBoundException, ImpossibleAction, UnknownHostException {
		BootstrapServer s=new BootstrapServer(Address.getInitialAddress(), 30, 100);
		Client c1=new Client(); new GameWindow(c1.getMaze());
		Client c2=new Client(); new GameWindow(c2. getMaze());
		c1.join(Address.getInitialAddress());		
		for(int i=0; i<99; i++){
			new Client().join(Address.getInitialAddress());
		}
		c2.join(Address.getInitialAddress());		
		s.close();		
		c1.play(Direction.NORTH);		
		c1.play(Direction.NORTH);		
		c2.play(Direction.SOUTH);
	}

}
