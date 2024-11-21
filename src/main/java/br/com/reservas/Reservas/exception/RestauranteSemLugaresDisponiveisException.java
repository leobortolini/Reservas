package br.com.reservas.Reservas.exception;

import lombok.Getter;

@Getter
public class RestauranteSemLugaresDisponiveisException extends SystemBaseException {
  private final String code = "restaurante.lugaresDisponiveis";
  private final String message = "Restaurante sem capacidade para a quantidade de pessoas informada";
  private final Integer httpStatus = 409;
}
