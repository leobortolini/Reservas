package br.com.reservas.Reservas.exception;

import lombok.Getter;

@Getter
public class ReservaNaoEncontradaException extends SystemBaseException {
    private final String code = "reserva.naoEncontrada";
    private final String message = "Reserva nao encontrada";
    private final Integer httpStatus = 404;
}
