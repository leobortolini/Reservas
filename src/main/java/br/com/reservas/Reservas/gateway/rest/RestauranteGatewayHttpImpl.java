package br.com.reservas.Reservas.gateway.rest;

import br.com.reservas.Reservas.exception.ErroAoConsultarRestauranteException;
import br.com.reservas.Reservas.gateway.RestauranteGateway;
import br.com.reservas.Reservas.gateway.rest.json.QuantidadeDeLugaresResponseJson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Slf4j
public class RestauranteGatewayHttpImpl implements RestauranteGateway {

    private final RestTemplate restTemplate;

    @Value("${restaurante.gateway.url}")
    private String gatewayURL;

    @Override
    public Long quantidadeDeLugares(Long restauranteId, LocalDateTime dataReserva) {
        try {
            String url = String.format(gatewayURL + "/api/v1/disponibilidade?restauranteId=%s&dataReserva=%s", restauranteId, dataReserva.toString());

            ResponseEntity<QuantidadeDeLugaresResponseJson> response = restTemplate.getForEntity(url, QuantidadeDeLugaresResponseJson.class);
            if (response.getBody() == null || response.getBody().quantidaDePessoas() == null || response.getBody().quantidaDePessoas() == null) throw new ErroAoConsultarRestauranteException();

            return response.getBody().quantidaDePessoas();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ErroAoConsultarRestauranteException();
        }
    }
}
