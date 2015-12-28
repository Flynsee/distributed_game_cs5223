package P2P;

import java.net.Inet4Address;
import java.net.UnknownHostException;
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
import java.util.concurrent.locks.ReentrantLock;

import Exceptions.ImpossibleAction;
import Game.Action;
import Game.Memory;
import Structures.Address;
import Game.Maze;

public class Server extends UnicastRemoteObject implements RemoteServer{

	private static final long serialVersionUID = -7850796484316633059L;

	private Memory memory;
	private Maze maze;
	private Client client;
	
	private boolean isPrimary=false, isBackup=false;
	private ReentrantLock lock=new ReentrantLock();
	
	private Address localAddress;
	private RemoteServer primaryServer, backupServer;
	private HashMap<String, Address> addresses;
	private LinkedList<String> players;
	
	private Registry registry;

	private Thread controller=new Thread(){
		public void run() {
			control();
		}
	};


	public Server(Client client) throws RemoteException, AlreadyBoundException, UnknownHostException {
		int port=Address.freePort();
		registry = LocateRegistry.createRegistry(port);
		String localProcessName=this.toString();
		localAddress=new Address(Inet4Address.getLocalHost().getHostAddress(), port, localProcessName);
		registry.bind(localProcessName, this);
		this.client=client;
		client.setLocalServerAddress(localAddress);
	}

	public LinkedList<Action> play(Action action) throws RemoteException {
		lock.lock();
		if(!(action.isMove())){
			lock.unlock();
			return null;
		}
		if(!isPrimary & !isBackup){
			lock.unlock();
			return null;
		}
		try {
			maze.update(action);
			memory.memorize(action);
			tryClose();
			if(!isBackup)
				backupServer.play(action);
			lock.unlock();
			return memory.recall(action);
		} catch (ImpossibleAction e) {
			lock.unlock();
			return null;
		}
	}

	public void setPrimary(Address address) throws RemoteException {
		try {
			primaryServer = (RemoteServer) locate(address);
			client.setPrimary(primaryServer);
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	public void setBackup(Address address) throws RemoteException {
		try {
			backupServer = locate(address);
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	private static RemoteServer locate(Address address) throws AccessException, RemoteException, NotBoundException{
		return (RemoteServer) LocateRegistry.getRegistry(address.getHost(), address.getPort()).lookup(address.getProcessName());
	}

	public void startPrimary(Maze maze, Memory memory, HashMap<String, Address> addresses,
			LinkedList<String> players) throws RemoteException {
		lock.lock();
		this.maze=maze;
		this.memory=memory;
		this.addresses=addresses;
		this.players=players;
		controller.start();
		lock.unlock();
		for(String player : players)
			try {
				locate(addresses.get(player)).setPrimary(localAddress);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		isPrimary=true;
		System.out.println("Started primary server on localhost");
	}

	public void startBackup(Maze maze, Memory memory, HashMap<String, Address> addresses,
			LinkedList<String> players) throws RemoteException {
		lock.lock();
		this.maze=maze;
		this.memory=memory;
		this.addresses=addresses;
		this.players=players;
		controller.start();
		lock.unlock();
		for(String player : players)
			try {
				locate(addresses.get(player)).setBackup(localAddress);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		isBackup=true;
		System.out.println("Started backup server on localhost");
	}
	
	private void recreateBackup(){
		lock.lock();
		if(!(isPrimary))
			return;
		boolean worked=false;
		int next=0;
		while(!(worked) && next<players.size()){
			try {
				String player=players.get(next);
				if(addresses.get(player)!=localAddress)
					locate(addresses.get(player)).startBackup(maze, memory, addresses, this.players);
				worked=true;
			} catch (RemoteException | NotBoundException e) {
				worked=false;
				next++;
			};
		}
		lock.unlock();
	}
	private void recreatePrimary(){
		lock.lock();
		if(!(isBackup))
			return;
		boolean worked=false;
		int next=0;
		while(!(worked) && next<players.size()){
			try {
				String player=players.get(next);
				if(addresses.get(player)!=localAddress)
					locate(addresses.get(player)).startPrimary(maze, memory, addresses, this.players);
				worked=true;
			} catch (RemoteException | NotBoundException e) {
				worked=false;
				next++;
			};
		}
		lock.unlock();
	}
	
	private void control(){
		if(isBackup)
			while(true){
				try {
					if(primaryServer!=null)
						primaryServer.isAlive();
					Thread.sleep(2000);
				} catch (RemoteException e) {
					recreatePrimary();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		else if(isPrimary)
			while(true){
				try {
					if(backupServer!=null)
						backupServer.isAlive();
					Thread.sleep(2000);
				} catch (RemoteException e) {
					recreateBackup();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	}
	
	public void isAlive() throws RemoteException {
	}

	private void tryClose() {
		if(!isPrimary)
			return;
		if(!maze.isOver())
			return;
		if(!memory.isEmpty())
			return;
		for(String player : players){
			try {
				if(addresses.get(player)!=localAddress)
					locate(addresses.get(player)).close();
			} catch (RemoteException | NotBoundException e) {
			};
		}
		try {
			UnicastRemoteObject.unexportObject(registry, true);
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}
	}
	public void close() throws RemoteException{
		if(isPrimary)
			return;
		UnicastRemoteObject.unexportObject(registry, true);
	}
}
