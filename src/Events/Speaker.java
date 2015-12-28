package Events;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Speaker<E> {
	private LinkedList<Listener<E>> listeners=new LinkedList<Listener<E>>();
	private LinkedBlockingQueue<E> list=new LinkedBlockingQueue<E>();
	private Thread speaker = 
	new Thread(){
		public void run() {
			while(true){
				try {
					E current = list.take();
					for(Listener<E> listener : listeners)
						listener.event(current);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
	public Speaker() {
		speaker.start();
	}
	public void bind(Listener<E> listener){
		this.listeners.add(listener);
	}
	protected void event(E e){
		list.offer(e);
	}
}
