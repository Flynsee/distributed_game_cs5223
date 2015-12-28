package ServerClient;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

import Game.Action;
import Game.Direction;

public interface RemoteServer extends Remote, Serializable{
	public LinkedList<Action> play(String id, Direction direction, String password) throws RemoteException;
	public LinkedList<Action> enter(String id, String password) throws RemoteException;
}
