package Game;

import java.io.Serializable;

public class Action implements Serializable{
	private static final long serialVersionUID = 8688092057661059263L;
	enum Type{
		NEWPLAYER, NEWTREASURE, MOVE, NEWGAME;
	}
	private Type type;
	private Direction direction;
	private String id;
	private int x,y;
	
	private Action(){
	}
	
	public static Action newTreasure(String id, int x, int y) {
		Action action=new Action();
		action.type=Type.NEWTREASURE;
		action.x=x;
		action.y=y;
		action.id=id;
		return action;
	}
	public static Action newGame(int x, int y) {
		Action action=new Action();
		action.type=Type.NEWGAME;
		action.x=x;
		action.y=y;
		return action;
	}
	public static Action newPlayer(String id, int x, int y) {
		Action action=new Action();
		action.type=Type.NEWPLAYER;
		action.id=id;
		action.x=x;
		action.y=y;
		return action;
	}
	public static Action newMove(String id, Direction direction) {
		Action action=new Action();
		action.type=Type.MOVE;
		action.id=id;
		action.direction=direction;
		return action;
	}
	public boolean isNewPlayer(){
		return type==Type.NEWPLAYER;
	}
	public boolean isNewTreasure(){
		return type==Type.NEWTREASURE;
	}
	public boolean isMove(){
		return type==Type.MOVE;
	}
	public Direction getDirection() {
		return direction;
	}
	public String getId() {
		return id;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public boolean isNewGame() {
		return type==Type.NEWGAME;
	}
	public void print(){
		System.out.println(getText());
	}
	public String getText(){
		return "Type: "+type+ " - X,Y="+x+","+y+" - Id="+id;
	}

	public String getType() {
		return type.toString();
	}
	
}
