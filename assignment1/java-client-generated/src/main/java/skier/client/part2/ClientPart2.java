package skier.client.part2;

import skier.client.Client;
import skier.client.Counter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ClientPart2 {

    public static Properties readConfigFile(String configPath) {
        Properties config = new Properties();
        try (FileInputStream fs = new FileInputStream(configPath)) {
            config.load(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public static void main(String[] args) {
        if (args.length == 0 && false) {
            System.out.println("Error, pass the config file path as the first parameter.");
            return;
        }
        String configPath = "/Users/jihongliu/IdeaProjects/building-scalable-distributed-system/assignment1/java-client-generated/src/main/resources/config.properties";
        //String configPath = args[0];
        Properties config = readConfigFile(configPath);

        int maxThread = Integer.parseInt(config.getProperty("maxThread"));
        int maxSkier = Integer.parseInt(config.getProperty("maxSkier"));
        int maxLifts = Integer.parseInt(config.getProperty("maxLifts"));
        String resortId = config.getProperty("resortId");
        String address = config.getProperty("address");

        Client client = new Client(maxThread, maxSkier, maxLifts, resortId, address, true);
        long begin = System.currentTimeMillis();

        // phase 1
        Counter task1 = client.submitTasks(1, 90, 1000, 5, maxThread / 4);
        while (task1.get() > maxThread * 9 / 40) ;

        // phase 2
        Counter task2 = client.submitTasks(91, 360, 1000, 5, maxThread);
        while (task2.get() > maxThread * 9 / 10) ;

        // phase 3
        Counter task3 = client.submitTasks(361, 420, 1000, 10, maxThread / 4);
        while (task1.get() > 0 || task2.get() > 0 || task3.get() > 0) ;

        long end = System.currentTimeMillis();
        client.getStat().setWallTime(end - begin);
        client.getStat().dump();
        client.getStat().writeToCSV();
        client.getStat().printStats1();
        client.getStat().printStats2();

        client.shutDown();
    }
}
