package br.com.reservas.gateway.database.jpa.entity;

import br.com.reservas.domain.Avaliacao;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
@Getter
@Table(name="avaliacao")
public class AvaliacaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comentario;
    @Enumerated(EnumType.STRING)
    private Avaliacao.Satisfacao satisfacao;
    @OneToOne
    private ReservaEntity reservaEntity;
}
