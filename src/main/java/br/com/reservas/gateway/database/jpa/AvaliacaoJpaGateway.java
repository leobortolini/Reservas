package br.com.reservas.gateway.database.jpa;

import br.com.reservas.domain.Avaliacao;
import br.com.reservas.exception.ReservaNaoEncontradaException;
import br.com.reservas.gateway.AvaliacaoGateway;
import br.com.reservas.gateway.database.jpa.entity.AvaliacaoEntity;
import br.com.reservas.gateway.database.jpa.entity.ReservaEntity;
import br.com.reservas.gateway.database.jpa.repository.AvaliacaoRepository;
import br.com.reservas.gateway.database.jpa.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AvaliacaoJpaGateway implements AvaliacaoGateway {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ReservaRepository reservaRepository;

    @Override
    public Avaliacao criar(Avaliacao avaliacao) {
        AvaliacaoEntity avaliacaoEntity = toEntity(avaliacao);

        avaliacaoEntity = avaliacaoRepository.save(avaliacaoEntity);
        avaliacao.setAvaliacaoId(avaliacaoEntity.getId());

        return avaliacao;
    }

    private AvaliacaoEntity toEntity(Avaliacao avaliacao) {
        Optional<ReservaEntity> reservaEntity = reservaRepository.findById(avaliacao.getReserva().getReservaId());

        if (reservaEntity.isPresent()) {
            return AvaliacaoEntity.builder().reservaEntity(reservaEntity.get()).comentario(avaliacao.getComentario()).satisfacao(avaliacao.getSatisfacao()).build();
        }

        throw new ReservaNaoEncontradaException();
    }
}
