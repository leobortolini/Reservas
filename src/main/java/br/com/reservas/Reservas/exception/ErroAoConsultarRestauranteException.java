package br.com.reservas.Reservas.exception;

import lombok.Getter;

@Getter
public class ErroAoConsultarRestauranteException extends SystemBaseException {
    private final String code = "restaurante.erroConsultaAPI";
    private final String message = "Erro ao chamar API do restaurante";
    private final Integer httpStatus = 500;
}
