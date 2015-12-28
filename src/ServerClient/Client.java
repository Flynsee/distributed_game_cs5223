package ServerClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

import Game.Action;
import Game.Direction;
import Game.Maze;

public class Client extends Messenger{
	private Maze maze;
	private RemoteServer remoteServer;
	private String id;
	private String password;
	private String name = Settings.serverName;
	private int port = Settings.serverPort;
	private boolean isPlaying=false;
	
	public Client(String host, String id, String password) throws RemoteException, NotBoundException{
		super("Client " +id);
		maze=new Maze();
		this.id=id;
		this.password=password;
		connect(host);
	}
	
	private void connect(String host) throws RemoteException, NotBoundException{
	    Registry registry = LocateRegistry.getRegistry(host, port);
	    remoteServer = (RemoteServer) registry.lookup(name);
	}
	
	public void enter(){
		try {
			LinkedList<Action> actions;
			if(isPlaying){
				printErr("The player "+id+" is already in the game");
				return;
			}
			actions = remoteServer.enter(id, password);
			maze.update(actions);
			isPlaying=true;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void play(Direction direction){
		try {
			if(!isPlaying){
				printErr("The player "+id+" is not in the game");
				return;
			}
			LinkedList<Action> actions;
			actions = remoteServer.play(id, direction, password);
			maze.update(actions);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	public Maze getMaze() {
		return maze;
	}
	public boolean isPlaying(){
		return isPlaying;
	}
}
