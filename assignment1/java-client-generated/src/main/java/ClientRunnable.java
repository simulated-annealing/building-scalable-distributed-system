import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.ResortsApi;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import io.swagger.client.model.SkierVertical;

public class ClientRunnable implements Runnable{
    ThreadData data;
    Counter task;
    SkiersApi skiersApi;
    ResortsApi resortsApi;
    Statistic stat;

    public ClientRunnable(ThreadData data, Counter task, Statistic stat) {
        super();
        this.data = data;
        this.task = task;
        this.stat = stat;
        ApiClient apiClient = new ApiClient();
        skiersApi = new SkiersApi(apiClient);
        resortsApi = new ResortsApi(apiClient);
        apiClient.setBasePath(data.getAddress());
    }

    @Override
    public void run() {
        for (int i = 0; i < data.getNumPost(); i++) {
            doPost();
        }

        for (int i = 0; i < data.getNumGet(); i++) {
            doGet();
        }
        task.dec();
    }

    void doPost() {
        LiftRide ride = new LiftRide();
        ride.setDayID(data.genDay());
        ride.setSkierID(data.genSkiId());
        ride.setLiftID(data.genLiftId());
        long begin = System.currentTimeMillis();
        ApiResponse response = null;
        try {
            response = skiersApi.writeNewLiftRideWithHttpInfo(ride);
        } catch (ApiException e) {
            e.printStackTrace();
        } finally {
            long end = System.currentTimeMillis();
            RestfulCall call = new RestfulCall("POST", response != null && response.getStatusCode() == 201, begin, end);
            stat.offer(call);
        }
    }

    void doGet() {
        long begin = System.currentTimeMillis();
        ApiResponse<SkierVertical> response = null;
        try {
            response = skiersApi.getSkierDayVerticalWithHttpInfo(data.getResortId(), data.genDay(), data.genSkiId());
        } catch (ApiException e) {
            e.printStackTrace();
        } finally {
            long end = System.currentTimeMillis();
            RestfulCall call = new RestfulCall("GET", response != null && response.getStatusCode() == 200, begin, end);
            stat.offer(call);
        }
    }
}