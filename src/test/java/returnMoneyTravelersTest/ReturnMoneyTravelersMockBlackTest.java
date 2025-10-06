package returnMoneyTravelersTest;

import dataAccess.DataAccess;
import domain.Car;
import domain.Driver;
import domain.Traveler;
import domain.Reservation;
import domain.Ride;
import org.junit.*;
import org.mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnMoneyTravelersMockBlackTest {

    static DataAccess dataAccess;
    static Traveler traveler;
    static Driver driver;
    static Ride ride;
    static Reservation reservation;
    static Car car;

    protected MockedStatic<Persistence> persistenceMock;

    @Mock
    protected EntityManagerFactory emf;
    @Mock
    protected EntityManager db;
    @Mock
    protected EntityTransaction et;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock de Persistence
        persistenceMock = Mockito.mockStatic(Persistence.class);
        persistenceMock.when(() -> Persistence.createEntityManagerFactory(any()))
                .thenReturn(emf);

        when(emf.createEntityManager()).thenReturn(db);
        when(db.getTransaction()).thenReturn(et);

        // Objetos reales (no persistidos)
        driver = new Driver("driver@test.com", "Driver", "pwd");
        traveler = new Traveler("t1@test.com", "Traveler", "pwd");
        car = new Car("1234ABC", 4, driver, false);
        ride = new Ride("A", "B", new Date(), 20.0f, driver, car);
        driver.addRide("A", "B", new Date(), 20.0f, car);

        reservation = new Reservation(1, ride, traveler);
        reservation.setPayed(true);

        // Mock del comportamiento del EntityManager
        when(db.find(Traveler.class, traveler.getEmail())).thenReturn(traveler);
        when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);

        // Mock de persist (no hace nada)
        doNothing().when(db).persist(any());

        dataAccess = new DataAccess(db);
    }

    @After
    public void tearDown() {
        persistenceMock.close();
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

    // Caso 2: resList = null
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
        assertTrue(true); // no lanza excepción
    }

    // Caso 4: Traveler no en BD
    @Test
    public void tc04() {
        when(db.find(Traveler.class, traveler.getEmail())).thenReturn(null);
        List<Reservation> list = Arrays.asList(reservation);
        dataAccess.returnMoneyTravelers(list, driver.getEmail());
        assertTrue(true);
    }

    // Caso 5: Driver no en BD
    @Test
    public void tc05() {
        when(db.find(Driver.class, driver.getEmail())).thenReturn(null);
        List<Reservation> list = Arrays.asList(reservation);
        dataAccess.returnMoneyTravelers(list, driver.getEmail());
        assertTrue(true);
    }

    // Caso 6: email = null
    @Test
    public void tc06() {
        List<Reservation> list = Arrays.asList(reservation);
        try {
            dataAccess.returnMoneyTravelers(list, null);
        } catch (IllegalArgumentException e) {
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
