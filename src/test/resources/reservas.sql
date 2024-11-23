-- Inserir uma reserva
INSERT INTO Reserva (id, restaurante_id, quantidade_lugares, nome_cliente, inicio_reserva, status)
VALUES
  (1, 1, 4, 'João Silva', '2024-11-21 19:00:00', 'PENDENTE'),
  (2, 2, 2, 'Maria Oliveira', '2024-11-22 20:00:00', 'INICIADA'),
  (3, 3, 6, 'Carlos Souza', '2024-11-23 18:30:00', 'CANCELADA'),
  (4, 3, 6, 'Carlos Souza 2', '2024-11-23 18:30:00', 'PENDENTE');
