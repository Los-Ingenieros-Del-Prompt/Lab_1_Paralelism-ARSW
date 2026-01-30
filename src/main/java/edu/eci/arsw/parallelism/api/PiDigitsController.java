package edu.eci.arsw.parallelism.api;

import edu.eci.arsw.parallelism.core.PiDigitsService;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import edu.eci.arsw.parallelism.monitoring.PiExecutionResult;


@RestController
@RequestMapping("/api/v1/pi")
@Validated
@Tag(
        name = "Pi Digits",
        description = "API for calculating digits of π using sequential or parallel strategies"
)
public class PiDigitsController {

    private final PiDigitsService service;

    public PiDigitsController(PiDigitsService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get digits of π",
            description = """
            Returns a sequence of π digits calculated deterministically,
            using a selectable execution strategy.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "π digits successfully calculated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PiResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters",
                    content = @Content
            )
    })
    @GetMapping("/digits")
    public PiResponse digits(

            @Parameter(description = "Starting position (0-based)", example = "0")
            @RequestParam @Min(0) int start,

            @Parameter(description = "Number of digits to calculate", example = "10")
            @RequestParam @Min(1) int count,

            @Parameter(description = "Number of threads to use", example = "4")
            @RequestParam(required = false) @Min(0) Integer threads,

            @Parameter(
                    description = "Execution strategy: sequential or threads",
                    example = "threads"
            )
            @RequestParam(required = false) String strategy
    ) {
        String digits = service.calculate(
                start,
                count,
                threads,
                strategy
        );

        return new PiResponse(start, count, digits);
    }


    @Operation(
            summary = "Measure execution time for π digit calculation",
            description = """
        Executes the π digit calculation and returns execution time metrics
        for performance comparison between strategies.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Execution completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PiExecutionResult.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters",
                    content = @Content
            )
    })
    @GetMapping("/digits/measure")
    public PiExecutionResult measure(

            @RequestParam @Min(0) int start,
            @RequestParam @Min(1) int count,
            @RequestParam(required = false) @Min(0) Integer threads,
            @RequestParam(required = false) String strategy
    ) {
        return service.calculateWithTiming(start, count, threads, strategy);
    }


}
