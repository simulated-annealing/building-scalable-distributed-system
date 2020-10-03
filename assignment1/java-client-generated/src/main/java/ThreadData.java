import java.util.Random;

public class ThreadData {
    int minSkierId;
    int maxSkierId;
    int minLiftId;
    int maxLiftId;
    int minDay;
    int maxDay;

    int numPost;
    int numGet;

    Random rand;

    public ThreadData(int minSkierId, int maxSkierId, int minLiftId, int maxLiftId, int minDay, int maxDay, int numPost, int numGet) {
        this.minSkierId = minSkierId;
        this.maxSkierId = maxSkierId;
        this.minLiftId = minLiftId;
        this.maxLiftId = maxLiftId;
        this.minDay = minDay;
        this.maxDay = maxDay;
        this.numPost = numPost;
        this.numGet = numGet;
        this.rand = new Random(1000);
    }

    public int genSkiId() {
        return range(minSkierId, maxSkierId);
    }

    public int genLiftId() {
        return range(minLiftId, maxLiftId);
    }

    public int genDay() {
        return range(minDay, maxDay);
    }

    public int getNumPost() {
        return numPost;
    }

    public int getNumGet() {
        return numGet;
    }

    int range(int lower, int upper) {
        return lower + rand.nextInt(upper-lower+1);
    }
}
