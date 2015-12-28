import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class BootstrapServer {
	public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException, NotBoundException {
		Start.startBootstrapServer();
	}
}
