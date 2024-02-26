package pl.lotko.example.optimistic.phonebook.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.lotko.example.optimistic.phonebook.core.PhoneBookPersistService;

import java.util.Optional;

@RestController
@RequestMapping("/api/phone-book/item")
@RequiredArgsConstructor
class PhoneBookItemController {
    private final PhoneBookPersistService phoneBookPersistService;

    @PostMapping
    PhoneBookItemResponseDto create(
            @RequestBody PhoneBookItemRequestDto newPhoneBookItemDto
    ) {
        return phoneBookPersistService.saveNew(newPhoneBookItemDto);
    }

    @GetMapping("{id}")
    Optional<PhoneBookItemResponseDto> get(@PathVariable Long id) {
        return phoneBookPersistService.getSaved(id);
    }

    @PutMapping("{id}")
    Optional<PhoneBookItemResponseDto> replace(
            @PathVariable Long id,
            @RequestBody PhoneBookItemRequestDto newPhoneBookItemDto
    ) {
        return phoneBookPersistService.replaceExisting(id, newPhoneBookItemDto);
    }
}
