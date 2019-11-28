package servers;

import java.util.ArrayList;
import java.util.List;

public class Buffer {
	private String internalBuffer =" ";
	private List<Object> listenerList = new ArrayList<Object>();
	
	private  void fire() {
		
		listenerList.forEach(  (Object a)->{synchronized (a) {
			
		 a.notify();}  } );
	}
	public void add(String text) {
		synchronized (internalBuffer) {
			internalBuffer = internalBuffer.concat(text+"\n");
			
		}
		
		
		fire();
	}
	public String getString() {
		return internalBuffer;
	}
	public boolean isReady() {
		return !internalBuffer.isBlank();
	}
	public void register(Object c) {
		listenerList.add(c);
	}
	public void clearBuffer() {internalBuffer = " ";}
}