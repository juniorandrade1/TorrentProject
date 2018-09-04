package ModuleFile;

import ModuleView.MainMenu;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Scanner;

public class FileClass {

    private String name;
    private String path;
    private String id;
    private int size;
    private int numParts;
    private boolean[] partsDownloaded;
    private String[] hashParts;
    public static final int PART_SIZE = 5000;

    public FileClass(String file) throws Exception {
        Path path = Paths.get(file);
        this.name = path.getFileName().toString();
        this.path = path.toString();
        this.id = createSha1(new File(file));
        File f = new File(file);
        this.size = (int)f.length();
        this.numParts = (this.size + PART_SIZE - 1) / PART_SIZE;
        this.partsDownloaded = new boolean[this.numParts];
        this.hashParts = new String[this.numParts];
        RandomAccessFile raf = new RandomAccessFile(this.path, "rw");
        for(int i = 0; i < this.partsDownloaded.length; ++i) {
            this.partsDownloaded[i] = true;
            byte[] b;
            if(this.size % PART_SIZE == 0 || i != this.numParts - 1) {
                b = new byte[PART_SIZE];
            } else {
                b = new byte[this.size % PART_SIZE];
            }
            raf.read(b);
            this.hashParts[i] = SHAsum(b);
        }
        raf.close();
    }

    public FileClass(String name, String path, String id, int size, int numParts, String hashParts[], boolean[] partsDownloaded) throws IOException {
        this.name = name;
        this.path = path;
        this.id = id;
        this.size = size;
        this.numParts = numParts;
        this.hashParts = hashParts;
        this.partsDownloaded = partsDownloaded;

        this.hashParts = hashParts;
    }

    public FileClass(String name, String path, String id, int size, String[] hashParts) throws IOException {
        this.name = name;
        this.path = path;
        this.id = id;
        this.size = size;
        this.numParts = size / PART_SIZE;
        if(size % PART_SIZE != 0) this.numParts++;
        this.partsDownloaded = new boolean[this.numParts];
        this.hashParts = hashParts;
        this.createData();
    }

    public void createData() throws IOException {
        RandomAccessFile f = new RandomAccessFile(this.path, "rw");
        f.write(new byte[this.size]);
        f.close();
    }

    static public String createSha1(File file) throws Exception  {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        InputStream in = new FileInputStream(file);
        int n = 0;
        byte[] buffer = new byte[8192];
        while (n != -1) {
            n = in.read(buffer);
            if (n > 0) {
                digest.update(buffer, 0, n);
            }
        }
        in.close();
        return byteToHex(digest.digest());
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String SHAsum(byte[] convertme) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return byteArray2Hex(md.digest(convertme));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String s = formatter.toString();
        formatter.close();
        return s;
    }

    public void setData(byte[] data, int partId) throws IOException {
        RandomAccessFile f = new RandomAccessFile(this.path, "rw");
        f.seek((long)partId * PART_SIZE);
        f.write(data);
        f.close();
        partsDownloaded[partId] = true;
        MainMenu.refreshTable();
    }

    public byte[] getData(int partId) {
        if(partsDownloaded[partId] == false) return null;
        byte[] data = null;
        if(this.size % PART_SIZE == 0 || partId != this.numParts - 1) {
            data = new byte[PART_SIZE];
        } else {
            data = new byte[this.size % PART_SIZE];
        }
        try {
            RandomAccessFile f = new RandomAccessFile(this.path, "r");
            f.seek(partId * PART_SIZE);
            f.read(data);
            f.close();
        } catch (Exception e) {
            data = null;
        }
        return data;
    }

    public boolean hasPart(int partId) {
        return this.partsDownloaded[partId];
    }

    public String getHashPart(int partId) {
        return this.hashParts[partId];
    }

    public double getPercentage() {
        double p = 0;
        for(int i = 0; i < this.numParts; ++i) {
            if(partsDownloaded[i] == true) p += 1.;
        }
        p = (p * 100. / this.numParts);
        return p;
    }

    public void createInfoFile(String path) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter out = new PrintWriter(new File(path), "UTF-8");
        String info = this.name + "\n" + this.id + "\n" + this.size + " " + this.numParts;
        for(int i = 0; i < this.numParts; ++i) {
            info += " " + hashParts[i];
        }
        out.write(info);
        out.close();
    }

    public String getInfo() {
        String info;
        info = this.name + "\n" + this.path + "\n" + this.id + "\n" + this.size + " " + this.numParts;
        for(int i = 0; i < this.numParts; ++i) {
            info += " " + hashParts[i];
        }
        info += "\n";
        return info;
    }

    public String getId() { return this.id; }
    public String getName(){ return this.name; }
    public String getPath(){ return this.path; }
    public int getSize(){ return this.size; }
    public boolean[] getPartsDownloaded() {
        return this.partsDownloaded;
    }

    static public FileClass readInfoFile(String pathInfo, String pathFile) {
        try {
            Scanner sc = new Scanner(new File(pathInfo), "UTF-8");
            String name = sc.nextLine();
            String id = sc.nextLine();
            int size = sc.nextInt();
            int numParts = sc.nextInt();
            String[] hashParts = new String[numParts];
            for(int i = 0; i < numParts; ++i) {
                hashParts[i] = sc.next();
            }
            sc.close();
            return new FileClass(name, pathFile, id, size, hashParts);
        } catch (Exception e) {
            return null;
        }
    }

}
