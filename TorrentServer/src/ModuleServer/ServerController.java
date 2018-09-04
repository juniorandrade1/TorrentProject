package ModuleServer;


import ModuleConnection.ConnectionUtilities;
import ModuleConnection.IConnection;
import ModuleServer.IServer;
import ModuleServer.Server;

import java.lang.reflect.Array;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

public class ServerController {

    static private Server obj;
    static private IServer stub;
    static private String ip;
    static private Registry registry;

    static public void createServer() {
        try {
            ip = ConnectionUtilities.getLocalAddress().toString().substring(1);
            System.out.println("Create server at ip = " + ip);
            System.setProperty("java.rmi.server.hostname", ip);

            obj = new Server();
            stub = (IServer) UnicastRemoteObject.exportObject(obj, 1099);

            LocateRegistry.createRegistry(1099);
            registry = LocateRegistry.getRegistry();
            registry.bind("filetransferserver", stub);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    static private final TreeSet<String> client = new TreeSet<String>();

    public static TreeSet<String> getClients() {
        synchronized (client) {
            return client;
        }
    }

    public static void addClient(String ip) {
        synchronized (client) {
            client.add(ip);
        }

    }

    public static void removeClient(String ip) {
        synchronized (client) {
            client.remove(ip);
        }
    }

    public static void start() {
        System.out.println("Starting Controller");
        createServer();
    }

    public static String askForAPart(String id, int part) {
        ArrayList< String > hasPart = new ArrayList< String >();
        synchronized (client) {
            for (String ip : client) {
                System.out.println(ip);
                try {
                    Registry registry = LocateRegistry.getRegistry(ip);
                    IConnection stub = (IConnection) registry.lookup("filetransfer");
                    if (stub.hasPart(id, part)) hasPart.add(ip);
                } catch (Exception e) {
                }
            }
            if (hasPart.size() == 0) return null;
            Random r = new Random();
            return hasPart.get(r.nextInt(hasPart.size()));
        }
    }

}
