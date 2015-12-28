package ServerClient;

public abstract class Messenger {
	String name;
	public Messenger(String name){
		this.name=name;
	}
	public void printErr(String text){
		System.err.println(name+" says: "+text);
	}
	public void printOut(String text){
		System.err.println(name+" says: "+text);
	}
}
