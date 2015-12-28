package Structures;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;

public class Address implements Serializable{
	
	private static final long serialVersionUID = -6837348722711348293L;
	private String host, processName;
	private int port;
	public Address(String host, int port, String processName){
		this.host=host;
		this.port=port;
		this.processName=processName;
	}
	public String getHost() {
		return host;
	}
	public int getPort() {
		return port;
	}
	public String getProcessName() {
		return processName;
	}
	public String print(){
		return(processName+"@"+host+":"+port);
	}
	private static Address initialAddress=new Address("localhost", 9000, "server");
	public static Address getInitialAddress() {
		return initialAddress;
	}
	public static int freePort() {
		int freePort=0;
		ServerSocket socket;
		try {
			socket = new ServerSocket(0);
			freePort=socket.getLocalPort();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return freePort;		
	}
}