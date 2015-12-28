package P2P;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

import Game.Action;
import Structures.Address;

public interface RemoteBootstrapServer extends Remote{
	public LinkedList<Action> join(Address address) throws RemoteException;
}
