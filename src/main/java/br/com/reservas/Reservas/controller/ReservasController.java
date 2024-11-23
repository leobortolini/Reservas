package br.com.reservas.Reservas.controller;

import br.com.reservas.Reservas.controller.json.*;
import br.com.reservas.Reservas.domain.Avaliacao;
import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.usecase.CriarAvaliacaoUsecase;
import br.com.reservas.Reservas.usecase.CriarReservaUsecase;
import br.com.reservas.Reservas.usecase.GerenciarReservasUsecase;
import br.com.reservas.Reservas.usecase.ListarReservasUsecase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/reservas")
@RequiredArgsConstructor
public class ReservasController {

    private final CriarReservaUsecase criarReservaUsecase;
    private final ListarReservasUsecase listarReservasUsecase;
    private final GerenciarReservasUsecase gerenciarReservasUsecase;
    private final CriarAvaliacaoUsecase criarAvaliacaoUsecase;

    @PostMapping
    public ResponseEntity<CriarReservaResponseJson> criarReserva(@Valid @RequestBody CriarReservaJson reservaJson) {
        Reserva reserva = criarReservaUsecase.criar(mapToDomain(reservaJson));

        return ResponseEntity.status(HttpStatus.CREATED).body(new CriarReservaResponseJson(reserva.getReservaId(), reserva.getRestauranteId(),
                reserva.getQuantidadeLugares(), reserva.getNomeCliente(), reserva.getInicioReserva(), reserva.getStatus().name()));
    }

    @PostMapping("/avaliar")
    public ResponseEntity<CriarAvaliacaoResponseJson> avaliarReserva(@Valid @RequestBody CriarAvaliacaoJson criarAvaliacaoJson) {
        Avaliacao avaliacao = criarAvaliacaoUsecase.criar(mapToDomain(criarAvaliacaoJson));

        return ResponseEntity.status(HttpStatus.CREATED).body(new CriarAvaliacaoResponseJson(avaliacao.getAvaliacaoId(), avaliacao.getReserva(),
                avaliacao.getComentario(), avaliacao.getSatisfacao().name()));
    }

    @GetMapping
    public ResponseEntity<ListarReservasResponseJson> listarReservas(@RequestParam Long restauranteId, @RequestParam String dataReserva) {
        List<Reserva> reservas = listarReservasUsecase.listarReservas(restauranteId, LocalDateTime.parse(dataReserva));

        return ResponseEntity.status(HttpStatus.OK).body(new ListarReservasResponseJson(reservas));
    }

    @PutMapping
    public ResponseEntity<AtualizarReservaResponseJson> atualizarReserva(@Valid @RequestBody AtualizarReservaJson atualizarReservaJson) {
        Reserva reserva = gerenciarReservasUsecase.atualizarReserva(mapToDomain(atualizarReservaJson));

        return ResponseEntity.status(HttpStatus.OK).body(new AtualizarReservaResponseJson(reserva));
    }

    private Avaliacao mapToDomain(CriarAvaliacaoJson criarAvaliacaoJson) {
        return new Avaliacao(null, listarReservasUsecase.listarReserva(criarAvaliacaoJson.getReservaId()), criarAvaliacaoJson.getComentario(), criarAvaliacaoJson.getSatisfacao());
    }

    private Reserva mapToDomain(CriarReservaJson criarReservaJson) {
        return new Reserva(null,
                criarReservaJson.getRestauranteId(),
                criarReservaJson.getQuantidadeLugares(),
                criarReservaJson.getNomeCliente(),
                criarReservaJson.getInicioReserva(),
                null);
    }

    private Reserva mapToDomain(AtualizarReservaJson criarReservaJson) {
        return new Reserva(criarReservaJson.getReservaId(),
                criarReservaJson.getRestauranteId(),
                criarReservaJson.getQuantidadeLugares(),
                criarReservaJson.getNomeCliente(),
                criarReservaJson.getInicioReserva(),
                criarReservaJson.getStatus());
    }
}
