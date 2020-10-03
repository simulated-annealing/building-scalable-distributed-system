import io.swagger.client.ApiClient;


public class ClientRunnable implements Runnable{
    ThreadData data;
    Counter task;
    ApiClient apiClient;

    public ClientRunnable(ThreadData data, Counter task) {
        super();
        this.data = data;
        this.task = task;
        this.apiClient = new ApiClient();
        this.apiClient.setBasePath("0.0.0.0");
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
        int skiId = data.genSkiId();
        int lyftId = data.genLiftId();
        int day = data.genDay();
    }

    void doGet() {

    }
}