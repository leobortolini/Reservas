package br.com.reservas.usecase;

import br.com.reservas.domain.Avaliacao;
import br.com.reservas.domain.Reserva;
import br.com.reservas.exception.AvaliacaoNaoPermitidaException;
import br.com.reservas.gateway.AvaliacaoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static br.com.reservas.domain.Reserva.Status.COMPLETADA;
import static br.com.reservas.domain.Reserva.Status.INICIADA;

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
