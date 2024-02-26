package pl.lotko.example.optimistic.phonebook.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "phone_book_item")
@Getter
@Setter
@ToString
public class PhoneBookItemEntity {
    @Column(name = "id", nullable = false, updatable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "name")
    private String name;

    @Column(name = "family_name")
    private String familyName;

    @Column(name = "phone")
    private String phone;
}
