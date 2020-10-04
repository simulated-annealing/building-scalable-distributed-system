import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Statistic {
    Queue<RestfulCall> callQ;
    List<RestfulCall> gets;
    List<RestfulCall> posts;

    public Statistic() {
        callQ = new LinkedBlockingQueue<>();
        gets = new ArrayList<>();
        posts = new ArrayList<>();
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
        int succeed = 0;
        int failed = 0;
        long start = Long.MAX_VALUE;
        long end = 0;

        for (RestfulCall call: posts) {
            if (call.isSuccess()) succeed++;
            else failed++;
            start = Math.min(start, call.getBegin());
            end = Math.max(end, call.getEnd());
        }
        for (RestfulCall call: gets) {
            if (call.isSuccess()) succeed++;
            else failed++;
            start = Math.min(start, call.getBegin());
            end = Math.max(end, call.getEnd());
        }
        long wallTime = end-start;

        System.out.println("client stat part 1");
        System.out.println("number of successful requests: " + succeed);
        System.out.println("number of unsuccessful requests: " + failed);
        System.out.println("total run time: " + wallTime );
        System.out.println("throughput: " + succeed/wallTime);
    }

    public void printStats2() {
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

        System.out.println("client stat part 2");
        System.out.println("mean response time for POST: " + postTime/posts.size());
        System.out.println("mean response time for GET: " + getTime/gets.size());
        System.out.println("median response time for POST: " + (posts.get(posts.size()/2).getEnd()-posts.get(posts.size()/2).getBegin()));
        System.out.println("median response time for GET: " + (gets.get(gets.size()/2).getEnd()-gets.get(gets.size()/2).getBegin()));
        System.out.println("total wall time: " + (postTime + getTime));
        System.out.println("throughput: " + (posts.size()+gets.size())/(postTime+getTime));
        System.out.println("p99 response time for POST: ");
        System.out.println("p99 response time for GET: ");
        System.out.println("max response time for POST: " + mxReqTime);
        System.out.println("max response time for GET: " + mxGetTime);
    }
}
