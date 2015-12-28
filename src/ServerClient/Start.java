package ServerClient;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Scanner;

import GUI.GameWindow;
import Game.Direction;

public class Start {
	
	public static void main(String[] args) throws InterruptedException, RemoteException, NotBoundException, AlreadyBoundException{
		start(args);
	}
	
	public static void start(String[] args) throws RemoteException, NotBoundException, AlreadyBoundException{
		if(args[0].equals("client"))
			startClient();
		else if(args[0].equals("server"))
			startServer();
	}

	public static void startServer() throws AccessException, RemoteException, AlreadyBoundException {
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
		
		ServerClient.Server server= new ServerClient.Server(N,M);
		String[] possibleAnswers = {"y", "YES", "yes", "Y"};
		System.out.println("Do you want a graphic interface?");
		String answer=input.nextLine();
		if(Arrays.asList(possibleAnswers).contains(answer))
			new GameWindow(server.getMaze(), "Server");
		input.close();
	}

	public static void startClient() throws RemoteException, NotBoundException {
		
		Scanner input=new Scanner(System.in);
		System.out.println("What is the host?");
		String host=input.nextLine();
		System.out.println("Choose your player ID.");
		String id=input.nextLine();
		System.out.println("Choose your password.");
		String password=input.nextLine();
		ServerClient.Client client=new ServerClient.Client(host, id, password);
		String[] possibleAnswers = {"y", "YES", "yes", "Y"};
		System.out.println("Do you want a graphic interface?");
		String answer=input.nextLine();
		if(Arrays.asList(possibleAnswers).contains(answer))
			new GameWindow(client.getMaze(), id + " @ " + host);
		client.enter();
		while(!client.getMaze().isOver() && client.isPlaying()){
			System.out.println("What is your next move?");
			String[] moves = {"NORTH", "SOUTH", "EAST", "WEST", "NOMOVE"};
			String direction=input.nextLine();
			if(Arrays.asList(moves).contains(direction))
				client.play(Direction.valueOf(direction));	
		}
		input.close();
		
		
	}
	
	
	
}
