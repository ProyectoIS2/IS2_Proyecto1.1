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

    private static DataAccess dataAccess;
    private static TestDataAccess testDA;

    private Traveler t1;
    private Ride r1;

    @BeforeClass
    public static void init() {
        dataAccess = new DataAccess(); //se abre la conexión real
        testDA = new TestDataAccess();

    }
    @Before
    public void setUp() {
        //dataAccess = new DataAccess(); //se abre la conexión real
        //testDA = new TestDataAccess();

        testDA.open();

        // Crear driver con ride de prueba
        Driver d1 = testDA.addDriverWithRide(
            "driver1@test.com", "Driver1",
            "Donostia", "Bilbao", Date.from(Instant.now()), 5, 10
        );
        r1 = d1.getRides().get(0); //el primer ride de d1
        testDA.close();

        // Crear traveler de prueba        
        dataAccess.open();
        try {
            t1 = dataAccess.createTraveler("t1@test.com", "Traveler1", "123");
        } catch (UserAlreadyExistException e) {
            t1 = null; // en caso de q existiese no se crea
        }
        dataAccess.close();

    }

    @After
    public void tearDown() {

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
            dataAccess.open();
            Reservation res = dataAccess.createReservation(1, 99999, "t1@test.com");
            assertNull(res); //esto verifica que res sea null
        } catch (Exception e) {
            fail("No debería lanzar excepción en test1: " + e.getMessage()); //fuerza el fallo del test, indica que no se debería haber llegado a este punto
        }finally{
            dataAccess.close();
        }
    }

    // Test 2: Asientos insuficientes, se solicitan más asientos de los q el ride tiene
       @Test
    public void test2() {
        try {
            dataAccess.open();
            assertThrows(NotEnoughAvailableSeatsException.class,
                () -> dataAccess.createReservation(99, r1.getRideNumber(), "t1@test.com")); //verifica que se lanza una excepción especifica al ejecutar el metodo de la derecha de ,()->
        } catch (Exception e) {
            fail("Error inesperado en test2: " + e.getMessage());
        }finally{
            dataAccess.close();
        }
    }

    // Caso 3: Reserva ya existe
       /**         @Test
   public void test3() {
        try {
            dataAccess.open();
            dataAccess.createReservation(1, r1.getRideNumber(), "t1@test.com"); // primera correcta
            assertThrows(ReservationAlreadyExistException.class,
                () -> dataAccess.createReservation(1, r1.getRideNumber(), "t1@test.com")); // segunda reserva identica falla
        } catch (Exception e) {
            fail("Error inesperado en test3: " + e.getMessage());
        }finally{
            dataAccess.close();
        }

    }

    // Caso 4: Reserva creada correctamente
    @Test
    public void test4() {
        try {
        	dataAccess.open();
            Reservation res = dataAccess.createReservation(2, r1.getRideNumber(), "t1@test.com");
            assertNotNull(res); //esto verifica que res no sea null, es decir, que la reserva sea correcta
            assertEquals("t1@test.com", res.getTraveler().getEmail()); //para comparar valores, en este caso entre el email del traveler de la reserva es correcto
            assertEquals(r1.getRideNumber(), res.getRide().getRideNumber());
        } catch (Exception e) {
            fail("Error inesperado en test4: " + e.getMessage());
        }finally{
            dataAccess.close();
        }
    }  */
}

