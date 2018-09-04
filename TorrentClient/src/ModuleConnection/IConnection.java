package ModuleConnection;


import ModuleFile.Part;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IConnection extends Remote {
    public boolean isOn() throws RemoteException;
    public boolean hasPart(String id, int part) throws RemoteException;
    public Data getPart(Part part) throws RemoteException, IOException;
}
