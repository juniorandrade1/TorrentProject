package ModuleFile;

import java.io.Serializable;
import java.rmi.RemoteException;

@SuppressWarnings("serial")
public class Part implements Serializable {

    private String fileId;
    private int partId;
    private String hash;

    public Part(String fileId, int partId, String hash) throws RemoteException {
        this.fileId = fileId;
        this.partId = partId;
        this.hash = hash;
    }

    public String getFileId() {
        return this.fileId;
    }

    public int getPartId() {
        return this.partId;
    }

    public String getHashPart() {
        return this.hash;
    }
}
