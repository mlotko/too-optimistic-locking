package pl.lotko.example.optimistic.phonebook.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PhoneBookItemRequestDto(
        @JsonProperty("name")
        String name,

        @JsonProperty("family_name")
        String familyName,

        @JsonProperty("phone")
        String phone
) {
}
