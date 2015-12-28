package Trash;

public class ImpossibleMove extends Exception{
	private static final long serialVersionUID = 1L;
	public ImpossibleMove(String id, String dir) {
		super("The move "+ dir+ " is not possible for the player "+id); 
	}
}
