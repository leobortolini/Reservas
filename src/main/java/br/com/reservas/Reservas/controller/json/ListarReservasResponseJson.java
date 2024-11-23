package br.com.reservas.Reservas.controller.json;

import br.com.reservas.Reservas.domain.Reserva;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ListarReservasResponseJson (List<Reserva> reservas){
}
