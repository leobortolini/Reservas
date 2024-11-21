-- Inserir reservas
INSERT INTO Reserva (restauranteId, quantidadeLugares, nomeCliente, inicioReserva, status)
VALUES
  (1, 4, 'João Silva', '2024-11-21 19:00:00', 'PENDENTE'),
  (2, 2, 'Maria Oliveira', '2024-11-22 20:00:00', 'INICIADA'),
  (3, 6, 'Carlos Souza', '2024-11-23 18:30:00', 'COMPLETADA');

-- Inserir avaliações para as reservas
INSERT INTO Avaliacao (comentario, satisfacao, reservaEntity_id)
VALUES
  ('Ótimo atendimento, recomendo!', 'PERFEITO', 1),
  ('Comida boa, mas o ambiente estava muito barulhento', 'SATISFEITO', 2),
  ('Não gostei do serviço, demorado e desorganizado', 'MUITO_INSATISFEITO', 3);
