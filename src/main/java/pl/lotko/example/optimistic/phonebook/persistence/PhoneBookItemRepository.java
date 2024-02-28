package pl.lotko.example.optimistic.phonebook.persistence;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PhoneBookItemRepository extends CrudRepository<PhoneBookItemEntity, Long> {
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PhoneBookItemEntity> findById(Long aLong);
}
