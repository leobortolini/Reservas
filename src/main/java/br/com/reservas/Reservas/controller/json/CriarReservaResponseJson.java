package br.com.reservas.Reservas.controller.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CriarReservaResponseJson(Long id, Long restauranteId, Integer quantidadeLugares, String nomeCliente, LocalDateTime inicioReserva,
                                       String status){ }
