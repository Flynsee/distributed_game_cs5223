package GameObjects;

import java.io.Serializable;

import Game.Position;

/*
 * Class representing an object on the grid. 
 */
public abstract class Pawn implements Serializable{

	private static final long serialVersionUID = 7015348209883556734L;
	private String id;
	private Position position;

	Pawn(Position position, String id){
		this.position=position;
		position.add(this);
		this.id=id;
	}
	public void setPosition(Position newPosition){
		position.remove(this);
		position=newPosition;
		position.add(this);
	}
	public Position getPosition(){
		return position;
	}
	public int getX() {
		return position.getX();
	}
	public int getY() {
		return position.getY();
	}
	public String getId() {
		return id;
	}
}
