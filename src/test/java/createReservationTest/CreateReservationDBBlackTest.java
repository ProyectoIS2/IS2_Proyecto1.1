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

    private String travelerEmail = "t1@test.com";
    private Traveler traveler;
    private Driver driver;
    private Ride ride;
    private Car car;

    @Before
    public void setUp() throws Exception {
        testDA = new TestDataAccess();
        testDA.open();

        dataAccess = new DataAccess();
        dataAccess.open();

        // Crear driver + ride persistidos en BD
        driver = testDA.addDriverWithRide("driver@test.com", "Driver",
                "A", "B", new Date(), 4, 20.0f);

        // Recuperar el ride creado
        if (driver.getRides().isEmpty()) {
            throw new RuntimeException("addDriverWithRide no ha creado rides correctamente");
        }
        ride = driver.getRides().get(driver.getRides().size()-1);

        // Crear el traveler
        try {
            traveler = dataAccess.createTraveler(travelerEmail, "Traveler", "pwd");
        } catch (Exception e) {
            // en caso de que ya exista, nos quedamos con un objeto local con el mismo email para las comprobaciones por mail
            traveler = new Traveler(travelerEmail, "Traveler", "pwd");
        }
    }

    @After
    public void tearDown() {
        try {
            // Borrar traveler si existe
            try {
                dataAccess.deleteAccountTraveler(traveler.getEmail());
            } catch (Exception ignored) {}

            // Borrar driver si existe
            try {
                dataAccess.deleteAccountDriver(driver.getEmail());
            } catch (Exception ignored) {}
        } finally {
            try { dataAccess.close(); } catch (Exception ignored) {}
            try { testDA.close(); } catch (Exception ignored) {}
        }
    }

    // Test 1: todas las entradas válidas => reserva correcta
    @Test
    public void tc01() throws Exception {
        Reservation res = dataAccess.createReservation(1, ride.getRideNumber(), travelerEmail);
        assertNotNull("Reserva esperada (no nula)", res);
        assertEquals("Email traveler", travelerEmail, res.getTraveler().getEmail());
        assertEquals("Ride number", ride.getRideNumber(), res.getRide().getRideNumber());
    }

    // Test 2: hm <= 0
    @Test
    public void tc02() throws Exception {
        // Según la implementación actual, crea la reserva aunque hm=0, por eso assertNotNull.
        Reservation res = dataAccess.createReservation(0, ride.getRideNumber(), travelerEmail);
        assertNotNull("Según implementación actual, hm=0 produce reserva -> comprobamos no nulo", res);
    }

    // Test 3: rideNumber null => IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void tc03() throws Exception {
        dataAccess.createReservation(1, null, traveler.getEmail());
    }

    // Test 4: rideNumber <= 0
    @Test
    public void tc04() throws Exception {
        Reservation res = dataAccess.createReservation(1, -5, travelerEmail);
        assertNull(res);
    }

    // Test 5: ride no está en BD
    @Test
    public void tc05() throws Exception {
        Reservation res = dataAccess.createReservation(1, 999999, travelerEmail);
        assertNull(res);
    }

    // Test 6: travelerEmail null => IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void tc06() throws Exception {
        dataAccess.createReservation(1, ride.getRideNumber(), null);
    }

    // Test 7: traveler no está en BD
    @Test
    public void tc07() throws Exception {
        Reservation res = dataAccess.createReservation(1, ride.getRideNumber(), "noexiste@test.com");
        assertNull(res);
    }

    // Test 8: Reserva ya existente
    @Test(expected = ReservationAlreadyExistException.class)
    public void tc08() throws Exception {
        // Primera reserva OK
        Reservation first = dataAccess.createReservation(1, ride.getRideNumber(), travelerEmail);
        assertNotNull(first);
        // Segunda reserva idéntica => lanza ReservationAlreadyExistException
        dataAccess.createReservation(1, ride.getRideNumber(), travelerEmail);
    }

    // Test 9: No hay suficientes plazas
    @Test(expected = NotEnoughAvailableSeatsException.class)
    public void tc09() throws Exception {
        int largeHm = 1000; //para solicitar más plazas de las que tiene ride
        dataAccess.createReservation(largeHm, ride.getRideNumber(), travelerEmail);
    }
}

