package ModuleSystem;


import ModuleConnection.IConnection;
import ModuleServer.Server;
import ModuleServer.ServerController;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class MainApp {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting Modules");
        ServerController.start();
        while(true) {
            TimeUnit.SECONDS.sleep(5);
            LinkedList<String> needRemove = new LinkedList<String>();
            synchronized (ServerController.getClients()) {
                System.out.println("--------------------------------");
                for(String ip : ServerController.getClients()) {
                    System.out.println(ip);
                    try {
                        Registry registry = LocateRegistry.getRegistry(ip);
                        IConnection stub = (IConnection) registry.lookup("filetransfer");
                        if(!stub.isOn()) needRemove.add(ip);
                    } catch(Exception e) {
                        if(ip != null) needRemove.add(ip);
                    }
                }
                System.out.println("--------------------------------");
            }
            for(String ip : needRemove) ServerController.removeClient(ip);
        }
    }
}
