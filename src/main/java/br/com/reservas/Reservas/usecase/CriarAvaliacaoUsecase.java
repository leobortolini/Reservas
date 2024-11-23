package br.com.reservas.Reservas.usecase;

import br.com.reservas.Reservas.domain.Avaliacao;
import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.exception.AvaliacaoNaoPermitidaException;
import br.com.reservas.Reservas.gateway.AvaliacaoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static br.com.reservas.Reservas.domain.Reserva.Status.COMPLETADA;
import static br.com.reservas.Reservas.domain.Reserva.Status.INICIADA;

@Service
@RequiredArgsConstructor
public class CriarAvaliacaoUsecase {
    private static final List<Reserva.Status> statusPermitidosParaAvaliar = Arrays.asList(INICIADA, COMPLETADA);
    private final AvaliacaoGateway avaliacaoGateway;

    public Avaliacao criar(Avaliacao avaliacao) {
        if (!statusPermitidosParaAvaliar.contains(avaliacao.getReserva().getStatus())) {
            throw new AvaliacaoNaoPermitidaException();
        }

        return avaliacaoGateway.criar(avaliacao);
    }
}
