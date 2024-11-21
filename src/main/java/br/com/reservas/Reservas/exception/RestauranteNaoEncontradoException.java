package br.com.reservas.Reservas.exception;

import lombok.Getter;

@Getter
public class RestauranteNaoEncontradoException extends SystemBaseException {
    private final String code = "restaurante.naoEncontrado";
    private final String message = "O ID de restaurante nao foi encontrado";
    private final Integer httpStatus = 404;
}
