package Game;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Memory implements Serializable{

	private static final long serialVersionUID = -5555551563335180729L;
	private ConcurrentLinkedQueue<String> players=new ConcurrentLinkedQueue<String>();
	private ConcurrentHashMap<String, LinkedList<Action>> playersMemory=new ConcurrentHashMap<String, LinkedList<Action>>();
	private ConcurrentLinkedQueue<Action> initialMemory=new ConcurrentLinkedQueue<Action>();
	
	public void closeEntry(){
		initialMemory=null;
	}
	
	public LinkedList<Action> recall(Action action) {
		if(action.isNewPlayer() || action.isMove()){
			LinkedList<Action> copy=new LinkedList<Action>(playersMemory.get(action.getId()));
			playersMemory.put(action.getId(),new LinkedList<Action>());
			return copy;
		}
		return null;
	}

	public void memorize(Action action) {
		if(action.isMove() & action.getDirection()==Direction.NOMOVE)
			return;
		if(action.isNewPlayer()){
			players.add(action.getId());
			playersMemory.put(action.getId(),new LinkedList<Action>(initialMemory));
		}
		if(initialMemory!=null)
			initialMemory.add(action);
		for(String id: players)
			playersMemory.get(id).add(action);		
	}

	public boolean isEmpty() {
		for(String player : players)
			if(!playersMemory.get(player).isEmpty())
				return false;
		return true;
	}
}
