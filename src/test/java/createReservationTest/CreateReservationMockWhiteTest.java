package createReservationTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.*;
import exceptions.*;

public class CreateReservationMockWhiteTest {

    private DataAccess dataAccess;
    private EntityManager em;

    private Ride ride;
    private Traveler traveler;
    private Driver driver;

    @Before
    public void init() {
        em = mock(EntityManager.class);
        dataAccess = new DataAccess(em);

        ride = mock(Ride.class);
        traveler = mock(Traveler.class);
        driver = mock(Driver.class);

        when(ride.getDriver()).thenReturn(driver);
        when(driver.getEmail()).thenReturn("driver@test.com");
    }

    // Caso 1: Ride no existe
    @Test
    public void test1_RideDoesNotExist() throws Exception {
        when(em.find(Ride.class, 999)).thenReturn(null);
        assertNull(dataAccess.createReservation(1, 999, "t1@test.com"));
    }

    // Caso 2: NotEnoughSeats
    @Test
    public void test2_NotEnoughSeats() throws Exception {
        when(em.find(Ride.class, 1)).thenReturn(ride);
        when(ride.getnPlaces()).thenReturn(1);

        assertThrows(NotEnoughAvailableSeatsException.class,
            () -> dataAccess.createReservation(5, 1, "t1@test.com"));
    }

    // Caso 3: Reserva ya existente
    @Test
    public void test3_ReservationAlreadyExists() throws Exception {
        when(em.find(Ride.class, 1)).thenReturn(ride);
        when(ride.getnPlaces()).thenReturn(5);
        when(em.find(Traveler.class, "t1@test.com")).thenReturn(traveler);
        when(em.find(Driver.class, "driver@test.com")).thenReturn(driver);

        when(ride.doesReservationExist(1, traveler)).thenReturn(true);

        assertThrows(ReservationAlreadyExistException.class,
            () -> dataAccess.createReservation(1, 1, "t1@test.com"));
    }

    // Caso 4: Reserva creada correctamente
    @Test
    public void test4_ReservationCreated() throws Exception {
        Reservation mockRes = mock(Reservation.class);

        when(em.find(Ride.class, 1)).thenReturn(ride);
        when(ride.getnPlaces()).thenReturn(5);
        when(em.find(Traveler.class, "t1@test.com")).thenReturn(traveler);
        when(em.find(Driver.class, "driver@test.com")).thenReturn(driver);

        when(ride.doesReservationExist(2, traveler)).thenReturn(false);
        when(traveler.makeReservation(ride, 2)).thenReturn(mockRes);

        Reservation res = dataAccess.createReservation(2, 1, "t1@test.com");

        assertNotNull(res);
        assertEquals(mockRes, res);
        verify(driver).addReservation(mockRes);
        verify(ride).addReservation(mockRes);
    }
}
