package io.swagger.api;

import com.haotang.server.database.Database;
import com.haotang.server.rabbitmq.RabbitMQConfig;
import io.swagger.model.LiftRide;
import io.swagger.model.SkierVertical;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-10-24T02:33:34.409Z[GMT]")
@Controller
public class SkiersApiController implements SkiersApi {

    private static final Logger log = LoggerFactory.getLogger(SkiersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final Database db;

    private final RabbitTemplate rabbitTemplate;

    @org.springframework.beans.factory.annotation.Autowired
    public SkiersApiController(ObjectMapper objectMapper, HttpServletRequest request, Database db, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.db = db;
        this.rabbitTemplate = rabbitTemplate;
    }

    public ResponseEntity<SkierVertical> getSkierDayVertical(@ApiParam(value = "ID of the resort the skier is at", required = true) @PathVariable("resortID") String resortID
            , @DecimalMin("1") @DecimalMax("366") @ApiParam(value = "ID number of ski day in the ski season", required = true) @PathVariable("dayID") String dayID
            , @ApiParam(value = "ID of the skier riding the lift", required = true) @PathVariable("skierID") String skierID ) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                int vertical = this.db.queryVertical(resortID, Integer.valueOf(dayID), Integer.valueOf(skierID));
                SkierVertical skierVertical = new SkierVertical().resortID(resortID).totalVert(vertical);
                return new ResponseEntity<SkierVertical>(skierVertical, HttpStatus.OK);
            } catch (Exception e) {
                log.error("Failed to get skier day vertical", e);
                return new ResponseEntity<SkierVertical>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<SkierVertical>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<SkierVertical> getSkierResortTotals(@ApiParam(value = "ID the skier to retrieve data for", required = true) @PathVariable("skierID") String skierID
            , @NotNull @ApiParam(value = "resort to filter by", required = true) @Valid @RequestParam(value = "resort", required = true) List<String> resort ) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                int vertical = 0;
                for(String resortId: resort) {
                    vertical += this.db.queryVertical(resortId, Integer.valueOf(skierID));
                }
                SkierVertical skierVertical = new SkierVertical().totalVert(vertical);
                return new ResponseEntity<SkierVertical>(skierVertical, HttpStatus.OK);
            } catch (Exception e) {
                log.error("Failed to get skier resort total vertical", e);
                return new ResponseEntity<SkierVertical>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<SkierVertical>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Void> writeNewLiftRide(@ApiParam(value = "information for new lift ride event", required = true) @Valid @RequestBody LiftRide body ) {
        String accept = request.getHeader("Accept");
        try {
            this.rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_ID, "post.", body);
        } catch (Exception e) {
            log.error("Failed to post new ride", e);
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}