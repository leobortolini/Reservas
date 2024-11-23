package br.com.reservas.Reservas.exception;

import lombok.Getter;

@Getter
public class AvaliacaoNaoPermitidaException extends SystemBaseException {
  private final String code = "avaliacao.erroAoAvaliar";
  private final String message = "O status da reserva deve ser INICIADA ou COMPLETADA para poder ser avaliada";
  private final Integer httpStatus = 409;
}
