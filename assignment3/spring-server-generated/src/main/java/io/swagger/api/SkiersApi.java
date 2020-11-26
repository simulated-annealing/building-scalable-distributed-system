/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.21).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.LiftRide;
import io.swagger.model.ResponseMsg;
import io.swagger.model.SkierVertical;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-10-24T02:33:34.409Z[GMT]")
@Api(value = "skiers", description = "the skiers API")
public interface SkiersApi {

    @ApiOperation(value = "", nickname = "getSkierDayVertical", notes = "get the total vertical for the skier for the specified ski day", response = SkierVertical.class, tags={ "skiers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation - total vertical for the day returned", response = SkierVertical.class),
        @ApiResponse(code = 400, message = "Invalid inputs supplied", response = ResponseMsg.class),
        @ApiResponse(code = 404, message = "Data not found", response = ResponseMsg.class) })
    @RequestMapping(value = "/skiers/{resortID}/days/{dayID}/skiers/{skierID}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<SkierVertical> getSkierDayVertical(@ApiParam(value = "ID of the resort the skier is at",required=true) @PathVariable("resortID") String resortID
,@DecimalMin("1") @DecimalMax("366") @ApiParam(value = "ID number of ski day in the ski season",required=true) @PathVariable("dayID") String dayID
,@ApiParam(value = "ID of the skier riding the lift",required=true) @PathVariable("skierID") String skierID
);


    @ApiOperation(value = "get the total vertical for the skier for the specified resort", nickname = "getSkierResortTotals", notes = "get the total vertical for the skier the specified resort.", response = SkierVertical.class, tags={ "skiers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = SkierVertical.class),
        @ApiResponse(code = 400, message = "Invalid inputs supplied", response = ResponseMsg.class),
        @ApiResponse(code = 404, message = "Data not found", response = ResponseMsg.class) })
    @RequestMapping(value = "/skiers/{skierID}/vertical",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<SkierVertical> getSkierResortTotals(@ApiParam(value = "ID the skier to retrieve data for",required=true) @PathVariable("skierID") String skierID
,@NotNull @ApiParam(value = "resort to filter by", required = true) @Valid @RequestParam(value = "resort", required = true) List<String> resort
);


    @ApiOperation(value = "write a new lift ride for the skier", nickname = "writeNewLiftRide", notes = "Stores new lift ride details in the data store", tags={ "skiers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Write successful"),
        @ApiResponse(code = 400, message = "Invalid inputs", response = ResponseMsg.class),
        @ApiResponse(code = 404, message = "Data not found", response = ResponseMsg.class) })
    @RequestMapping(value = "/skiers/liftrides",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Void> writeNewLiftRide(@ApiParam(value = "information for new lift ride event" ,required=true )  @Valid @RequestBody LiftRide body
);

}

