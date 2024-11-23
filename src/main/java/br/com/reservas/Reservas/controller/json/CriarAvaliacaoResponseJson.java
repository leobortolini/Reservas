package br.com.reservas.Reservas.controller.json;

import br.com.reservas.Reservas.domain.Reserva;

public record CriarAvaliacaoResponseJson(Long id, Reserva reserva, String comentario, String satisfacao) {
}
