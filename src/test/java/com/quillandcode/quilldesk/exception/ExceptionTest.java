package com.quillandcode.quilldesk.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ExceptionTest {

    @Test
    void badRequestExceptionStoresMessage() {
        String message = "Invalid request payload";

        BadRequestException exception = new BadRequestException(message);

        assertEquals(message, exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void resourceNotFoundExceptionStoresMessage() {
        String message = "Resource not found";

        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        assertEquals(message, exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }
}
