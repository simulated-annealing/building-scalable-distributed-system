public class RestfulCall implements Comparable<RestfulCall> {
    String command;
    boolean success;
    long begin;
    long end;

    public RestfulCall(String command, boolean success, long begin, long end) {
        this.command = command;
        this.success = success;
        this.begin = begin;
        this.end = end;
    }

    public String getCommand() {
        return command;
    }

    public boolean isSuccess() {
        return success;
    }

    public long getBegin() {
        return begin;
    }

    public long getEnd() {
        return end;
    }

    @Override
    public int compareTo(RestfulCall call) {
        long dur0 = end - begin;
        long dur1 = call.end - call.begin;
        if (dur0 > dur1) return 1;
        if (dur0 < dur1) return -1;
        return 0;
    }
}
