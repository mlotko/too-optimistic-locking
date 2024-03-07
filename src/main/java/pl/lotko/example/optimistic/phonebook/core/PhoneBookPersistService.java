package pl.lotko.example.optimistic.phonebook.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.lotko.example.optimistic.phonebook.api.PhoneBookItemRequestDto;
import pl.lotko.example.optimistic.phonebook.api.PhoneBookItemResponseDto;
import pl.lotko.example.optimistic.phonebook.api.Validable;
import pl.lotko.example.optimistic.phonebook.persistence.PhoneBookItemEntity;
import pl.lotko.example.optimistic.phonebook.persistence.PhoneBookItemRepository;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PhoneBookPersistService {
    private final PhoneBookDtoToEntityMapper mapper;
    private final PhoneBookItemRepository repository;

    @Transactional
    public Validable<Long, PhoneBookItemResponseDto> saveNew(PhoneBookItemRequestDto newPhoneBookItemDto) {
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
        return new Validable<>(0L, dto);
    }

    @Transactional(readOnly = true)
    public Optional<Validable<Long, PhoneBookItemResponseDto>> getSaved(Long id) {
        return repository.findById(id)
                .map(entity -> {
                    final var dto = mapper.mapEntityToDto(entity);
                    System.out.printf("""
                            Entity:
                            %s
                            DTO:
                            %s\n
                            """, entity, dto);
                    return new Validable<>(entity.getVersion(), dto);
                });
    }

    @Transactional(readOnly = true)
    public Optional<Validable<Long, PhoneBookItemResponseDto>> getSavedIfNotVersion(Long id, Long version) {
        return repository.findById(id)
                .map(entity -> {
                    if (Objects.equals(version, entity.getVersion())) {
                        throw new UpdateNotRequiredException("phone book item %d already still in version %d".formatted(
                                id, version
                        ));
                    }
                    final var dto = mapper.mapEntityToDto(entity);
                    System.out.printf("""
                            Entity:
                            %s
                            DTO:
                            %s\n
                            """, entity, dto);
                    return new Validable<>(entity.getVersion(), dto);
                });
    }


    @Transactional
    public Optional<Validable<Long, PhoneBookItemResponseDto>> replaceExisting(Long id, Validable<Long, PhoneBookItemRequestDto> existingPhoneBookVersionItemDto) {
        return repository.findById(id)
                .map(existing -> {
                    if (!Objects.equals(existingPhoneBookVersionItemDto.validator(), existing.getVersion())) {
                        throw new VersionMismatchException("Expected item %s to be in version %d but was %d".formatted(
                                existingPhoneBookVersionItemDto.validated(), existingPhoneBookVersionItemDto.validator(),
                                existing.getVersion()
                        ));
                    }
                    System.out.printf("Found %s by id %d\n", existing, id);
                    mapper.mapDtoToExistingEntity(existingPhoneBookVersionItemDto.validated(), existing);
                    System.out.printf("Applied changes: %s\n", existing);
                    final PhoneBookItemEntity saved = repository.save(existing);
                    System.out.printf("Saved changes: %s\n", saved);
                    final PhoneBookItemResponseDto dto = mapper.mapEntityToDto(saved);
                    System.out.printf("DTO %s\n", dto);
                    return new Validable<>(existing.getVersion() + 1, dto);
                });
    }


}
