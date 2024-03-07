package pl.lotko.example.optimistic.phonebook.api;

public record Validable<T, E>(
        T validator,
        E validated
) {
}
