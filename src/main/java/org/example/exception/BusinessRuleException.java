package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando uma operação viola uma regra de negócio do sistema.
 * Exemplos: tentar criar uma entidade que já existe, ou excluir um recurso
 * que está em uso por outro.
 * A anotação @ResponseStatus(HttpStatus.BAD_REQUEST) instrui o Spring a retornar
 * automaticamente o status HTTP 400.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}

