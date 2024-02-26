package pl.lotko.example.optimistic.phonebook.core;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.lotko.example.optimistic.phonebook.api.PhoneBookItemRequestDto;
import pl.lotko.example.optimistic.phonebook.api.PhoneBookItemResponseDto;
import pl.lotko.example.optimistic.phonebook.persistence.PhoneBookItemEntity;

@Mapper(componentModel = "spring")
public interface PhoneBookDtoToEntityMapper {
    PhoneBookItemEntity mapDtoToNewEntity(PhoneBookItemRequestDto dto);
    void mapDtoToExistingEntity(PhoneBookItemRequestDto dto, @MappingTarget PhoneBookItemEntity existingEntity);

    PhoneBookItemResponseDto mapEntityToDto(PhoneBookItemEntity entity);
}
