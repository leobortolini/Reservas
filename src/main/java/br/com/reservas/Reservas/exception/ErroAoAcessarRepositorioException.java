package br.com.reservas.Reservas.exception;

import lombok.Getter;

@Getter
public class ErroAoAcessarRepositorioException extends SystemBaseException {

    private final String code = "usuario.erroAcessarRepositorio";
    private final String message = "Erro ao acessar repositorio de dados.";
    private final Integer httpStatus = 500;
}
