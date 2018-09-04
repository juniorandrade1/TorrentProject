package ModuleSearch;

import ModuleFile.Part;

import java.util.LinkedList;
import java.util.Queue;

public class SearchQueue  extends Thread {
    private boolean finish;
    private Queue<Part> queue;

    public SearchQueue() {
        this.queue = new LinkedList<Part>();
        this.finish = false;
        this.start();
    }
    public void addToQueue(Part part) {
        synchronized (this.queue) {
            queue.add(part);
        }
    }
    public void finish() {
        this.finish = true;
    }
    @Override
    public void run() {
        while(!this.finish) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this.queue) {
                if(queue.size() == 0) continue;
                try {
                    Part part = queue.element();
                    queue.remove();
                    SearchController.getPart(part);
                }
                catch (Exception e) {
                    continue;
                }
            }
        }
    }
}
