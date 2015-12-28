import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Peer {
	public static void main(String[] args) throws AccessException, RemoteException, UnknownHostException, AlreadyBoundException, NotBoundException {
		Start.startPeer();
	}
}
