package createReservationTest;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Traveler;
import domain.Car;
import domain.Ride;
import domain.Reservation;
import exceptions.NotEnoughAvailableSeatsException;
import exceptions.ReservationAlreadyExistException;
import org.junit.*;
import testOperations.TestDataAccess;

import java.util.Date;

import static org.junit.Assert.*;

public class CreateReservationDBBlackTest {

    static DataAccess dataAccess;
    static TestDataAccess testDA;

    private Traveler traveler;
    private Driver driver;
    private Ride ride;
    private Car car;

    @Before
    public void setUp() {
        dataAccess = new DataAccess();
        testDA = new TestDataAccess();
        testDA.open();
        dataAccess.open();

        // crear driver + traveler + ride
        driver = testDA.createDriver("driver@test.com", "Driver");
        traveler = new Traveler("t1@test.com", "Traveler", "pwd");
        car = new Car("1234ABC", 4, driver, false);
        ride = new Ride("A", "B", new Date(), 20.0f, driver, car);
        
    }

    @After
    public void tearDown() {
        dataAccess.close();
        testDA.close();
        testDA.open();
        testDA.removeDriver("driver@test.com");
        testDA.close();
    }

    // Test 1: todas las entradas válidas → reserva correcta
    @Test
    public void tc01() throws Exception {
        Reservation res = dataAccess.createReservation(1, ride.getRideNumber(), traveler.getEmail());
        assertNotNull(res);
        assertEquals(traveler, res.getTraveler());
        assertEquals(ride, res.getRide());
    }

    // Test 2: hm <= 0
    @Test
    public void tc02() throws Exception {
        Reservation res = dataAccess.createReservation(0, ride.getRideNumber(), traveler.getEmail());
        assertNull(res);
    }

    // Test 3: rideNumber == null
    @Test
    public void tc03() throws Exception {
        Reservation res = dataAccess.createReservation(1, null, traveler.getEmail());
        assertNull(res);
    }

    // Test 4: rideNumber <= 0
    @Test
    public void tc04() throws Exception {
        Reservation res = dataAccess.createReservation(1, -5, traveler.getEmail());
        assertNull(res);
    }

    // Test 5: ride no está en BD
    @Test
    public void tc05() throws Exception {
        Reservation res = dataAccess.createReservation(1, 9999, traveler.getEmail());
        assertNull(res);
    }

    // Test 6: travelerEmail == null
    @Test
    public void tc06() throws Exception {
        Reservation res = dataAccess.createReservation(1, ride.getRideNumber(), null);
        assertNull(res);
    }

    // Test 7: traveler no está en BD
    @Test
    public void tc07() throws Exception {
        Reservation res = dataAccess.createReservation(1, ride.getRideNumber(), "noexiste@test.com");
        assertNull(res);
    }

    // Test 8: Reserva ya existente
    @Test
    public void tc08() throws Exception {
        dataAccess.createReservation(1, ride.getRideNumber(), traveler.getEmail());
        dataAccess.createReservation(1, ride.getRideNumber(), traveler.getEmail()); // segunda → excepción
    }

    // Test 9: No hay suficientes plazas
    @Test
    public void tc09() throws Exception {
        dataAccess.createReservation(10, ride.getRideNumber(), traveler.getEmail());
    }
}
