package ModuleConnection;

public class ConnectionController {
    static private final Connection connection = new Connection();
    public static void init() { connection.init(); }

    public static void finish() {
        connection.finish();
    }


}
