package ModuleConnection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IConnection extends Remote {
    public boolean isOn() throws RemoteException;
    public boolean hasPart(String id, int part) throws RemoteException;
}

