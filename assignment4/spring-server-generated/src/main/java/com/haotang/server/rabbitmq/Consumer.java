package com.haotang.server.rabbitmq;

import com.haotang.server.database.Database;
import io.swagger.model.LiftRide;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class Consumer {

    @Autowired
    private Database db;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ID, containerFactory = "jsaFactory")
    public void ridePostHandler(@RequestParam LiftRide body) {
        String resortId = body.getResortID();
        Integer dayId = Integer.valueOf(body.getDayID());
        Integer skierId = Integer.valueOf(body.getSkierID());
        Integer lyftId = Integer.valueOf(body.getLiftID());
        Integer time = Integer.valueOf(body.getTime());
        this.db.insertToSkierTable(resortId, dayId, skierId, lyftId, time);
    }

}
