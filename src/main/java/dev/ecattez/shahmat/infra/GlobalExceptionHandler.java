package fr.leroymerlin.configurators.rollershutter.configuration.infra;

import fr.leroymerlin.configurators.rollershutter.configuration.domain.ConfigurationException;
import fr.leroymerlin.configurators.rollershutter.feasibility.domain.FeasibilityException;
import fr.leroymerlin.configurators.rollershutter.configuration.infra.event_sourcing.SequenceAlreadyExists;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(SequenceAlreadyExists.class)
    public ResponseEntity<?> handleSequenceAlreadyExists(SequenceAlreadyExists e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        Problem
                                .create()
                                .withTitle(e.getClass().getSimpleName())
                                .withType(
                                        UriComponentsBuilder
                                                .fromUriString(request.getRequestURL().toString())
                                                .build()
                                                .toUri()
                                )
                                .withDetail(e.getMessage())
                                .withStatus(HttpStatus.CONFLICT)
                );
    }

    @ExceptionHandler(ConfigurationException.class)
    public ResponseEntity<?> handleConfigurationException(ConfigurationException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Problem
                                .create()
                                .withTitle(e.getClass().getSimpleName())
                                .withType(
                                        UriComponentsBuilder
                                                .fromUriString(request.getRequestURL().toString())
                                                .build()
                                                .toUri()
                                )
                                .withDetail(e.getMessage())
                                .withStatus(HttpStatus.BAD_REQUEST)
                );
    }

    @ExceptionHandler(FeasibilityException.class)
    public ResponseEntity<?> handleFeasibilityException(FeasibilityException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Problem
                                .create()
                                .withTitle(e.getClass().getSimpleName())
                                .withType(
                                        UriComponentsBuilder
                                                .fromUriString(request.getRequestURL().toString())
                                                .build()
                                                .toUri()
                                )
                                .withDetail(e.getMessage())
                                .withStatus(HttpStatus.BAD_REQUEST)
                );
    }
}
