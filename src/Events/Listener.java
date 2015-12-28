package Events;

import java.util.concurrent.LinkedBlockingQueue;

public class Listener<E> {
	private LinkedBlockingQueue<E> es=new LinkedBlockingQueue<E>();
	public Listener(Speaker<E> speaker) {
		speaker.bind(this);
	}
	public void event(E e){
		es.offer(e);
	}
	public E take() throws InterruptedException{
		return es.take();
	}
	public E poll(){
		return es.poll();
	}
}
