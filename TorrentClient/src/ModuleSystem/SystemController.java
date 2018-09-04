package ModuleSystem;
import ModuleConnection.ConnectionController;
import ModuleConnection.ConnectionUtilities;
import ModuleFile.FileController;
import ModuleSearch.SearchController;
import ModuleServer.IServer;
import ModuleView.MainMenu;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SystemController {
    public static void start() {
        ConnectionController.init();
        try {
            Registry registry = LocateRegistry.getRegistry(Settings.SERVER_IP);
            IServer stub = (IServer) registry.lookup("filetransferserver");
            String ip = ConnectionUtilities.getLocalAddress().toString().substring(1);
            stub.initConnectionWithClient(ip);
        } catch(Exception e) {
            System.out.println("Não foi possível conectar ao servidor!");
            e.printStackTrace();
        }
        MainMenu.init();
    }
    public static void finish() throws IOException{
        ConnectionController.finish();
        SearchController.finish();
    }
}
