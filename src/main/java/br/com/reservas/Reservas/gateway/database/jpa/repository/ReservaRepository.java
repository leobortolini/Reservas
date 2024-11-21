package br.com.reservas.Reservas.gateway.database.jpa.repository;

import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.gateway.database.jpa.entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {

    @Query("SELECT SUM(r.quantidadeLugares) FROM ReservaEntity r WHERE r.restauranteId = :restauranteId AND r.inicioReserva = :inicioReserva AND r.status NOT IN :excludedStatuses")
    Long somarQuantidadeLugares(@Param("restauranteId") Long restauranteId,
                                @Param("inicioReserva") LocalDateTime inicioReserva,
                                @Param("excludedStatuses") List<Reserva.Status> excludedStatuses);

    List<ReservaEntity> findAllByRestauranteIdAndInicioReserva(Long restauranteId, LocalDateTime inicioReserva);

}
