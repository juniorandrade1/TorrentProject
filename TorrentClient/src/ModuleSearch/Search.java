package  ModuleSearch;

import ModuleConnection.ConnectionController;
import ModuleConnection.Data;
import ModuleFile.FileClass;
import ModuleFile.FileController;
import ModuleFile.Part;

public class Search extends Thread {
    private Part part;

    public Search(Part part) {
        this.part = part;
    }

    @Override
    public void run() {
        String ip = SearchController.getRamdomClient(part);
        if(ip == null) {
            SearchController.searchQueue.addToQueue(part);
            return;
        }
        byte[] data = null;
        Data d = null;
        d = SearchController.getPartFromPeer(ip, part);
        if(d != null) {
            data = d.data;
        }
        if(data != null && d.partId != -1 && FileClass.SHAsum(data).compareTo(part.getHashPart()) == 0) {
            FileClass file = FileController.getFile(part.getFileId());
            try {
                file.setData(data, part.getPartId());
            } catch(Exception e) {
                addToQueue(part);
            }
        } else {
            addToQueue(part);
        }
    }

    private void addToQueue(Part part) {
        boolean ok = false;
        while(ok == false) {
            try {
                SearchController.searchQueue.addToQueue(part);
                ok = true;
            } catch (Exception e) {
                ok = false;
            }
        }
    }
}
