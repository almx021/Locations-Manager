package com.mendes.desafio_tecnico_backend_nuven.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseError (
    LocalDateTime timestamp,
    Integer statusCode,
    String statusError,
    List<String> errors
) {
}