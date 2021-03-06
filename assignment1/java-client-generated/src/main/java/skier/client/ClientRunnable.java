package skier.client;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.ResortsApi;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientRunnable implements Runnable {
    ThreadData data;
    Counter task;
    SkiersApi skiersApi;
    ResortsApi resortsApi;
    Statistic stat;

    long timeStamp;

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
            beginStat();
            int respCode = doPost();
            endStat("POST", respCode);
        }

        for (int i = 0; i < data.getNumGet(); i++) {
            beginStat();
            int respCode = skierDayVert();
            endStat("GET skierDayVert", respCode);
        }

        if (data.getNumGet() == 10) { // if phase 3
            for (int i = 0; i < data.getNumGet(); i++) {
                beginStat();
                int respCode = skierResortTotal();
                endStat("GET skierResortTotal", respCode);
            }
        }

        task.dec();
    }

    int doPost() {
        int code = -1;
        LiftRide ride = new LiftRide();
        ride.setDayID("1");
        ride.setTime(data.genTime());
        ride.setSkierID(data.genSkiId());
        ride.setLiftID(data.genLiftId());
        ride.setResortID("Hyatt");
        try {
            ApiResponse response = skiersApi.writeNewLiftRideWithHttpInfo(ride);
            code = response.getStatusCode();
        } catch (ApiException e) {
            e.printStackTrace();
            return code;
        }
        return code;
    }

    int skierDayVert() {
        int code = -1;
        try {
            ApiResponse response = skiersApi.getSkierDayVerticalWithHttpInfo(data.getResortId(), "1", data.genSkiId());
            code = response.getStatusCode();
        } catch (ApiException e) {
            e.printStackTrace();
            return code;
        }
        return code;
    }

    int skierResortTotal() {
        int code = -1;
        try {
            List<String> resorts = new ArrayList<>();
            resorts.add(data.getResortId());
            ApiResponse response = skiersApi.getSkierResortTotalsWithHttpInfo(data.genSkiId(), resorts);
            code = response.getStatusCode();
        } catch (ApiException e) {
            e.printStackTrace();
            return code;
        }
        return code;
    }

    void beginStat() {
        if (stat.isIncludePart2()) {
            timeStamp = System.currentTimeMillis();
        }
    }

    void endStat(String cmd, int responseCode) {
        if (stat.isIncludePart2()) {
            long end = System.currentTimeMillis();
            RestfulCall call = new RestfulCall(cmd, responseCode, timeStamp, end);
            stat.offer(call);
        }
        if (responseCode == 200 || responseCode == 201) {
            stat.incSucceed();
        } else {
            stat.incFailed();
            Logger.getGlobal().log(Level.WARNING, "Failed Request, response code " + responseCode);
        }
    }
}