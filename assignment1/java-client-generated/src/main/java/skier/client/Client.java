package skier.client;

import java.util.concurrent.*;


public class Client {
    ExecutorService pool;
    int maxSkier;
    int maxLifts;
    String resortId;
    String address;
    Statistic stat;

    public Client(int maxThread, int maxSkier, int maxLifts, String resortId, String address, boolean includePart2) {
        this.pool = Executors.newFixedThreadPool(maxThread);
        this.maxSkier = maxSkier;
        this.maxLifts = maxLifts;
        this.resortId = resortId;
        this.address = address;
        this.stat = new Statistic(includePart2);
    }

    public Statistic getStat() {
        return stat;
    }

    public Counter submitTasks(int minTime, int maxTime, int numPost, int numGet, int numThread) {
        int chunk = maxSkier / numThread;
        Counter task = new Counter(numThread);
        for (int i = 0; i < numThread; i++) {
            ThreadData data = new ThreadData(i * chunk + 1, (i + 1) * chunk, 1, maxLifts, minTime, maxTime, numPost, numGet, resortId, address);
            pool.submit(new ClientRunnable(data, task, stat));
        }
        return task;
    }

    public void shutDown() {
        pool.shutdown();
    }

}