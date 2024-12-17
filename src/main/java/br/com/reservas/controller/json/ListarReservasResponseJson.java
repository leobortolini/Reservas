package br.com.reservas.controller.json;

import br.com.reservas.domain.Reserva;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ListarReservasResponseJson (List<Reserva> reservas){
}
