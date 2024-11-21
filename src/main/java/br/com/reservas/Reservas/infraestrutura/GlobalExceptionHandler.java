package br.com.reservas.Reservas.infraestrutura;

import br.com.reservas.Reservas.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErroAoAcessarRepositorioException.class)
    public ResponseEntity<ErrorResponse> tratarErroDeAcessoAoRepositorio(ErroAoAcessarRepositorioException ex) {
        return ResponseEntity.status(HttpStatus.valueOf(ex.getHttpStatus())).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ErroAoConsultarRestauranteException.class)
    public ResponseEntity<ErrorResponse> tratarErroAoConsultarRestaurante(ErroAoConsultarRestauranteException ex) {
        return ResponseEntity.status(HttpStatus.valueOf(ex.getHttpStatus())).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(RestauranteNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> tratarRestauranteNaoEncontrado(RestauranteNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.valueOf(ex.getHttpStatus())).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(RestauranteSemLugaresDisponiveisException.class)
    public ResponseEntity<ErrorResponse> tratarLimiteDeLugaresRestaurante(RestauranteSemLugaresDisponiveisException ex) {
        return ResponseEntity.status(HttpStatus.valueOf(ex.getHttpStatus())).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ReservaNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> tratarReservaNaoEncontrada(ReservaNaoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.valueOf(ex.getHttpStatus())).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(AvaliacaoNaoPermitidaException.class)
    public ResponseEntity<ErrorResponse> tratarErroAoAvaliar(AvaliacaoNaoPermitidaException ex) {
        return ResponseEntity.status(HttpStatus.valueOf(ex.getHttpStatus())).body(new ErrorResponse(ex.getMessage()));
    }


    public record ErrorResponse(String message) { }
}
