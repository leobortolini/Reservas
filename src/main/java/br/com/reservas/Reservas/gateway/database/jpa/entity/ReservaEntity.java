package br.com.reservas.Reservas.gateway.database.jpa.entity;

import br.com.reservas.Reservas.domain.Reserva;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="Reserva")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long restauranteId;
    private Integer quantidadeLugares;
    private String nomeCliente;
    private LocalDateTime inicioReserva;
    @Enumerated(EnumType.STRING)
    private Reserva.Status status;

    public static ReservaEntity toEntity(Reserva reserva) {
        return ReservaEntity.builder()
                .restauranteId(reserva.getRestauranteId())
                .nomeCliente(reserva.getNomeCliente())
                .inicioReserva(reserva.getInicioReserva())
                .quantidadeLugares(reserva.getQuantidadeLugares())
                .status(reserva.getStatus())
                .build();
    }
}
