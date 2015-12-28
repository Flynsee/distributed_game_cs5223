package P2P;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import Exceptions.ImpossibleAction;
import Game.Action;
import Game.Memory;
import Structures.Address;
import Structures.Pair;
import Game.Maze;

public class BootstrapServer extends UnicastRemoteObject implements RemoteBootstrapServer{

	private static final long serialVersionUID = -7850796484316633059L;
	private Memory memory=new Memory();
	private Maze maze= new Maze();
	private HashMap<String, Address> addresses=new HashMap<String, Address>();
	private LinkedList<String> players=new LinkedList<String>();
	private LinkedList<Pair> availablePositions=new LinkedList<Pair>();
	private Random random=new Random();
	private int duration=0;
	private Registry registry;
	private ReentrantLock lock=new ReentrantLock();

	public BootstrapServer(Address address, int N, int M) throws RemoteException, AlreadyBoundException, InterruptedException, NotBoundException{
		registry = LocateRegistry.createRegistry(address.getPort());
		registry.bind(address.getProcessName(), this);
		startGame(N, N, M);
		if(duration>0)
		new Thread(){
			public void run() {
				try{
					Thread.sleep(duration);
					close();
				}
				catch(InterruptedException | RemoteException e){
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void close() throws NoSuchObjectException {
		lock.lock();
		System.out.println("Closing bootstrap server");
		memory.closeEntry();
		createPrimary(createBackup());
		UnicastRemoteObject.unexportObject(registry, true);
		System.out.println("Bootstrap server closed");
	}

	private static RemoteServer locate(Address address) throws AccessException, RemoteException, NotBoundException{
		return (RemoteServer) LocateRegistry.getRegistry(address.getHost(), address.getPort()).lookup(address.getProcessName());
	}
	
	private int createBackup() {
		boolean worked=false;
		int next=0;
		while(!(worked) && next<players.size()){
			try {
				locate(addresses.get(players.get(next))).startBackup(maze, memory, addresses, this.players);
				worked=true;
			} catch (RemoteException | NotBoundException e) {
				worked=false;
				next++;
			};
		}
		return next;
	}

	private void createPrimary(int backupIndex) {
		boolean worked=false;
		int next=0;
		while(!(worked) && next<players.size()){
			if(next==backupIndex)
				next++;
			try {
				locate(addresses.get(players.get(next))).startPrimary(maze, memory, addresses, this.players);
				worked=true;
			} catch (RemoteException | NotBoundException e) {
				worked=false;
				next++;
			};
		}
	}

	private void startGame(int height, int width, int M){
		lock.lock();
		LinkedList<Action> actions=new LinkedList<Action>();
		Action action=Action.newGame(height, width);
		memory.memorize(action);
		actions.add(action);
		for(int i=0; i<M; i++){
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			action=Action.newTreasure(String.valueOf(i), x, y);
			actions.add(action);
			memory.memorize(action);
		}
		maze.update(actions);
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				availablePositions.add(new Pair(x,y));
			}
		}
		lock.unlock();
	}
	
	public LinkedList<Action> join(Address address){
		lock.lock();
		if(availablePositions==null || availablePositions.size()==0){
			lock.unlock();
			return null;
		}
		String id=Integer.toString(players.size());
		int r=random.nextInt(availablePositions.size());
		Pair pair=availablePositions.remove(r);
		Action action=Action.newPlayer(id, pair.getX(), pair.getY());
		try {
			maze.update(action);
			addresses.put(id, address);
			players.add(id);
			memory.memorize(action);
			lock.unlock();
			return memory.recall(action);
		} catch (ImpossibleAction e){
			e.printStackTrace();
			lock.unlock();
			return null;
		}
		
	}

}
