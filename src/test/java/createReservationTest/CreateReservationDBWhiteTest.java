package createReservationTest;

import dataAccess.DataAccess;
import domain.*;
import exceptions.*;
import testOperations.TestDataAccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

import org.junit.*;

public class CreateReservationDBWhiteTest {

    private DataAccess dataAccess;
    private TestDataAccess testDA;

    private Traveler t1;
    private Ride r1;

    @Before
    public void setUp() {
        dataAccess = new DataAccess(); //se abre la conexión real
        testDA = new TestDataAccess();

        testDA.open();
        dataAccess.open();

        // Crear driver con ride de prueba
        Driver d1 = testDA.addDriverWithRide(
            "driver1@test.com", "Driver1",
            "Donostia", "Bilbao", Date.from(Instant.now()), 5, 10
        );
        r1 = d1.getRides().get(0); //el primer ride de d1

        // Crear traveler de prueba
        try {
            t1 = dataAccess.createTraveler("t1@test.com", "Traveler1", "123");
        } catch (UserAlreadyExistException e) {
            t1 = null; // en caso de q existiese no se crea
        }
    }

    @After
    public void tearDown() {
        dataAccess.close();
        testDA.close();

        // Limpieza de datos
        testDA.open();
        testDA.removeDriver("driver1@test.com");
   //     if (t1 != null) testDA.removeTraveler(t1.getEmail());
        testDA.close();
    }

    // Test 1: Ride inexistente
    @Test
    public void test1() {
        try {
            Reservation res = dataAccess.createReservation(1, 99999, "t1@test.com");
            assertNull(res); //comprobar que no se ha creado ninguna reserva
        } catch (Exception e) {
            fail("No debería lanzar excepción en test1: " + e.getMessage());
        }
    }

    // Test 2: Asientos insuficientes, se solicitan más asientos de los q el ride tiene
    @Test
    public void test2() {
        try {
            assertThrows(NotEnoughAvailableSeatsException.class,
                () -> dataAccess.createReservation(99, r1.getRideNumber(), "t1@test.com"));
        } catch (Exception e) {
            fail("Error inesperado en test2: " + e.getMessage());
        }
    }

    // Caso 3: Reserva ya existe
    @Test
    public void test3() {
        try {
            dataAccess.createReservation(1, r1.getRideNumber(), "t1@test.com"); // primera correcta
            assertThrows(ReservationAlreadyExistException.class,
                () -> dataAccess.createReservation(1, r1.getRideNumber(), "t1@test.com")); // segunda reserva identica falla
        } catch (Exception e) {
            fail("Error inesperado en test3: " + e.getMessage());
        }
    }

    // Caso 4: Reserva creada correctamente
    @Test
    public void test4() {
        try {
            Reservation res = dataAccess.createReservation(2, r1.getRideNumber(), "t1@test.com");
            assertNotNull(res); //se crea la reserva
            assertEquals("t1@test.com", res.getTraveler().getEmail()); //traveler y ride correcto
            assertEquals(r1.getRideNumber(), res.getRide().getRideNumber());
        } catch (Exception e) {
            fail("Error inesperado en test4: " + e.getMessage());
        }
    }
}

