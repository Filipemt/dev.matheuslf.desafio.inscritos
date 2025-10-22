package dev.matheuslf.desafio.inscritos.exceptions;

public class DuplicatedRegisterException extends RuntimeException {
    public DuplicatedRegisterException(String message) {
        super(message);
    }
}
