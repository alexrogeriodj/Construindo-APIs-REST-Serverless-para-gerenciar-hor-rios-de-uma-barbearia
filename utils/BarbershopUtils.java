package com.alexrogeriodj.barbershop.utils;

import com.alexrogeriodj.barbershop.domain.Barbershop;

import static com.alexrogeriodj.barbershop.utils.AvailabilityUtils.createAvailability;

public class BarbershopUtils {

    public static Barbershop createBarbershop() {
        var expectedName = "Barbearia do alexrogeriodj";
        var expectedAddress = "Avenida Paulista 201";
        var expectedCity = "São Paulo";

        var barbershop = Barbershop.builder()
                .name(expectedName)
                .address(expectedAddress)
                .city(expectedCity)
                .availability(createAvailability())
                .build();
        return barbershop;
    }
}
