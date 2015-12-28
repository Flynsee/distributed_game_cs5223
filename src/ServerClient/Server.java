package ServerClient;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import Game.Action;
import Game.Direction;
import Game.Maze;



public class Server extends UnicastRemoteObject implements RemoteServer{

	private static final long serialVersionUID = 2771560553410170098L;
	private class Pair{
		private int x,y;
		Pair(int x, int y){
			this.x=x;
			this.y=y;
		}
	}
	private Maze maze;
	private LinkedList<Pair> availablePositions=new LinkedList<Pair>();
	private LinkedList<String> players=new LinkedList<String>();
	private HashMap<String, String> passwords=new HashMap<String, String>();
	private HashMap<String, LinkedList<Action>> playersMemory=new HashMap<String, LinkedList<Action>>();
	private LinkedList<Action> globalMemory=new LinkedList<Action>();
	private Random random=new Random();
	private Registry registry;
	private int port=Settings.serverPort;
	private String name=Settings.serverName;
	
	
	public Server(int N, int M) throws AccessException, RemoteException, AlreadyBoundException{
		registry = LocateRegistry.createRegistry(port);
		registry.bind(name, this);
		startGame(N,N,M);
	}
	
	private boolean verifyPassword(String id, String password){
		return passwords.get(id).equals(password);
	}
	
	private void startGame(int height, int width, int M){
		LinkedList<Action> actions=new LinkedList<Action>();
		updateMemory(Action.newGame(height, width));
		actions.add(Action.newGame(height, width));
		for(int i=0; i<M; i++){
			int x = random.nextInt(maze.getWidth());
			int y = random.nextInt(maze.getHeight());
			actions.add(Action.newTreasure(String.valueOf(i),x, y));
			updateMemory(Action.newTreasure(String.valueOf(i),x, y));
		}
		maze.update(actions);
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				availablePositions.add(new Pair(x,y));
			}
		}
	}
	
	private void updateMemory(Action action) {
		globalMemory.add(action);
		for(String id: players)
			playersMemory.get(id).add(action);
	}

	public LinkedList<Action> play(String id, Direction direction, String password) {
		if(!verifyPassword(id, password))
			return null;
		LinkedList<Action> actions=new LinkedList<Action>();
		actions.add(Action.newMove(id, direction));
		maze.update(actions);
		updateMemory(Action.newMove(id, direction));
		LinkedList<Action> copy=new LinkedList<Action>(playersMemory.get(id));
		playersMemory.put(id,new LinkedList<Action>());
		return copy;
	}

	public LinkedList<Action> enter(String id, String password){
		if(players.contains(id))
			return null;
		if(availablePositions.size()==0)
			return null;
		int r=random.nextInt(availablePositions.size());
		Pair pair=availablePositions.remove(r);
		LinkedList<Action> actions=new LinkedList<Action>();
		actions.add(Action.newPlayer(id, pair.x, pair.y));
		maze.update(actions);
		updateMemory(Action.newPlayer(id, pair.x, pair.y));
		players.add(id);
		passwords.put(id, password);
		playersMemory.put(id, new LinkedList<Action>());
		return globalMemory;
	}
	public Maze getMaze() {
		return maze;
	}
}
