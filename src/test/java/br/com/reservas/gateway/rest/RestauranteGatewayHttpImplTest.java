package br.com.reservas.gateway.rest;

import br.com.reservas.exception.ErroAoConsultarRestauranteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestauranteGatewayHttpImplTest {

    @InjectMocks
    private RestauranteGatewayHttpImpl restauranteGatewayHttp;

    @Mock
    private RestTemplate restTemplate;

    @Value("${restaurante.gateway.url}")
    private String restauranteGatewayUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void quantidadeDeLugares_DeveRetornarQuantidade_QuandoRespostaForSucesso() {
        // Arrange
        Long restauranteId = 1L;
        LocalDateTime dataReserva = LocalDateTime.now();
        Long quantidadeEsperada = 50L;

        String urlEsperada = String.format(
                "%s/api/v1/restaurantes/disponibilidade?restauranteId=%s&dataReserva=%s",
                restauranteGatewayUrl,
                restauranteId,
                dataReserva
        );

        when(restTemplate.getForEntity(urlEsperada, Integer.class)).thenReturn(ResponseEntity.of(Optional.of(50)));

        // Act
        Long quantidadeDeLugares = restauranteGatewayHttp.quantidadeDeLugares(restauranteId, dataReserva);

        // Assert
        assertNotNull(quantidadeDeLugares);
        assertEquals(quantidadeEsperada, quantidadeDeLugares);
        verify(restTemplate, times(1)).getForEntity(urlEsperada, Integer.class);
    }

    @Test
    void quantidadeDeLugares_DeveLancarErroAoConsultarRestauranteException_QuandoRespostaForNula() {
        // Arrange
        Long restauranteId = 1L;
        LocalDateTime dataReserva = LocalDateTime.now();

        String urlEsperada = String.format(
                "%s/api/v1/restaurantes/disponibilidade?restauranteId=%s&dataReserva=%s",
                restauranteGatewayUrl,
                restauranteId,
                dataReserva
        );

        when(restTemplate.getForEntity(urlEsperada, Integer.class)).thenReturn(ResponseEntity.of(Optional.empty()));

        // Act & Assert
        assertThrows(ErroAoConsultarRestauranteException.class, () -> restauranteGatewayHttp.quantidadeDeLugares(restauranteId, dataReserva));
        verify(restTemplate, times(1)).getForEntity(urlEsperada, Integer.class);
    }

    @Test
    void quantidadeDeLugares_DeveLancarErroAoConsultarRestauranteException_QuandoOcorreException() {
        // Arrange
        Long restauranteId = 1L;
        LocalDateTime dataReserva = LocalDateTime.now();

        String urlEsperada = String.format(
                "%s/api/v1/restaurantes/disponibilidade?restauranteId=%s&dataReserva=%s",
                restauranteGatewayUrl,
                restauranteId,
                dataReserva
        );

        when(restTemplate.getForEntity(urlEsperada, Integer.class)).thenThrow(new RuntimeException("Erro de conexão"));

        // Act & Assert
        assertThrows(ErroAoConsultarRestauranteException.class, () -> restauranteGatewayHttp.quantidadeDeLugares(restauranteId, dataReserva));
        verify(restTemplate, times(1)).getForEntity(urlEsperada, Integer.class);
    }
}
