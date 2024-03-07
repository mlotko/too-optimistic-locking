package pl.lotko.example.optimistic.phonebook.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lotko.example.optimistic.phonebook.core.PhoneBookPersistService;
import pl.lotko.example.optimistic.phonebook.core.VersionMismatchException;

import java.util.Optional;

import static java.util.function.Predicate.not;

@RestController
@RequestMapping("/api/phone-book/item")
@RequiredArgsConstructor
class PhoneBookItemController {
    private final PhoneBookPersistService phoneBookPersistService;

    @PostMapping
    ResponseEntity<PhoneBookItemResponseDto> create(
            @RequestBody PhoneBookItemRequestDto newPhoneBookItemDto
    ) {
        final Validable<Long, PhoneBookItemResponseDto> saved
                = phoneBookPersistService.saveNew(newPhoneBookItemDto);
        return splitToBodyAndEtag(saved, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    ResponseEntity<PhoneBookItemResponseDto> get(
            @PathVariable Long id,
            @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false, defaultValue = "*") String etag
    ) {
        return parseEtagToVersion(etag)
                .flatMap(requestedVersion -> phoneBookPersistService.getSavedIfNotVersion(id, requestedVersion))
                .or(() -> phoneBookPersistService.getSaved(id))
                .map(got -> splitToBodyAndEtag(got, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    ResponseEntity<PhoneBookItemResponseDto> replace(
            @PathVariable Long id,
            @RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String etag,
            @RequestBody PhoneBookItemRequestDto newPhoneBookItemDto
    ) {
        if (etag == null || etag.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.PRECONDITION_REQUIRED)
                    .header(HttpHeaders.EXPECT)
                    .build();
        }
        return parseEtagToVersion(etag)
                .map(requestedVersion -> phoneBookPersistService.replaceExisting(id, new Validable<>(requestedVersion, newPhoneBookItemDto))
                        .map(replaced -> splitToBodyAndEtag(replaced, HttpStatus.OK))
                        .orElseGet(() -> ResponseEntity.notFound().build()))
                .orElseGet(
                        () -> ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build());
    }

    private ResponseEntity<PhoneBookItemResponseDto> splitToBodyAndEtag(Validable<Long, PhoneBookItemResponseDto> saved, HttpStatus status) {
        return ResponseEntity.status(status)
                .header(HttpHeaders.ETAG, saved.validator().toString())
                .body(saved.validated());
    }

    private Optional<Long> parseEtagToVersion(String etag) {
        try {
            return Optional.ofNullable(etag)
                    .filter(not(String::isBlank))
                    .map(String::trim)
                    .filter(not("*"::equals))
                    .map(Long::valueOf);
        } catch (NumberFormatException parseException) {
            throw new VersionMismatchException(parseException.getMessage(), parseException);
        }
    }
}
