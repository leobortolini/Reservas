package br.com.reservas.Reservas.gateway.database.jpa;

import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.exception.ErroAoAcessarRepositorioException;
import br.com.reservas.Reservas.exception.ReservaNaoEncontradaException;
import br.com.reservas.Reservas.gateway.ReservaGateway;
import br.com.reservas.Reservas.gateway.database.jpa.entity.ReservaEntity;
import br.com.reservas.Reservas.gateway.database.jpa.repository.ReservaRepository;
import br.com.reservas.Reservas.gateway.dto.ReservaDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservaJpaGateway implements ReservaGateway {

    private final ReservaRepository reservaRepository;

    @Override
    public Long contarReservas(Long restauranteId, LocalDateTime data) {
        List<Reserva.Status> excludedStatuses = Arrays.asList(Reserva.Status.CANCELADA, Reserva.Status.COMPLETADA);

        return reservaRepository.somarQuantidadeLugares(restauranteId, data, excludedStatuses);
    }

    @Override
    public Reserva criar(Reserva reserva) {
        try {
            ReservaEntity reservaEntity = ReservaEntity.toEntity(reserva);

            reservaEntity = reservaRepository.save(reservaEntity);
            reserva.setReservaId(reservaEntity.getId());

            return reserva;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ErroAoAcessarRepositorioException();
        }
    }

    @Override
    public ReservaDTO atualizarReserva(Reserva reserva) {
        if (reservaRepository.findById(reserva.getReservaId()).isEmpty()) {
            throw new ReservaNaoEncontradaException();
        }
        ReservaEntity entity = ReservaEntity.toEntity(reserva);
        entity.setId(reserva.getReservaId());
        entity = reservaRepository.save(entity);

        return new ReservaDTO(entity.getId(), entity.getRestauranteId(), entity.getQuantidadeLugares(), entity.getNomeCliente(), entity.getInicioReserva(), entity.getStatus());
    }

    @Override
    public List<ReservaDTO> listarReservas(Long restauranteId, LocalDateTime data) {
        List<ReservaEntity> reservaEntityList = reservaRepository.findAllByRestauranteIdAndInicioReserva(restauranteId, data);

        return reservaEntityList.stream()
                .map(entity -> new ReservaDTO(entity.getId(), entity.getRestauranteId(), entity.getQuantidadeLugares(), entity.getNomeCliente(), entity.getInicioReserva(), entity.getStatus()))
                .toList();
    }

    @Override
    public ReservaDTO listarReserva(Long reservaId) {
        Optional<ReservaEntity> entity = reservaRepository.findById(reservaId);

        if (entity.isPresent()) {
            ReservaEntity reservaEntity = entity.get();
            return new ReservaDTO(reservaEntity.getId(), reservaEntity.getRestauranteId(), reservaEntity.getQuantidadeLugares(),
                    reservaEntity.getNomeCliente(), reservaEntity.getInicioReserva(), reservaEntity.getStatus());
        }

        return null;
    }
}
