package ModuleConnection;

import java.io.Serializable;
import java.rmi.RemoteException;

@SuppressWarnings("serial")
public class Data implements Serializable {
    public String fileId;
    public int partId;
    public byte[] data;

    public Data() throws RemoteException {
        fileId = "";
        partId = -1;
        data = null;
    }
    public Data(String fileId, int partId, byte[] data) throws RemoteException {
        this();
        if(data != null) {
            this.fileId = fileId;
            this.partId = partId;
            this.data = data;
        }
    }
}
