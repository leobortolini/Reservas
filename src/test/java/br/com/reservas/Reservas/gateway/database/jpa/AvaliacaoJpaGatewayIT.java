package br.com.reservas.Reservas.gateway.database.jpa;

import br.com.reservas.Reservas.domain.Avaliacao;
import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.exception.ReservaNaoEncontradaException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class AvaliacaoJpaGatewayIT {

    @Autowired
    private AvaliacaoJpaGateway avaliacaoJpaGateway;

    @Test
    @Sql(scripts = {"/clean.sql", "/reservas.sql"})
    void deveCriarAvaliacao() {
        // Arrange
        Reserva reserva = new Reserva();
        reserva.setReservaId(1L);
        Avaliacao avaliacao =  new Avaliacao(null, reserva, "Comentario", Avaliacao.Satisfacao.PERFEITO);

        // Act
        avaliacao = avaliacaoJpaGateway.criar(avaliacao);

        // Assert
        assertNotNull(avaliacao.getAvaliacaoId());
    }

    @Test
    @Sql(scripts = {"/clean.sql"})
    void deveLancarExcecaoAoCriarAvaliacaoComReservaInexistente() {
        // Arrange
        Reserva reserva = new Reserva();
        reserva.setReservaId(1L);
        Avaliacao avaliacao =  new Avaliacao(null, reserva, "Comentario", Avaliacao.Satisfacao.PERFEITO);

        // Act & Assert
        assertThrows(ReservaNaoEncontradaException.class, () -> avaliacaoJpaGateway.criar(avaliacao));
    }
}
