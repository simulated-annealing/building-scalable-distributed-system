import java.util.concurrent.*;


public class ClientMain {
    ExecutorService pool;
    int maxSkier;
    int maxLifts;

    ClientMain(int maxThread, int maxSkier, int maxLifts) {
        this.pool = Executors.newFixedThreadPool(maxThread);
        this.maxSkier = maxSkier;
        this.maxLifts = maxLifts;
    }

    Counter submitTasks(int minDay, int maxDay, int numPost, int numGet, int numThread) {
        int chunk = maxSkier/numThread;
        Counter task = new Counter(numThread);
        for (int i = 0; i < numThread; i++) {
            ThreadData data = new ThreadData(i*chunk+1, (i+1)*chunk, 1, maxLifts, minDay, maxDay, numPost, numGet);
            pool.submit(new ClientRunnable(data, task));
        }
        return task;
    }

    void shutDown() {
        pool.shutdown();
    }

    public static void main(String[] args) {
        int maxThread = 256;
        int maxSkier = 50000;
        int maxLifts = 40;
        int maxDay = 1;
        String resortId = "SilverMt";
        String address = "";

        ClientMain client = new ClientMain(maxThread, maxSkier, maxLifts);

        // phase 1
        Counter task1 = client.submitTasks(1, 90, 100, 5, maxThread/4);
        while (task1.get() > maxThread/36);

        // phase 2
        Counter task2 = client.submitTasks(91, 360, 100, 5, maxThread);
        while (task2.get() > maxThread/9);

        // phase 3
        Counter task3 = client.submitTasks(361, 420, 100, 10, maxThread);
        while(task1.get() > 0 || task2.get() > 0 || task3.get() > 0);

        client.shutDown();
    }
}