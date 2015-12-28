package Trash;

import java.util.HashMap;
import java.util.LinkedList;

import Game.Position;
import GameObjects.Pawn;

/*
 * This class represents a set of elements of a subclass of Pawn
 */
public class Set<E extends Pawn>{
	private HashMap<String, E> collection=new HashMap<String, E>();
	private HashMap<Position, LinkedList<E>> grid=new HashMap<Position, LinkedList<E>>();;
	
	public Set() {
	}
	
	public void add(E pawn){
		String id=pawn.getId();
		Position position=pawn.getPosition();
		if(collection.containsKey(id)){
			throw new IllegalArgumentException(id+ " is already present");
		}
		this.collection.put(id, pawn);
		if(!this.grid.containsKey(position))
			this.grid.put(position, new LinkedList<E>());
		this.grid.get(position).add(pawn);
	}
	public void remove(E pawn){
		collection.remove(pawn.getId());
		grid.get(pawn.getPosition()).remove(pawn);
		if(grid.get(pawn.getPosition()).size()==0){
			grid.remove(pawn.getPosition());
		}
	}
	public E getById(String id){
		return collection.get(id);
	}
	public E getByPos(int x, int y){
		LinkedList<E> list = grid.get(x+","+y);
		if(list==null)
			return null;
		return list.getFirst();
	}
	public int getPosCount(int x, int y){
		LinkedList<E> list = grid.get(x+","+y);
		if(list==null)
			return 0;
		return list.size();
	}
	public int getSize() {
		return collection.size();
	}
}
