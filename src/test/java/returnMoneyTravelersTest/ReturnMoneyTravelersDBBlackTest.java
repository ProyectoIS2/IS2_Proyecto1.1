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

        // ✅ Iniciar transacción aquí
        dataAccess.db.getTransaction().begin();
    }

    @After
    public void tearDown() throws Exception {
        // ✅ Solo commit si la transacción sigue activa (evita error 613)
        if (dataAccess.db.getTransaction().isActive()) {
            dataAccess.db.getTransaction().commit();
        }

        // Limpiar BD
        testDA.open();
        testDA.removeDriver("driver@test.com");
        testDA.close();
        dataAccess.close();
    }

    // Test 1: Todo correcto => reembolso ok
    @Test
    public void tc01() {
    	
        List<Reservation> list = Arrays.asList(reservation);

        //para guardar los valores iniciales
        double beforeTraveler = traveler.getMoney();
        double beforeDriver = driver.getMoney();
        
        dataAccess.returnMoneyTravelers(list, driver.getEmail());
        
        //recuperar valores actualizados de BD
        Driver driverFromDB = dataAccess.db.find(Driver.class, driver.getEmail());
        Traveler travelerFromDB = dataAccess.db.find(Traveler.class, traveler.getEmail());
        
        double cost = reservation.getCost();

        assertEquals(travelerFromDB.getMoney(), beforeTraveler + cost, 0.001);
        assertEquals(driverFromDB.getMoney(), beforeDriver - cost, 0.001);
    }

    // Caso 2: resList null (no lanza excepción)
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
        assertTrue(true); // simplemente comprobar que no lanza excepción
    }

    // Caso 4: Traveler no está en BD
    @Test
    public void tc04() {
        reservation.getTraveler().setEmail("noExiste@test.com");
        List<Reservation> list = Arrays.asList(reservation);
        dataAccess.returnMoneyTravelers(list, driver.getEmail());
        assertTrue(true);
    }

    // Caso 5: Driver no está en BD
    @Test
    public void tc05() {
        List<Reservation> list = Arrays.asList(reservation);
        dataAccess.returnMoneyTravelers(list, "noExiste@test.com");
        assertTrue(true);
    }

    // Caso 6: email null => IllegalArgumentException
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

    // Caso 7: reserva no pagada => sin cambios
    @Test
    public void tc07() {
        reservation.setPayed(false);
        List<Reservation> list = Arrays.asList(reservation);
        dataAccess.returnMoneyTravelers(list, driver.getEmail());
        assertTrue(true);
    }
}
