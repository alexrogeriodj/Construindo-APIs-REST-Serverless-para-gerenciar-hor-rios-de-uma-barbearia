package com.alexrogeriodj.barbershop.service;

import com.alexrogeriodj.barbershop.domain.Availability;
import com.alexrogeriodj.barbershop.domain.Barbershop;
import com.alexrogeriodj.barbershop.exception.BarbershopAlreadyRegisteredException;
import com.alexrogeriodj.barbershop.ports.in.AvailabilityCommand;
import com.alexrogeriodjs.barbershop.ports.in.CreateBarbershopCommand;
import com.alexrogeriodj.barbershop.ports.in.CreateBarbershopUseCase;
import com.alexrogeriodj.barbershop.ports.out.BarbershopRepositoryPort;
import com.alexrogeriodj.barbershop.ports.out.CreatedBarbershop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateBarbershopServiceTest {

    @Mock
    private BarbershopRepositoryPort barbershopRepositoryPort;

    private CreateBarbershopUseCase createBarbershopUseCase;

    @BeforeEach
    void setUp() {
        createBarbershopUseCase = new CreateBarbershopService(barbershopRepositoryPort);
    }

    @Test
    void whenNewBarbershopIsInformedThenItShouldBeCreated() {
        CreateBarbershopCommand command = createBarbershopCommand();
        Barbershop expectedBarbershop = createBarbershop(command);

        when(barbershopRepositoryPort.load(command.getName())).thenReturn(Optional.empty());
        when(barbershopRepositoryPort.save(expectedBarbershop)).thenReturn(expectedBarbershop);

        CreatedBarbershop createdBarbershop = createBarbershopUseCase.create(command);

        assertThat(createdBarbershop.getName(), equalTo(command.getName()));
        assertThat(createdBarbershop.getAddress(), equalTo(command.getAddress()));
        assertThat(createdBarbershop.getCity(), equalTo(command.getCity()));
    }

    @Test
    void whenExistingBarbershopIsInformedThenAnExceptionShouldBeThrown() {
        CreateBarbershopCommand command = createBarbershopCommand();
        Barbershop expectedBarbershop = createBarbershop(command);

        when(barbershopRepositoryPort.load(command.getName())).thenReturn(Optional.of(expectedBarbershop));

        assertThrows(BarbershopAlreadyRegisteredException.class, () -> createBarbershopUseCase.create(command));
    }

    private Barbershop createBarbershop(CreateBarbershopCommand command) {
        AvailabilityCommand availabilityCommand = command.getAvailabilityCommand();
        Availability availability = createAvailability(availabilityCommand);
        return Barbershop.builder()
                .name(command.getName())
                .city(command.getCity())
                .address(command.getAddress())
                .availability(availability)
                .build();
    }

    private Availability createAvailability(AvailabilityCommand availabilityCommand) {
        return Availability.of(
                availabilityCommand.getDaysOfWeek(),
                availabilityCommand.getOpenTime(),
                availabilityCommand.getCloseTime(),
                availabilityCommand.getDuration());
    }

    private CreateBarbershopCommand createBarbershopCommand() {
        var name = "Barbearia do Rodrigo";
        var address = "Avenida Paulista, 201";
        var city = "São Paulo";
        var availability = createAvailabilityCommand();

        return new CreateBarbershopCommand(name, address, city, availability);
    }

    private AvailabilityCommand createAvailabilityCommand() {
        return new AvailabilityCommand(List.of(DayOfWeek.FRIDAY),
                LocalTime.of(10, 0),
                LocalTime.of(19, 0),
                LocalTime.of(1, 0));
    }
}
