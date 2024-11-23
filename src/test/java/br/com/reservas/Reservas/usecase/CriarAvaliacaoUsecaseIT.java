package br.com.reservas.Reservas.usecase;

import br.com.reservas.Reservas.domain.Avaliacao;
import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.exception.AvaliacaoNaoPermitidaException;
import br.com.reservas.Reservas.exception.ReservaNaoEncontradaException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class CriarAvaliacaoUsecaseIT {
    @Autowired
    private CriarAvaliacaoUsecase listarReservasUsecase;

    @Test
    @Sql(scripts = {"/clean.sql", "/reservas.sql"})
    void deveCriarAvaliacaoComReservaValida() {
        // Arrange
        Reserva reserva = new Reserva(2L, null, null, null, null, Reserva.Status.INICIADA);
        Avaliacao avaliacao = new Avaliacao(null, reserva, "Comentario", Avaliacao.Satisfacao.PERFEITO);

        // Act
        avaliacao = listarReservasUsecase.criar(avaliacao);

        // Assert
        assertNotNull(avaliacao.getAvaliacaoId());
    }

    @Test
    @Sql(scripts = {"/clean.sql", "/reservas.sql"})
    void deveLancarExcecaoParaReservaComStatusInvalido() {
        // Arrange
        Reserva reserva = new Reserva(2L, null, null, null, null, Reserva.Status.CANCELADA);
        Avaliacao avaliacao = new Avaliacao(null, reserva, "Comentario", Avaliacao.Satisfacao.PERFEITO);

        // Act & Assert
        assertThrows(AvaliacaoNaoPermitidaException.class, () -> listarReservasUsecase.criar(avaliacao));
    }

    @Test
    @Sql(scripts = {"/clean.sql", "/reservas.sql"})
    void deveLancarExcecaoParaReservaInexistente() {
        // Arrange
        Reserva reserva = new Reserva(999L, null, null, null, null, Reserva.Status.INICIADA);
        Avaliacao avaliacao = new Avaliacao(null, reserva, "Comentario", Avaliacao.Satisfacao.PERFEITO);

        // Act & Assert
        assertThrows(ReservaNaoEncontradaException.class, () -> listarReservasUsecase.criar(avaliacao));
    }
}
