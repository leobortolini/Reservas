package br.com.reservas.Reservas.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Avaliacao {
    private Long avaliacaoId;
    private Reserva reserva;
    private String comentario;
    private Satisfacao satisfacao;

    public enum Satisfacao {
        MUITO_INSATISFEITO,
        INSATISFEITO,
        SATISFEITO,
        MUITO_SATISFEITO,
        PERFEITO;
    }
}
