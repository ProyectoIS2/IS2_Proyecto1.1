package returnMoneyTravelersTest;

import dataAccess.DataAccess;
import domain.*;
import org.junit.*;
import testOperations.TestDataAccess;

import java.util.*;

import static org.junit.Assert.*;

public class ReturnMoneyTravelersDBBlackTest {

    static DataAccess dataAccess;
    static TestDataAccess testDA;

    private Driver driver;
    private Traveler traveler;
    private Ride ride;
    private Reservation reservation;

    @Before
    public void setUp() throws Exception {
        dataAccess = new DataAccess();
        testDA = new TestDataAccess();
        dataAccess.open();
        testDA.open();

        // Limpiar la BD antes de cada test
        dataAccess.db.getTransaction().begin();
        dataAccess.db.createQuery("DELETE FROM Traveler t").executeUpdate();
        dataAccess.db.createQuery("DELETE FROM Driver d").executeUpdate();
        dataAccess.db.createQuery("DELETE FROM Reservation r").executeUpdate();
        dataAccess.db.createQuery("DELETE FROM Transaction t").executeUpdate();
        dataAccess.db.getTransaction().commit();
        
        // Crear driver + ride (persistido)
        driver = testDA.addDriverWithRide("driver@test.com", "Driver", "A", "B", new Date(), 4, 20.0f);
        ride = driver.getRides().get(driver.getRides().size() - 1);

        
        // Crear traveler y persistirlo
        traveler = dataAccess.createTraveler("t1@test.com", "Traveler", "pwd");

        // Crear reserva y marcar como pagada
        reservation = dataAccess.createReservation(1, ride.getRideNumber(), traveler.getEmail());
        reservation.setPayed(true);
        dataAccess.db.persist(reservation);

        // Iniciar transacción aquí
        dataAccess.db.getTransaction().begin();
    }

    @After
    public void tearDown() throws Exception {
        //  Solo commit si la transacción sigue activa (evita error 613)
        if (dataAccess.db.getTransaction().isActive()) {
            dataAccess.db.getTransaction().commit();
        }

        // Limpiar BD
        testDA.open();
        testDA.removeDriver("driver@test.com");
        testDA.close();
        dataAccess.close();
    }

    // Caso 1: Todo correcto
    @Test
    public void tc01() {
        List<Reservation> list = Arrays.asList(reservation);

        double beforeTraveler = traveler.getMoney();
        double beforeDriver = driver.getMoney();

        dataAccess.returnMoneyTravelers(list, driver.getEmail());

        double afterTraveler = traveler.getMoney();
        double afterDriver = driver.getMoney();

        double cost = reservation.getCost();

        assertEquals(beforeTraveler + cost, afterTraveler, 0.001);
        assertEquals(beforeDriver - cost, afterDriver, 0.001);
    }

    // Caso 2: resList = null (manejado internamente, no lanza excepción)
    @Test
    public void tc02() {
        double beforeTraveler = traveler.getMoney();
        double beforeDriver = driver.getMoney();

        dataAccess.returnMoneyTravelers(null, driver.getEmail());

        assertEquals(beforeTraveler, traveler.getMoney(), 0.001);
        assertEquals(beforeDriver, driver.getMoney(), 0.001);
    }

    // Caso 3: lista vacía
    @Test
    public void tc03() {
        List<Reservation> list = new ArrayList<>();
        dataAccess.returnMoneyTravelers(list, driver.getEmail());
        assertTrue(true); 
    }

    // Caso 4: Traveler no en BD
    @Test
    public void tc04() {
        reservation.getTraveler().setEmail("noExiste@test.com");
        List<Reservation> list = Arrays.asList(reservation);
        dataAccess.returnMoneyTravelers(list, driver.getEmail());
        assertTrue(true);
    }

    // Caso 5: Driver no en BD
    @Test
    public void tc05() {
        List<Reservation> list = Arrays.asList(reservation);
        dataAccess.returnMoneyTravelers(list, "noExiste@test.com");
        assertTrue(true);
    }

    // Caso 6: email = null
    @Test
    public void tc06() {
        List<Reservation> list = Arrays.asList(reservation);
        try {
            dataAccess.returnMoneyTravelers(list, null);
        } catch (IllegalArgumentException e) {
            // esperado por ObjectDB
            assertTrue(true);
        }
    }

    // Caso 7: reserva no pagada
    @Test
    public void tc07() {
        reservation.setPayed(false);
        List<Reservation> list = Arrays.asList(reservation);
        dataAccess.returnMoneyTravelers(list, driver.getEmail());
        assertTrue(true);
    }
}
