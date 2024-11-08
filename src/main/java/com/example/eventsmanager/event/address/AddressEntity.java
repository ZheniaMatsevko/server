package com.example.eventsmanager.event.address;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AddressEntity {
    private String country;
    private String city;
    private String address;
}
