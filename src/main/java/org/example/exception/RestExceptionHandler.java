package org.example.exception;

import org.example.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Manipulador de exceções global para a API REST.
 * A anotação @ControllerAdvice permite que esta classe intercepte exceções
 * lançadas por qualquer @Controller ou @RestController na aplicação.
 * Isso centraliza o tratamento de erros, mantendo os controllers limpos e
 * garantindo que as respostas de erro da API sejam consistentes.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Manipula a exceção ResourceNotFoundException.
     * É acionado sempre que a exceção é lançada em qualquer parte da aplicação.
     * @param ex A exceção capturada.
     * @return Uma ResponseEntity contendo um corpo de erro padronizado e o status HTTP 404 Not Found.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Manipula a exceção BusinessRuleException.
     * É acionado sempre que uma regra de negócio é violada.
     * @param ex A exceção capturada.
     * @return Uma ResponseEntity contendo um corpo de erro padronizado e o status HTTP 400 Bad Request.
     */
    @ExceptionHandler(BusinessRuleException.class)
    public final ResponseEntity<ErrorResponseDTO> handleBusinessRuleException(BusinessRuleException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

