package Game;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import Signal.Speaker;
import Exceptions.ImpossibleAction;
import GameObjects.Pawn;
import GameObjects.Player;
import GameObjects.Treasure;




/*
 * This class represents the Maze
 */
public class Maze extends Speaker implements Serializable{
	
	private static final long serialVersionUID = 2826378832091803411L;
	private int height;
	private int width;
	private Position[][] grid;
	private ConcurrentHashMap<String, Player> players;
	private ConcurrentHashMap<String, Treasure> treasures;
	private boolean isOver=true;
	private int freeTreasures;

	public Maze(){
	}
	public void update(LinkedList<Action> actions){
		if(actions==null)
			return;
		for(Action action: actions){
			if(isValid(action))
				updateAux(action);
				event();	
		}
	}
	public void update(Action action) throws ImpossibleAction {
		if(!(isValid(action)))
			throw new ImpossibleAction(action);
		updateAux(action);
		event();
	}
	private void updateAux(Action action) {
			if(action.isMove())
				move(action);
			else if(action.isNewPlayer())
				addPlayer(action);
			else if(action.isNewTreasure())
				addTreasure(action);
			else if(action.isNewGame())
				init(action);
	}

	private void addTreasure(Action action){
		int x = action.getX();
		int y = action.getY();
		Treasure treasure=new Treasure(grid[x][y], action.getId());
		treasures.put(treasure.getId(), treasure);
		freeTreasures++;
	}
	private void addPlayer(Action action){
		int x = action.getX();
		int y = action.getY();
		String id = action.getId();
		Player player = new Player(grid[x][y], id);
		players.put(id, player);
		collectTreasure(player);
	}
	private void move(Action action){
		Direction direction=action.getDirection();
		if(direction==Direction.NOMOVE)
			return;
		Player player = players.get(action.getId());
		Position newPosition=newPosition(action);	
		player.setPosition(newPosition);
		collectTreasure(player);
		isOver=(isOver || freeTreasures<=0);
	}	
	private void init(Action action){
		players=new ConcurrentHashMap<String, Player>();
		treasures=new ConcurrentHashMap<String, Treasure>();
		this.height=action.getX();
		this.width=action.getY();
		grid=new Position[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				grid[x][y]=new Position(x,y);
			}
		}
		isOver=false;
		freeTreasures=0;
	}

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	private boolean isFreeTreasure(Position position){
		boolean isFree=false;
		if(position.getPawns()==null || position.getPawns().isEmpty())
			return false;
		for(Pawn pawn : position.getPawns()){
			if(pawn.getClass()==Treasure.class){
				Treasure treasure=(Treasure) pawn;
				isFree=isFree||(!(treasure.isOwned()));
			}
		}
		return isFree;
	}
	private boolean isPlayer(Position position){
		if(position.getPawns()==null)
			return false;
		for(Pawn pawn : position.getPawns())
				if(pawn.getClass()==Player.class)
					return true;
		return false;
	}
	private boolean isTreasure(Position position){
		if(position.getPawns()==null)
			return false;
		for(Pawn pawn : position.getPawns())
				if(pawn.getClass()==Treasure.class)
					return true;
		return false;
	}
	private Position newPosition(Action action){
		Position position=players.get(action.getId()).getPosition();
		switch (action.getDirection()) {
		case NORTH:
			return (grid[position.getX()][position.getY()+1]);
		case SOUTH:
			return (grid[position.getX()][position.getY()-1]);
		case EAST:
			 return (grid[position.getX()+1][position.getY()]);
		case WEST:
			return (grid[position.getX()-1][position.getY()]);
		case NOMOVE:
			break;
		}
		return null;
	}
	private boolean isValidMove(Action action){
		if(!(players.containsKey(action.getId())))
			return false;
		Player player=players.get(action.getId()); 
		Direction direction=action.getDirection();
		return !(player.getX()==0 & direction==Direction.WEST ||
				player.getX()==width-1 & direction==Direction.EAST ||
				player.getY()==0 & direction==Direction.SOUTH ||
				player.getY()==height-1 & direction==Direction.NORTH);
	}
	private void collectTreasure(Player player){
		for(Pawn pawn : player.getPosition().getPawns()){
			if(pawn.getClass()==Treasure.class){
				Treasure treasure=(Treasure) pawn;
				if(!(treasure.isOwned())){
					treasure.ownedBy(player);
					player.addTreasure(treasure);
					freeTreasures--;
				}
			}
		}
	}
	public boolean isFreeTreasure(int x, int y){
		return isFreeTreasure(grid[x][y]);
	}
	public boolean isTreasure(int x, int y){
		return isTreasure(grid[x][y]);
	}
	public boolean isPlayer(int x, int y){
		return isPlayer(grid[x][y]);
	}
	public boolean isOver(){
		return isOver;
	}
	public boolean isValid(Action action){
		if(action==null)
			return false;
		if(action.isNewGame())
			return (action.getX()>0 & action.getY()>0);
		if(isOver)
			return action.isMove() & action.getDirection()==Direction.NOMOVE;
		if(action.isNewPlayer())
			return (!(players.containsKey(action.getId())) & 
					action.getX()>=0 & action.getY()>=0 & action.getY()<height & action.getX()<width & 
					!(isPlayer(grid[action.getX()][action.getY()]) ));
		if(action.isMove())
			return isValidMove(action);
		if(action.isNewTreasure())
			return (action.getX()>=0 & action.getY()>=0 & action.getY()<height & action.getX()<width);
		return false;
	}
}
