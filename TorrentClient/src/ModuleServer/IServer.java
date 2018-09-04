package ModuleServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote {
    public void initConnectionWithClient(String ip) throws RemoteException;
    public String askForAPart(String id, int part) throws RemoteException;
}
