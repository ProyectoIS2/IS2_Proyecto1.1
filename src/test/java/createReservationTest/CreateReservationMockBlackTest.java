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
import org.mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CreateReservationMockBlackTest {

    static DataAccess dataAccess;
    static Traveler traveler;
    static Driver driver;
    static Car car;
    static Ride ride;

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

        persistenceMock = Mockito.mockStatic(Persistence.class);
        persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
                .thenReturn(emf);

        when(emf.createEntityManager()).thenReturn(db);
        when(db.getTransaction()).thenReturn(et);

        driver = new Driver("driver@test.com", "Driver", "pwd");
        traveler = new Traveler("t1@test.com", "Traveler", "pwd");
        car = new Car("1234ABC", 4, driver, false);
        ride = new Ride("A", "B", new Date(), 20.0f, driver, car);

        when(db.find(Ride.class, 1)).thenReturn(ride);
        when(db.find(Traveler.class, traveler.getEmail())).thenReturn(traveler);
        when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);

        dataAccess = new DataAccess(db);
    }

    @After
    public void tearDown() {
        persistenceMock.close();
    }

    // Test 1: todas las entradas válidas → reserva creada
    @Test
    public void tc01() throws Exception {
        Reservation res = dataAccess.createReservation(1, 1, traveler.getEmail());
        assertNotNull(res);
    }

    // Test 2: hm <= 0
    @Test
    public void tc02() throws Exception {
        Reservation res = dataAccess.createReservation(0, 1, traveler.getEmail());
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
        Reservation res = dataAccess.createReservation(1, -10, traveler.getEmail());
        assertNull(res);
    }

    // Test 5: ride ∉ BD
    @Test
    public void tc05() throws Exception {
        when(db.find(Ride.class, 999)).thenReturn(null);
        Reservation res = dataAccess.createReservation(1, 999, traveler.getEmail());
        assertNull(res);
    }

    // Test 6: travelerEmail == null
    @Test
    public void tc06() throws Exception {
        Reservation res = dataAccess.createReservation(1, 1, null);
        assertNull(res);
    }

    // Test 7: traveler ∉ BD
    @Test
    public void tc07() throws Exception {
        when(db.find(Traveler.class, "otro@test.com")).thenReturn(null);
        Reservation res = dataAccess.createReservation(1, 1, "otro@test.com");
        assertNull(res);
    }

    // Test 8: reserva ya existente
    @Test(expected = ReservationAlreadyExistException.class)
    public void tc08() throws Exception {
        dataAccess.createReservation(1, 1, traveler.getEmail());
        dataAccess.createReservation(1, 1, traveler.getEmail()); // segunda → excepción
    }

    // Test 9: no hay suficientes plazas
    @Test(expected = NotEnoughAvailableSeatsException.class)
    public void tc09() throws Exception {
        dataAccess.createReservation(10, 1, traveler.getEmail());
    }
}
