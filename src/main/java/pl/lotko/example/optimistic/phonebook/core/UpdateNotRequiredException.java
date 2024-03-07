package pl.lotko.example.optimistic.phonebook.core;

public class UpdateNotRequiredException extends RuntimeException {
    public UpdateNotRequiredException(String message) {
        super(message);
    }
}
