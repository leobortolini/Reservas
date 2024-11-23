package br.com.reservas.Reservas.gateway.rest;

import br.com.reservas.Reservas.exception.ErroAoConsultarRestauranteException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@EnableWireMock({@ConfigureWireMock(port = 8080, httpsPort = 0)})
class RestauranteGatewayHttpImplIT{

    @Autowired
    private RestauranteGatewayHttpImpl restauranteGateway;

    @Test
    void deveRetornarQuantidadeDeLugares() {
        // Arrange
        Long restauranteId = 1L;
        LocalDateTime dataReserva = LocalDateTime.of(2024, 11, 24, 12, 0);
        String url = String.format("/api/v1/disponibilidade?restauranteId=%s&dataReserva=%s", restauranteId, dataReserva);

        stubFor(get(urlEqualTo(url))
                .willReturn(okJson("{\n" +
                        "    \"restauranteId\": 1,\n" +
                        "    \"quantidaDePessoas\": 50\n" +
                        "}")));

        // Act
        Long quantidadeLugares = restauranteGateway.quantidadeDeLugares(restauranteId, dataReserva);

        // Assert
        assertNotNull(quantidadeLugares);
        assertEquals(50L, quantidadeLugares);

        verify(getRequestedFor(urlEqualTo(url)));
    }

    @Test
    void deveLancarErroAoConsultarRestauranteQuandoRetornoForNulo() {
        // Arrange
        Long restauranteId = 2L;
        LocalDateTime dataReserva = LocalDateTime.of(2024, 11, 24, 12, 0);
        String url = String.format("/api/v1/disponibilidade?restauranteId=%s&dataReserva=%s", restauranteId, dataReserva);

        stubFor(get(urlEqualTo(url))
                .willReturn(okJson("{}"))); // Simula retorno sem o campo esperado

        // Act & Assert
        assertThrows(ErroAoConsultarRestauranteException.class,
                () -> restauranteGateway.quantidadeDeLugares(restauranteId, dataReserva));

        verify(getRequestedFor(urlEqualTo(url)));
    }

    @Test
    void deveLancarErroAoConsultarRestauranteQuandoServicoRetornaErro() {
        // Arrange
        Long restauranteId = 3L;
        LocalDateTime dataReserva = LocalDateTime.of(2024, 11, 24, 12, 0);
        String url = String.format("/api/v1/disponibilidade?restauranteId=%s&dataReserva=%s", restauranteId, dataReserva);

        stubFor(get(urlEqualTo(url))
                .willReturn(serverError())); // Simula erro no serviço externo

        // Act & Assert
        assertThrows(ErroAoConsultarRestauranteException.class,
                () -> restauranteGateway.quantidadeDeLugares(restauranteId, dataReserva));

        verify(getRequestedFor(urlEqualTo(url)));
    }
}
