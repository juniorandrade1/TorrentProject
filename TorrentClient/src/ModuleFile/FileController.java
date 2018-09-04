package ModuleFile;

import ModuleSearch.SearchController;
import ModuleSearch.SearchQueue;

import java.rmi.RemoteException;
import java.util.HashMap;

public class FileController {
    static private final HashMap<String, FileClass> files = new HashMap<String, FileClass>();
    public static void addFile(String path) throws Exception{
        FileClass file = new FileClass(path);
        files.put(file.getId(), file);
        System.out.println(file.getInfo());
    }
    public static boolean hasPart(String id, int part) {
        FileClass file = files.get(id);
        if(file == null) return false;
        return  file.hasPart(part);
    }
    public static HashMap<String, FileClass> getFiles() { return files; }
    public static byte[] getPart(Part part) {
        FileClass file = files.get(part.getFileId());
        return file.getData(part.getPartId());
    }
    public static void addFileFromInfo(FileClass file) {
        files.put(file.getId(), file);
    }
    public static void addInfoFile(String pathInfo, String pathFile) throws RemoteException {
        FileClass file = FileClass.readInfoFile(pathInfo, pathFile);
        addFileFromInfo(file);
        for(int i = 0; i < file.getPartsDownloaded().length; ++i) {
            if(!file.getPartsDownloaded()[i]) {
                SearchController.searchQueue.addToQueue(new Part(file.getId(), i, file.getHashPart(i)));
            }
        }
    }
    static public FileClass getFile(String id) {
        return files.get(id);
    }
}
