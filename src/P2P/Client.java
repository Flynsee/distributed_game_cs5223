package P2P;

import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.LinkedList;

import Exceptions.ImpossibleAction;
import Game.Action;
import Game.Maze;
import Structures.Address;
import Game.Direction;



public class Client{
	
	private RemoteServer primaryServer;
	private RemoteBootstrapServer initialServer;
	private String id;
	private Maze maze;
	private Address localServerAddress;
	private Server server;
				
	public Client() throws AccessException, RemoteException, AlreadyBoundException, UnknownHostException, NotBoundException{
		maze=new Maze();
		server=new Server(this);
	}
	
	
	
	private void setInitial(Address address) throws RemoteException, NotBoundException{
	    initialServer = (RemoteBootstrapServer) LocateRegistry.getRegistry(address.getHost(), address.getPort()).lookup(address.getProcessName());
	}
	public void setPrimary(RemoteServer primaryServer) throws RemoteException{
	    this.primaryServer=primaryServer;
	    play(Direction.NOMOVE);
	}

	public void join(Address address){
		LinkedList<Action> actions=null;
		try {
			setInitial(address);
			actions=initialServer.join(localServerAddress);
		} catch (RemoteException | NotBoundException e) {
			printErr("Server not reachable");
			return;
		}
		if(actions==null){
			printErr("Server won't accept connexions");
			return;
		}
		maze.update(actions);
		id=actions.getLast().getId();
	}

	public void play(Direction direction){
		try {
			LinkedList<Action> actions;
			actions = primaryServer.play(Action.newMove(id, direction));
			if(actions==null & direction!=Direction.NOMOVE)
				throw new ImpossibleAction(Action.newMove(id, direction));
			maze.update(actions);
			if(isOver())
				server.close();
		} catch (RemoteException e) {
			printErr("Please wait to play, the server is not reachable");
		} catch (ImpossibleAction e) {
			printErr("Impossible to play " + direction);
		}
	}
	
	public Maze getMaze() {
		return maze;
	}
	private void printErr(String text){
		System.err.println("Client "+ id+"@"+localServerAddress.print()+" says: "+text);
	}
	public void setLocalServerAddress(Address localServerAddress) {
		this.localServerAddress = localServerAddress;
	}
	public boolean isOver(){
		return maze.isOver();
	}
}

	