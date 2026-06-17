package com.tall.GestionUnchk.exception;

/**
 * Exception levée en cas d'authentification invalide
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
