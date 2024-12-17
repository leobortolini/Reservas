package br.com.reservas.gateway.database.jpa.repository;

import br.com.reservas.gateway.database.jpa.entity.AvaliacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvaliacaoRepository extends JpaRepository<AvaliacaoEntity, Long> {
}
