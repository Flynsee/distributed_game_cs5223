package Signal;

public abstract class Speaker {
	Listener listener;
	public void bind(Listener listener){
		this.listener=(listener);
	}
	protected void event(){
		if(listener!=null)
			listener.event();
	}
}
