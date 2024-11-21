package br.com.reservas.Reservas.controller.json;

import br.com.reservas.Reservas.domain.Reserva;

import java.util.List;


public record ListarReservasResponseJson (List<Reserva> reservas){
}
