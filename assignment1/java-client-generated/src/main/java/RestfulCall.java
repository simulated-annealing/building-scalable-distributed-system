public class RestfulCall implements Comparable<RestfulCall> {
    String command;
    int code; // response code
    long begin;
    long end;

    public RestfulCall(String command, int code, long begin, long end) {
        this.command = command;
        this.code = code;
        this.begin = begin;
        this.end = end;
    }

    public String getCommand() {
        return command;
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
