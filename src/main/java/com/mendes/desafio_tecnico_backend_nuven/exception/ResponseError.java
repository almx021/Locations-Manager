package com.mendes.desafio_tecnico_backend_nuven.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Record that is used to model an error response sent to client when an exception is thrown.
 */
public record ResponseError (
    LocalDateTime timestamp,
    Integer statusCode,
    String statusError,
    List<String> errors
) {
}