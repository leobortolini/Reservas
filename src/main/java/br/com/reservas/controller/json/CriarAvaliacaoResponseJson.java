package br.com.reservas.controller.json;

import br.com.reservas.domain.Reserva;

public record CriarAvaliacaoResponseJson(Long id, Reserva reserva, String comentario, String satisfacao) {
}
