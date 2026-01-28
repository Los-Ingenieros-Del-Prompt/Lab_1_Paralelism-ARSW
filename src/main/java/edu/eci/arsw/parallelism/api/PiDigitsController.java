
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
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/pi")
@Validated
@Tag(
        name = "Pi Digits",
        description = "API for calculating digits of π using a sequential algorithm"
)

public class PiDigitsController {

    private final PiDigitsService service;

    public PiDigitsController(PiDigitsService service) {
        this.service = service;
    }
    @Operation(
            summary = "Get digits of π",
            description = """
            Returns a sequence of π digits calculated deterministically
            using a sequential algorithm, starting from a given position.
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
                    description = "Invalid parameters (start < 0 or count < 1)",
                    content = @Content
            )
    })
    @GetMapping("/digits")
    public PiResponse digits(
            @Parameter(
                    description = "Starting position (0-based) for the π digits",
                    example = "0"
            )
            @RequestParam @Min(0) int start,

            @Parameter(
                    description = "Number of digits to calculate",
                    example = "10"
            )
            @RequestParam @Min(1) int count
    )

    {
        String digits = service.calculateSequential(start, count);
        return new PiResponse(start, count, digits);
    }
}
