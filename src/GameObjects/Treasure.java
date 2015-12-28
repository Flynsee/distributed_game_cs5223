package GameObjects;

import Game.Position;

/*
 * Class representing a treasure in the game.
 */
public class Treasure extends Pawn{

	private static final long serialVersionUID = -4650568747210718185L;
	private Player owner=null;
	
	public Treasure(Position position, String id) {
		super(position, id);
	}
	public boolean isOwned(){
		return !(this.owner==null);
	}
	public Player getOwner() {
		return owner;
	}
	public void ownedBy(Player player){
		this.owner=player;
	}
}
