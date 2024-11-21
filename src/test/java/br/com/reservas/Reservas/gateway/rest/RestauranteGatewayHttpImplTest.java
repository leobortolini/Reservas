package br.com.reservas.Reservas.gateway.rest;

import br.com.reservas.Reservas.exception.ErroAoConsultarRestauranteException;
import br.com.reservas.Reservas.gateway.rest.json.QuantidadeDeLugaresResponseJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestauranteGatewayHttpImplTest {

    @InjectMocks
    private RestauranteGatewayHttpImpl restauranteGatewayHttp;

    @Mock
    private RestTemplate restTemplate;

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
                "https://ff36ceeb-8f69-481c-a753-20b9dac467db.mock.pstmn.io/api/v1/disponibilidade?restauranteId=%s&dataReserva=%s",
                restauranteId,
                dataReserva
        );

        QuantidadeDeLugaresResponseJson responseJson = new QuantidadeDeLugaresResponseJson(restauranteId, quantidadeEsperada);
        ResponseEntity<QuantidadeDeLugaresResponseJson> responseEntity = new ResponseEntity<>(responseJson, HttpStatus.OK);

        when(restTemplate.getForEntity(urlEsperada, QuantidadeDeLugaresResponseJson.class)).thenReturn(responseEntity);

        // Act
        Long quantidadeDeLugares = restauranteGatewayHttp.quantidadeDeLugares(restauranteId, dataReserva);

        // Assert
        assertNotNull(quantidadeDeLugares);
        assertEquals(quantidadeEsperada, quantidadeDeLugares);
        verify(restTemplate, times(1)).getForEntity(urlEsperada, QuantidadeDeLugaresResponseJson.class);
    }

    @Test
    void quantidadeDeLugares_DeveLancarErroAoConsultarRestauranteException_QuandoRespostaForNula() {
        // Arrange
        Long restauranteId = 1L;
        LocalDateTime dataReserva = LocalDateTime.now();

        String urlEsperada = String.format(
                "https://ff36ceeb-8f69-481c-a753-20b9dac467db.mock.pstmn.io/api/v1/disponibilidade?restauranteId=%s&dataReserva=%s",
                restauranteId,
                dataReserva
        );

        ResponseEntity<QuantidadeDeLugaresResponseJson> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.getForEntity(urlEsperada, QuantidadeDeLugaresResponseJson.class)).thenReturn(responseEntity);

        // Act & Assert
        assertThrows(ErroAoConsultarRestauranteException.class, () -> restauranteGatewayHttp.quantidadeDeLugares(restauranteId, dataReserva));
        verify(restTemplate, times(1)).getForEntity(urlEsperada, QuantidadeDeLugaresResponseJson.class);
    }

    @Test
    void quantidadeDeLugares_DeveLancarErroAoConsultarRestauranteException_QuandoOcorreException() {
        // Arrange
        Long restauranteId = 1L;
        LocalDateTime dataReserva = LocalDateTime.now();

        String urlEsperada = String.format(
                "https://ff36ceeb-8f69-481c-a753-20b9dac467db.mock.pstmn.io/api/v1/disponibilidade?restauranteId=%s&dataReserva=%s",
                restauranteId,
                dataReserva
        );

        when(restTemplate.getForEntity(urlEsperada, QuantidadeDeLugaresResponseJson.class)).thenThrow(new RuntimeException("Erro de conexão"));

        // Act & Assert
        assertThrows(ErroAoConsultarRestauranteException.class, () -> restauranteGatewayHttp.quantidadeDeLugares(restauranteId, dataReserva));
        verify(restTemplate, times(1)).getForEntity(urlEsperada, QuantidadeDeLugaresResponseJson.class);
    }
}
