package createReservationTest;

import dataAccess.DataAccess;
import domain.Car;
import domain.Driver;
import domain.Reservation;
import domain.Ride;
import domain.Traveler;
import exceptions.NotEnoughAvailableSeatsException;
import exceptions.ReservationAlreadyExistException;
import org.junit.*;
import org.mockito.*;

import javax.persistence.*;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Mocked white-box tests for DataAccess.createReservation(...)
 * - Uses real domain objects (Ride, Traveler, Driver)
 * - Mocks persistence (EntityManager, EntityTransaction, EntityManagerFactory)
 */
public class CreateReservationMockWhiteTest {

    static DataAccess dataAccess;           // SUT (constructed with mocked EntityManager)
    static Traveler traveler;
    static Driver driver;
    static Car car1;
    static Car car2;
    static Ride ride1; // ride with plenty of seats
    static Ride ride2; // ride with few seats

    protected MockedStatic<Persistence> persistenceMock;

    @Mock
    protected EntityManagerFactory entityManagerFactory;
    @Mock
    protected EntityManager db;
    @Mock
    protected EntityTransaction et;

    @Before
    public void setUp() {
        // init mocks annotated with @Mock
        MockitoAnnotations.openMocks(this);

        // mock static Persistence.createEntityManagerFactory(...) (keeps pattern from your example)
        persistenceMock = Mockito.mockStatic(Persistence.class);
        persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
                .thenReturn(entityManagerFactory);

        // entityManagerFactory -> entityManager (not strictly needed since we pass db to DataAccess constructor,
        // but kept to mimic your example)
        when(entityManagerFactory.createEntityManager()).thenReturn(db);
        // db.getTransaction() returns our mocked transaction
        when(db.getTransaction()).thenReturn(et);

        // Create real domain objects using the constructors from your project
        driver = new Driver("driver1@test.com", "Driver One", "pwd");          // (email, name, password)
        traveler = new Traveler("t1@test.com", "Traveler One", "pwd");        // (email, name, password)

        car1 = new Car("1234ABC", 4, driver, false);
        car2 = new Car("5678ABC", 1, driver, false);
        // rides constructed with (from, to, date, nPlaces, price, driver)
        ride1 = new Ride("A", "B", new Date(), 20.0f, driver,car1); // plenty of seats
        ride2 = new Ride("C", "D", new Date(), 30.0f, driver,car2); // only 1 seat

        when(db.find(Ride.class, 1)).thenReturn(ride1);
        when(db.find(Ride.class, 2)).thenReturn(ride2);
        when(db.find(Traveler.class, traveler.getEmail())).thenReturn(traveler);
        when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);

        // Build SUT using the mocked EntityManager
        dataAccess = new DataAccess(db);
    }

    @After
    public void tearDown() {
        // close the static mock created for Persistence
        persistenceMock.close();
        // no real DB resources to close because we passed mocked EntityManager
    }

    /**
     * Caso 1: Ride inexistente -> db.find(Ride.class,id) devuelve null => NPE capturada y método devuelve null
     */
    @Test
    public void test1() {
        // Arrange: la búsqueda del ride devuelve null
        when(db.find(Ride.class, 99)).thenReturn(null);

        try {
            Reservation res = dataAccess.createReservation(1, 99, traveler.getEmail());
            assertNull("Si ride == null, createReservation debe devolver null (catch NullPointerException)", res);
        } catch (Exception e) {
            fail("No debía lanzar excepción, debía devolver null. Excepción: " + e.getMessage());
        }
    }

    /**
     * Caso 2: NotEnoughAvailableSeatsException
     * - ride2 tiene 1 plaza, solicitamos 5 -> excepción.
     */
    @Test
    public void test2() {
        // Arrange: ride2 (id=2) tiene 1 plaza (ya configurado en setUp)
        when(db.find(Ride.class, 2)).thenReturn(ride2);
        when(db.find(Traveler.class, traveler.getEmail())).thenReturn(traveler);

        try {
            dataAccess.createReservation(5, 2, traveler.getEmail());
            fail("Se esperaba NotEnoughAvailableSeatsException");
        } catch (NotEnoughAvailableSeatsException expected) {
            // OK
        } catch (Exception e) {
            fail("Se esperaba NotEnoughAvailableSeatsException, se lanzó otra excepción: " + e.getMessage());
        }
    }

    /**
     * Caso 3: ReservationAlreadyExistException
     * Simulamos reserva previa creando una Reservation real y añadiéndola al ride.
     */
    @Test
    public void test3() {
        // Arrange: ride1 (id=1) tiene suficiente plazas.
        when(db.find(Ride.class, 1)).thenReturn(ride1);
        when(db.find(Traveler.class, traveler.getEmail())).thenReturn(traveler);
        when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);

        // Creamos una reserva real y la añadimos a ride1 para simular que ya existe
        Reservation existing = traveler.makeReservation(ride1, 2);
        driver.addReservation(existing);
        ride1.addReservation(existing);

        try {
            dataAccess.createReservation(2, 1, traveler.getEmail());
            fail("Se esperaba ReservationAlreadyExistException");
        } catch (ReservationAlreadyExistException expected) {
            // OK
        } catch (Exception e) {
            fail("Se esperaba ReservationAlreadyExistException, se lanzó otra excepción: " + e.getMessage());
        }
    }

    /**
     * Caso 4: Flujo correcto -> reserva creada y retornada
     */
    @Test
    public void test4() {
        // Arrange: ride1 (id=1) sin reservas previas
        when(db.find(Ride.class, 1)).thenReturn(ride1);
        when(db.find(Traveler.class, traveler.getEmail())).thenReturn(traveler);
        when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);

        try {
            Reservation res = dataAccess.createReservation(2, 1, traveler.getEmail());
            assertNotNull("Se esperaba una Reservation creada", res);
            assertEquals("Ride asociado debe ser ride1", ride1, res.getRide());
            assertEquals("Traveler asociado debe ser traveler", traveler, res.getTraveler());
        } catch (Exception e) {
            fail("No se esperaba excepción en flujo correcto: " + e.getMessage());
        }
    }
}
