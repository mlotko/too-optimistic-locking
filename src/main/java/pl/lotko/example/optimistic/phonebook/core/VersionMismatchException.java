package pl.lotko.example.optimistic.phonebook.core;

public class VersionMismatchException extends RuntimeException {
    public VersionMismatchException(String message) {
        super(message);
    }
}
