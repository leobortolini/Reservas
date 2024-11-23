package br.com.reservas.Reservas.usecase;

import br.com.reservas.Reservas.domain.Avaliacao;
import br.com.reservas.Reservas.domain.Reserva;
import br.com.reservas.Reservas.exception.AvaliacaoNaoPermitidaException;
import br.com.reservas.Reservas.gateway.AvaliacaoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CriarAvaliacaoUsecaseTest {

    @InjectMocks
    private CriarAvaliacaoUsecase criarAvaliacaoUsecase;

    @Mock
    private AvaliacaoGateway avaliacaoGateway;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criar_DeveRetornarIdAvaliacao_QuandoStatusPermitido() {
        // Arrange
        Reserva reserva = new Reserva();
        reserva.setStatus(Reserva.Status.COMPLETADA);
        Avaliacao avaliacao = new Avaliacao(null, reserva, "Ótimo atendimento", Avaliacao.Satisfacao.PERFEITO);
        Avaliacao avaliacaoComId = new Avaliacao();
        avaliacaoComId.setAvaliacaoId(1L);
        when(avaliacaoGateway.criar(avaliacao)).thenReturn(avaliacaoComId);

        // Act
        Avaliacao novaAvaliacao = criarAvaliacaoUsecase.criar(avaliacao);

        // Assert
        assertNotNull(novaAvaliacao);
        assertEquals(1L, novaAvaliacao.getAvaliacaoId());
        verify(avaliacaoGateway, times(1)).criar(avaliacao);
    }

    @Test
    void criar_DeveLancarAvaliacaoNaoPermitidaException_QuandoStatusNaoPermitido() {
        // Arrange
        Reserva reserva = new Reserva();
        reserva.setStatus(Reserva.Status.PENDENTE); // Status não permitido
        Avaliacao avaliacao = new Avaliacao(null, reserva, "Ótimo atendimento", Avaliacao.Satisfacao.PERFEITO);

        // Act & Assert
        assertThrows(AvaliacaoNaoPermitidaException.class, () -> criarAvaliacaoUsecase.criar(avaliacao));
        verify(avaliacaoGateway, never()).criar(any());
    }
}
