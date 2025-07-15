package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando um recurso específico não é encontrado no sistema.
 * A anotação @ResponseStatus(HttpStatus.NOT_FOUND) instrui o Spring a retornar
 * automaticamente o status HTTP 404 quando esta exceção não é capturada por um
 * manipulador mais específico.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

