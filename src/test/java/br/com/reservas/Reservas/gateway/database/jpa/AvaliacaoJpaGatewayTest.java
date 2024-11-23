package br.com.reservas.Reservas.gateway.database.jpa;

import br.com.reservas.Reservas.domain.Avaliacao;
import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.exception.ReservaNaoEncontradaException;
import br.com.reservas.Reservas.gateway.database.jpa.entity.AvaliacaoEntity;
import br.com.reservas.Reservas.gateway.database.jpa.entity.ReservaEntity;
import br.com.reservas.Reservas.gateway.database.jpa.repository.AvaliacaoRepository;
import br.com.reservas.Reservas.gateway.database.jpa.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvaliacaoJpaGatewayTest {

    @InjectMocks
    private AvaliacaoJpaGateway avaliacaoJpaGateway;

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @Mock
    private ReservaRepository reservaRepository;

    private Avaliacao avaliacao;
    private Reserva reserva;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup para o teste
        reserva = new Reserva();
        reserva.setReservaId(1L);
        avaliacao = new Avaliacao(null, reserva, "Excelente", Avaliacao.Satisfacao.PERFEITO);
    }

    @Test
    void criar_DeveRetornarIdDaAvaliacao() {
        // Arrange
        ReservaEntity reservaEntity = new ReservaEntity();

        AvaliacaoEntity avaliacaoEntity = AvaliacaoEntity.builder()
                .id(1L)
                .reservaEntity(reservaEntity)
                .comentario(avaliacao.getComentario())
                .satisfacao(avaliacao.getSatisfacao())
                .build();

        when(reservaRepository.findById(avaliacao.getReserva().getReservaId())).thenReturn(Optional.of(reservaEntity));
        when(avaliacaoRepository.save(any(AvaliacaoEntity.class))).thenReturn(avaliacaoEntity);

        // Act
        Avaliacao avaliacaoComId = avaliacaoJpaGateway.criar(avaliacao);

        // Assert
        assertNotNull(avaliacaoComId);
        assertEquals(avaliacaoEntity.getId(), avaliacaoComId.getAvaliacaoId());
        verify(reservaRepository, times(1)).findById(avaliacao.getReserva().getReservaId());
        verify(avaliacaoRepository, times(1)).save(any(AvaliacaoEntity.class));
    }

    @Test
    void criar_DeveLancarReservaNaoEncontradaException_QuandoReservaNaoExistir() {
        // Arrange
        when(reservaRepository.findById(avaliacao.getReserva().getReservaId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ReservaNaoEncontradaException.class, () -> avaliacaoJpaGateway.criar(avaliacao));
        verify(reservaRepository, times(1)).findById(avaliacao.getReserva().getReservaId());
    }

}
