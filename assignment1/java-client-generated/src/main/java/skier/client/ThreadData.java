package skier.client;
import java.util.Random;

public class ThreadData {
    String resortId;
    String address;

    int minSkierId;
    int maxSkierId;
    int minLiftId;
    int maxLiftId;
    int minDay;
    int maxDay;

    int numPost;
    int numGet;

    Random rand;

    public ThreadData(int minSkierId, int maxSkierId, int minLiftId, int maxLiftId, int minDay, int maxDay, int numPost, int numGet, String resortId, String address) {
        this.minSkierId = minSkierId;
        this.maxSkierId = maxSkierId;
        this.minLiftId = minLiftId;
        this.maxLiftId = maxLiftId;
        this.minDay = minDay;
        this.maxDay = maxDay;
        this.numPost = numPost;
        this.numGet = numGet;
        this.resortId = resortId;
        this.address = address;
        this.rand = new Random(1000);
    }

    public String genSkiId() {
        return String.valueOf(range(minSkierId, maxSkierId));
    }

    public String genLiftId() {
        return String.valueOf(range(minLiftId, maxLiftId));
    }

    public String genDay() {
        return String.valueOf(range(minDay, maxDay));
    }

    public int getNumPost() {
        return numPost;
    }

    public int getNumGet() {
        return numGet;
    }

    public String getResortId() {
        return resortId;
    }

    public String getAddress() {
        return address;
    }

    int range(int lower, int upper) {
        return lower + rand.nextInt(upper-lower+1);
    }
}
