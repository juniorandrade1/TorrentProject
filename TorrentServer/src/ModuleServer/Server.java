package ModuleServer;

import java.rmi.RemoteException;

public class Server implements IServer {
    @Override
    public void initConnectionWithClient(String ip) throws RemoteException {
        ServerController.addClient(ip);
    }
    @Override
    public String askForAPart(String id, int part) throws RemoteException {
        System.out.println("ASKING PART = " + id);
        return ServerController.askForAPart(id, part);
    }
}
