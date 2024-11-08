package com.example.eventsmanager.event.address;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AddressDto {
    private String country;
    private String city;
    private String address;
}
