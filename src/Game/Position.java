package Game;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;

import GameObjects.Pawn;

public class Position implements Serializable{

	private static final long serialVersionUID = 5982132189245147120L;
	private int x;
	private int y;
	private ConcurrentLinkedQueue<Pawn> pawns=new ConcurrentLinkedQueue<Pawn>();
	
	public Position(int x, int y) {
		this.x=x;
		this.y=y;
	}
	public void add(Pawn pawn){
		pawns.add(pawn);
	}
	public void remove(Pawn pawn){
		pawns.remove(pawn);
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public ConcurrentLinkedQueue<Pawn> getPawns() {
		return pawns;
	}
}
