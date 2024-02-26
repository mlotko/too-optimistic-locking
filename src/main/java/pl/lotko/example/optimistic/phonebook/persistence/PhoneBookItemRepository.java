package pl.lotko.example.optimistic.phonebook.persistence;

import org.springframework.data.repository.CrudRepository;

public interface PhoneBookItemRepository extends CrudRepository<PhoneBookItemEntity, Long> {
}
