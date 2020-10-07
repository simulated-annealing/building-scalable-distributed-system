import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Statistic {
    Queue<RestfulCall> callQ;
    List<RestfulCall> gets;
    List<RestfulCall> posts;
    boolean includePart2;
    long wallTime;
    long succeed;
    long failed;

    public Statistic(boolean includePart2) {
        this.callQ = new LinkedBlockingQueue<>();
        this.gets = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.includePart2 = includePart2;
        this.wallTime = 0;
        this.succeed = 0;
        this.failed = 0;
    }

    public boolean isIncludePart2() {
        return includePart2;
    }

    public void setWallTime(long wallTime) {
        this.wallTime = wallTime;
    }

    public synchronized void incSucceed() {
        succeed++;
    }

    public synchronized void incFailed() {
        failed++;
    }

    public boolean offer(RestfulCall call) {
        return callQ.offer(call);
    }

    public void dump() {
        while (!callQ.isEmpty()) {
            RestfulCall call = callQ.poll();
            if (call.getCommand().equals("POST")) {
                posts.add(call);
            } else {
                gets.add(call);
            }
        }
        Collections.sort(posts);
        Collections.sort(gets);
    }

    public void printStats1() {
        System.out.println("client stat part 1");
        System.out.println("number of successful requests: " + succeed);
        System.out.println("number of unsuccessful requests: " + failed);
        System.out.println("total run time: " + wallTime + " ms");
        System.out.println("throughput: " + succeed*1000/wallTime + " req/sec");
    }

    public void printStats2() {
        if (!includePart2) return;
        long getTime = 0;
        long postTime = 0;
        long mxReqTime = 0;
        long mxGetTime = 0;

        for (RestfulCall call: posts) {
            long dur = call.getEnd()-call.getBegin();
            mxReqTime = Math.max(mxReqTime, dur);
            postTime += dur;
        }
        for (RestfulCall call: gets) {
            long dur = call.getEnd()-call.getBegin();
            mxGetTime = Math.max(mxGetTime, dur);
            getTime += dur;
        }

        RestfulCall p = null;

        System.out.println("client stat part 2");
        System.out.println("mean response time for POST: " + postTime/posts.size() + " ms");
        System.out.println("mean response time for GET: " + getTime/gets.size() + " ms");
        p = posts.get(posts.size()/2);
        System.out.println("median response time for POST: " + (p.getEnd()-p.getBegin()) + " ms");
        p = gets.get(gets.size()/2);
        System.out.println("median response time for GET: " + (p.getEnd()-p.getBegin()) + " ms");
        System.out.println("total wall time: " + wallTime + " ms");
        System.out.println("throughput: " + 1000*succeed/wallTime + " req/sec");
        p = posts.get(Math.min(posts.size() - 1, posts.size()*99/100));
        System.out.println("p99 response time for POST: " + (p.getEnd()-p.getBegin()) + " ms");
        p = gets.get(Math.min(gets.size() - 1, gets.size()*99/100));
        System.out.println("p99 response time for GET: " + (p.getEnd()-p.getBegin()) + " ms");
        System.out.println("max response time for POST: " + mxReqTime + " ms");
        System.out.println("max response time for GET: " + mxGetTime + " ms");
    }
}
