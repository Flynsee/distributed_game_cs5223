

import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Scanner;

import GUI.GameWindow;
import Game.Direction;
import P2P.BootstrapServer;
import P2P.Client;
import Structures.Address;

public class Start {
	
	public static void start(String args) throws RemoteException, AlreadyBoundException, InterruptedException, NotBoundException, UnknownHostException{
		if(args.equals("peer"))
			startPeer();
		else if(args.equals("bootstrap"))
			startBootstrapServer();
	}

	public static void startBootstrapServer() throws RemoteException, AlreadyBoundException, InterruptedException, NotBoundException {
		Scanner input=new Scanner(System.in);
		System.out.println("What will be the size of the game?");
		int N=Integer.parseInt(input.nextLine());
		if(!(N>0)){
			input.close();
			return;
		}
		System.out.println("How many treasures do you want to start with?");
		int M=Integer.parseInt(input.nextLine());
		if(!(M>0)){
			input.close();
			return;
		}
		BootstrapServer s=new BootstrapServer(Address.getInitialAddress(), N, M);
		System.out.println("Type CLOSE to close the Bootstrap server");
		while(!input.nextLine().equals("CLOSE"));
		s.close();
		input.close();
	}

	public static void startPeer() throws AccessException, RemoteException, UnknownHostException, AlreadyBoundException, NotBoundException {
		Scanner input=new Scanner(System.in);
		Client peer=new Client();
		String[] possibleAnswers = {"y", "YES", "yes", "Y"};
		System.out.println("Do you want a graphic interface?");
		String answer=input.nextLine();
		if(Arrays.asList(possibleAnswers).contains(answer))
			new GameWindow(peer.getMaze());
		peer.join(Address.getInitialAddress());
		while(!peer.isOver()){
			System.out.println("What is your next move?");
			String[] moves = {"NORTH", "SOUTH", "EAST", "WEST", "NOMOVE"};
			String direction=input.nextLine();
			if(Arrays.asList(moves).contains(direction))
				peer.play(Direction.valueOf(direction));	
		}
		input.close();		
	}
	
	
	
}
