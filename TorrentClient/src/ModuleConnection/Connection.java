package ModuleConnection;

import ModuleFile.FileController;
import ModuleFile.Part;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Connection implements IConnection {

    private Connection obj;
    private IConnection stub;
    private String ip;
    private Registry registry;

    public void init() {
        try {
            ip = ConnectionUtilities.getLocalAddress().toString().substring(1);
            System.setProperty("java.rmi.server.hostname", ip);

            obj = new Connection();
            stub = (IConnection) UnicastRemoteObject.exportObject(obj, 1098);

            LocateRegistry.createRegistry(1098);
            registry = LocateRegistry.getRegistry();
            registry.bind("filetransfer", stub);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }


    @Override
    public boolean isOn() {
        return true;
    }

    public void finish() {
        try {
            registry.unbind("filetransfer");
            UnicastRemoteObject.unexportObject(obj, true);
        } catch (Exception e) {

        }
    }

    @Override
    public boolean hasPart(String id, int part) {
        return FileController.hasPart(id, part);
    }

    @Override
    public Data getPart(Part part) throws RemoteException, IOException {
        byte[] data = null;
        Data d = null;

        data = FileController.getPart(part);
        if(data != null) {
            try {
                d = new Data(part.getFileId(), part.getPartId(), data);
            } catch (Exception e) {
                return null;
            }
        }
        return d;
    }
}
