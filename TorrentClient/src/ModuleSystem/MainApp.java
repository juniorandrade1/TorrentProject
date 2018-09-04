package ModuleSystem;

import ModuleConnection.ConnectionController;
import ModuleConnection.ConnectionUtilities;
import ModuleServer.IServer;
import ModuleView.MainMenu;
import sun.applet.Main;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainApp {
    public static void main(String[]args) {
        SystemController.start();
    }
}
