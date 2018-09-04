package ModuleSearch;

import ModuleConnection.ConnectionUtilities;
import ModuleConnection.Data;
import ModuleConnection.IConnection;
import ModuleFile.FileController;
import ModuleFile.Part;
import ModuleSearch.SearchQueue;
import ModuleServer.IServer;
import ModuleSystem.Settings;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class SearchController {

    public static final SearchQueue searchQueue = new SearchQueue();


    public static void getPart(Part part) {
        Search s = new Search(part);
        s.start();
    }

    public static Data getPartFromPeer(String ip, Part part) {
        try {
            Registry registry = LocateRegistry.getRegistry(ip);
            IConnection stub = (IConnection) registry.lookup("filetransfer");
            return stub.getPart(part);
        } catch(Exception e) {
            System.out.println("Não foi possível conectar ao Peer!");
            e.printStackTrace();
            return null;
        }
    }

    public static String getRamdomClient(Part part){
        try {
            Registry registry = LocateRegistry.getRegistry(Settings.SERVER_IP);
            IServer stub = (IServer) registry.lookup("filetransferserver");
            String ip = stub.askForAPart(part.getFileId(), part.getPartId());
            return ip;
        } catch(Exception e) {
            System.out.println("Não foi possível conectar ao Servidor!");
            e.printStackTrace();
            return null;
        }
    }

    public static void finish() {
        searchQueue.finish();
    }
}