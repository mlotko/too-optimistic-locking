package pl.lotko.example.optimistic.phonebook.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.lotko.example.optimistic.phonebook.api.PhoneBookItemRequestDto;
import pl.lotko.example.optimistic.phonebook.api.PhoneBookItemResponseDto;
import pl.lotko.example.optimistic.phonebook.persistence.PhoneBookItemEntity;
import pl.lotko.example.optimistic.phonebook.persistence.PhoneBookItemRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PhoneBookPersistService {
    private final PhoneBookDtoToEntityMapper mapper;
    private final PhoneBookItemRepository repository;

    @Transactional
    public PhoneBookItemResponseDto saveNew(PhoneBookItemRequestDto newPhoneBookItemDto) {
        final PhoneBookItemEntity phoneBookItemEntity = repository.save(mapper.mapDtoToNewEntity(newPhoneBookItemDto));
        final PhoneBookItemResponseDto dto = mapper.mapEntityToDto(phoneBookItemEntity);
        System.out.printf("""
                Saved:
                %s
                to:
                %s:
                and returned:
                %s\n
                """, newPhoneBookItemDto, phoneBookItemEntity, dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public Optional<PhoneBookItemResponseDto> getSaved(Long id) {
        return repository.findById(id)
                .map(entity -> {
                    final var dto = mapper.mapEntityToDto(entity);
                    System.out.printf("""
                            Entity:
                            %s
                            DTO:
                            %s\n
                            """, entity, dto);
                    return dto;
                });
    }

    @Transactional
    public Optional<PhoneBookItemResponseDto> replaceExisting(Long id, PhoneBookItemRequestDto existingPhoneBookItemDto) {
        return repository.findById(id)
                .map(existing -> {
                    System.out.printf("Found %s by id %d\n", existing, id);
                    mapper.mapDtoToExistingEntity(existingPhoneBookItemDto, existing);
                    System.out.printf("Applied changes: %s\n", existing);
                    final PhoneBookItemEntity saved = repository.save(existing);
                    System.out.printf("Saved changes: %s\n", saved);
                    final PhoneBookItemResponseDto dto = mapper.mapEntityToDto(saved);
                    System.out.printf("DTO %s\n", dto);
                    return dto;
                });
    }


}
