package returnMoneyTravelersTest;

import dataAccess.DataAccess;
import domain.*;
import org.junit.*;
import testOperations.TestDataAccess;

import java.util.*;

import static org.junit.Assert.*;

public class ReturnMoneyTravelersDBWhiteTest {

    static DataAccess dataAccess;
    static TestDataAccess testDA;

    private Driver driver;
    private Traveler traveler;
    private Ride ride;
    private Reservation reservation;
    private Car car;

    @Before
    public void setUp() {
        dataAccess = new DataAccess();
        testDA = new TestDataAccess();
        dataAccess.open();
        testDA.open();

        // Crear driver + traveler + coche + viaje
        driver = testDA.createDriver("driver@test.com", "Driver");
        traveler = new Traveler("t1@test.com", "Traveler", "pwd");
        car = new Car("1234ABC", 4, driver, false);
        ride = new Ride("A", "B", new Date(), 20.0f, driver, car);

        // Crear reserva asociada
        reservation = new Reservation(1, ride, traveler);
        reservation.setPayed(true);

        dataAccess.db.getTransaction().begin();
    }

    @After
    public void tearDown() {
        if (dataAccess.db.getTransaction().isActive())
            dataAccess.db.getTransaction().commit();

        testDA.open();
        testDA.removeDriver("driver@test.com");
        testDA.close();
        dataAccess.close();
    }

    // Test 1: reserva pagada, se realiza el reembolso correctamente
    @Test
    public void tc01() {
        float dineroInicialTraveler = traveler.getMoney();
        float dineroInicialDriver = driver.getMoney();

        List<Reservation> list = Arrays.asList(reservation);
        dataAccess.returnMoneyTravelers(list, driver.getEmail());

        float esperadoTraveler = dineroInicialTraveler + reservation.getCost();
        float esperadoDriver = dineroInicialDriver - reservation.getCost();

        assertEquals(esperadoTraveler, traveler.getMoney(), 0.001);
        assertEquals(esperadoDriver, driver.getMoney(), 0.001);
    }

    // Test 2: Lista vacía => no se modifica el estado
    @Test
    public void tc02() {
        float dineroInicialTraveler = traveler.getMoney();
        float dineroInicialDriver = driver.getMoney();

        dataAccess.returnMoneyTravelers(new ArrayList<>(), driver.getEmail());

        assertEquals(dineroInicialTraveler, traveler.getMoney(), 0.001);
        assertEquals(dineroInicialDriver, driver.getMoney(), 0.001);
    }

    // Test 3: Una reserva no pagada => no cambia el dinero
    @Test
    public void tc03() {
        reservation.setPayed(false);
        float dineroInicialTraveler = traveler.getMoney();
        float dineroInicialDriver = driver.getMoney();

        List<Reservation> list = Arrays.asList(reservation);
        dataAccess.returnMoneyTravelers(list, driver.getEmail());

        assertEquals(dineroInicialTraveler, traveler.getMoney(), 0.001);
        assertEquals(dineroInicialDriver, driver.getMoney(), 0.001);
    }

    // Test 4: resList null => entra en el catch
    @Test
    public void tc04() {
        try {
            dataAccess.returnMoneyTravelers(null, driver.getEmail());
            assertTrue(true);
        } catch (Exception e) {
            fail("No debería lanzar excepción");
        }
    }
}
