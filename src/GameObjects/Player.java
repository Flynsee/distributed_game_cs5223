package GameObjects;

import java.util.LinkedList;

import Game.Position;

/*
 * This class represent a player on the grid
 */
public class Player extends Pawn{
	private static final long serialVersionUID = -7826294091610987564L;

	public Player(Position position, String id) {
		super(position, id);
	}
	private LinkedList<Treasure> loot=new LinkedList<Treasure>();
	
	public void addTreasure(Treasure treasure){
		this.loot.add(treasure);
	}
	public int countTreasures(){
		return loot.size();
	}
	public LinkedList<Treasure> getTreasures(){
		return loot;
	}
}
