package createReservationTest;

import dataAccess.DataAccess;
import domain.*;
import exceptions.*;
import testOperations.TestDataAccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Date;

import org.junit.*;

public class CreateReservationDBWhiteTest {

    private DataAccess dataAccess;          
    private TestDataAccess testDA;  

    @Before
    public void setUp() {
        dataAccess = new DataAccess();
        testDA = new TestDataAccess();
        testDA.open();
    }

    @After
    public void tearDown() {
        testDA.close();
    }

    // Test 1: NullPointerException => El ride indicado no existe
    @Test
    public void test1() throws Exception {
        assertNull(dataAccess.createReservation(2, null, "t1@test.com")); 
    }

    // Test 2: NotEnoughAvailableSeatsException => asientos indicados > asientos disponibles
    @Test
    public void test2() throws Exception {
        Driver d = testDA.addDriverWithRide("driver@test.com", "Driver",
                "Donostia", "Bilbao", new Date(), 1, 10);
        Ride r = d.getRides().get(0);
        dataAccess.createTraveler("t1@test.com", "Traveler1", "123");

        assertThrows(NotEnoughAvailableSeatsException.class,
            () -> dataAccess.createReservation(2, r.getRideNumber(), "t1@test.com"));
    }

    // Test 3: ReservationAlreadyExistException => la reserva ya se ha realizado 
    @Test
    public void test3() throws Exception {
        Driver d = testDA.addDriverWithRide("driver@test.com", "Driver",
                "Donostia", "Bilbao", new Date(), 5, 10);
        Ride r = d.getRides().get(0);
        dataAccess.createTraveler("t1@test.com", "Traveler1", "123");

        dataAccess.createReservation(2, r.getRideNumber(), "t1@test.com");

        assertThrows(ReservationAlreadyExistException.class,
            () -> dataAccess.createReservation(2, r.getRideNumber(), "t1@test.com"));
    }

    // Test4: Reserva creada correctamente
    @Test
    public void test4() throws Exception {
        Driver d = testDA.addDriverWithRide("driver@test.com", "Driver",
                "Donostia", "Bilbao", new Date(), 5, 10);
        Ride r = d.getRides().get(0);
        dataAccess.createTraveler("t1@test.com", "Traveler1", "123");

        Reservation res = dataAccess.createReservation(2, r.getRideNumber(), "t1@test.com");

        assertNotNull(res);
        assertEquals("t1@test.com", res.getTraveler().getEmail());
        assertEquals(r.getRideNumber(), res.getRide().getRideNumber());
    }
}

