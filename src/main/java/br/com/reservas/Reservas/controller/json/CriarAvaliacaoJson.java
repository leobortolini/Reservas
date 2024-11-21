package br.com.reservas.Reservas.controller.json;

import br.com.reservas.Reservas.domain.Avaliacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CriarAvaliacaoJson {
    @NotNull
    private Long reservaId;
    @NotNull
    private Avaliacao.Satisfacao satisfacao;
    @Size(min = 10, max = 255, message = "O comentário deve ter entre 10 e 255 caracteres")
    private String comentario;
}
