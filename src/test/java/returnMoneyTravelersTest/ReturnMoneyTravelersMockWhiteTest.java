package returnMoneyTravelersTest;

import dataAccess.DataAccess;
import domain.*;
import org.junit.*;
import org.mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnMoneyTravelersMockWhiteTest {

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

        // Mock estático de Persistence
        persistenceMock = Mockito.mockStatic(Persistence.class);
        persistenceMock.when(() -> Persistence.createEntityManagerFactory(any()))
                .thenReturn(emf);

        when(emf.createEntityManager()).thenReturn(db);
        when(db.getTransaction()).thenReturn(et);

        // Objetos reales
        driver = new Driver("driver@test.com", "Driver", "pwd");
        traveler = new Traveler("t1@test.com", "Traveler", "pwd");
        car = new Car("1234ABC", 4, driver, false);
        ride = new Ride("A", "B", new Date(), 20.0f, driver, car);
        reservation = new Reservation(1, ride, traveler);
        reservation.setPayed(true);

        // Mock comportamiento de EntityManager
        when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);
        when(db.find(Traveler.class, traveler.getEmail())).thenReturn(traveler);

        dataAccess = new DataAccess(db);
    }

    @After
    public void tearDown() {
        persistenceMock.close();
    }

    // Test 1: flujo completo => se persisten las entidades esperadas
    @Test
    public void tc01() {
        List<Reservation> list = Arrays.asList(reservation);
        dataAccess.returnMoneyTravelers(list, driver.getEmail());

        verify(db, atLeastOnce()).persist(any(Traveler.class));
        verify(db, atLeastOnce()).persist(any(Driver.class));
        verify(db, atLeastOnce()).persist(any(Transaction.class));
    }

    // Test 2: reserva no pagada => sin persistencias
    @Test
    public void tc02() {
        reservation.setPayed(false);
        List<Reservation> list = Arrays.asList(reservation);

        dataAccess.returnMoneyTravelers(list, driver.getEmail());
        verify(db, never()).persist(any(Transaction.class));
    }

    // Test 3: driver no encontrado => sin persistencias
    @Test
    public void tc03() {
        when(db.find(Driver.class, driver.getEmail())).thenReturn(null);
        List<Reservation> list = Arrays.asList(reservation);

        dataAccess.returnMoneyTravelers(list, driver.getEmail());
        verify(db, never()).persist(any(Transaction.class));
    }

    // Test 4: resList null => sin persistencias
    @Test
    public void tc04() {
        dataAccess.returnMoneyTravelers(null, driver.getEmail());
        verify(db, never()).persist(any());
    }

}
