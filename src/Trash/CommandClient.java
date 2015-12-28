package Trash;

import java.util.Arrays;
import java.util.Scanner;

import Game.Direction;
import ServerClient.Client;

public class CommandClient extends Thread {
	private Client client;
	private Scanner input=new Scanner(System.in);
	
	public CommandClient(Client client) {
		this.client=client;
	}
	
	public void run(){
		client.enter();
		while(!client.getMaze().isOver() && client.isPlaying())
			play();
		input.close();
	}

	private void play() {
		System.out.println("What is your next move?");
		String[] moves = {"NORTH", "SOUTH", "EAST", "WEST", "NOMOVE"};
		String direction=input.nextLine();
		if(Arrays.asList(moves).contains(direction))
			client.play(Direction.valueOf(direction));	
	}	
}
