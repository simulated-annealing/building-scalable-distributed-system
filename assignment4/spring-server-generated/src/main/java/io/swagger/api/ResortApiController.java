package io.swagger.api;

import io.swagger.model.TopTen;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-10-24T02:33:34.409Z[GMT]")
@Controller
public class ResortApiController implements ResortApi {

    private static final Logger log = LoggerFactory.getLogger(ResortApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ResortApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<TopTen> getTopTenVert(@NotNull @ApiParam(value = "resort to query by", required = true) @Valid @RequestParam(value = "resort", required = true) List<String> resort
,@NotNull @ApiParam(value = "day number in the season", required = true) @Valid @RequestParam(value = "dayID", required = true) List<String> dayID
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<TopTen>(objectMapper.readValue("{\n  \"topTenSkiers\" : [ {\n    \"skierID\" : \"888899\",\n    \"VertcialTotal\" : 30400\n  }, {\n    \"skierID\" : \"888899\",\n    \"VertcialTotal\" : 30400\n  } ]\n}", TopTen.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<TopTen>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<TopTen>(HttpStatus.NOT_IMPLEMENTED);
    }

}
