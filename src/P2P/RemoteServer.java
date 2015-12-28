package P2P;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;

import Game.Action;
import Game.Memory;
import Structures.Address;
import Game.Maze;

public interface RemoteServer extends Remote{
	public LinkedList<Action> play(Action action) throws RemoteException;
	public void startPrimary(Maze maze, Memory memory, HashMap<String, Address> addresses, LinkedList<String> players) throws RemoteException;
	public void startBackup(Maze maze, Memory memory, HashMap<String, Address> addresses, LinkedList<String> players) throws RemoteException;
	public void setPrimary(Address address) throws RemoteException;
	public void setBackup(Address address) throws RemoteException;
	public void isAlive() throws RemoteException;
	public void close() throws RemoteException;
}
