# language: pt
Funcionalidade: API - Mensagens

    Cenário: Criar nova reserva
        Quando criar nova reserva
        Então a reserva é criada com sucesso

    Cenário: Atualizar reserva
        Dado que uma reserva exista
        Quando requisitar atualização da reserva
        Então a reserva é atualizada com sucesso

    Cenário: Listar reserva
        Dado que mais de uma reserva exista
        Quando listar as reservas
        Então as reservas são listadas

    Cenário: Avaliar reserva
        Dado que uma reserva esteja iniciada
        Quando avaliar a reserva
        Então a reserva é avaliada com sucesso