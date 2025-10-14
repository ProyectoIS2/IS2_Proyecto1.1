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

import org.junit.Test;

import javax.persistence.*;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class CreateReservationMockWhiteTest {

    static DataAccess dataAccess;
    static Traveler traveler;
    static Driver driver;
    static Car car1;
    static Car car2;
    static Ride ride1; // viaje con muchos asientos
    static Ride ride2; // viaje con pocos asientos

    protected MockedStatic<Persistence> persistenceMock; //mock estatico de persistence

    @Mock
    protected EntityManagerFactory entityManagerFactory;
    @Mock
    protected EntityManager db;
    @Mock
    protected EntityTransaction et;

    @Before
    public void setUp() {
        // inicializa mocks anotados con @Mock
        MockitoAnnotations.openMocks(this);

        // Mock estático de Persistence.createEntityManagerFactory(...)
        persistenceMock = Mockito.mockStatic(Persistence.class);
        persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
                .thenReturn(entityManagerFactory);

        //configurar factoty => entity manager
        when(entityManagerFactory.createEntityManager()).thenReturn(db);
        when(db.getTransaction()).thenReturn(et);

        // Crear objetos de dominio reales
        driver = new Driver("driver1@test.com", "Driver One", "pwd");       
        traveler = new Traveler("t1@test.com", "Traveler One", "pwd");        

        car1 = new Car("1234ABC", 4, driver, false);
        car2 = new Car("5678ABC", 1, driver, false);
        
        ride1 = new Ride("A", "B", new Date(), 20.0f, driver,car1); // 4 plazas
        ride2 = new Ride("C", "D", new Date(), 30.0f, driver,car2); // 1 plaza

        // Configurar comportamiento del EntityManager simulado
        when(db.find(Ride.class, 1)).thenReturn(ride1);
        when(db.find(Ride.class, 2)).thenReturn(ride2);
        when(db.find(Traveler.class, traveler.getEmail())).thenReturn(traveler);
        when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);

        dataAccess = new DataAccess(db); // Crear SUT con entityManager simulado
    }

    @After
    public void tearDown() {
    	persistenceMock.close(); //para cerrar los mocks estaticos
    }


     // Test 1: Ride inexistente, db.find devuelve null => NullPointerException capturada internamente y método devuelve null     
    @Test
    public void test1() {
        // la búsqueda del ride devuelve null
        when(db.find(Ride.class, 99)).thenReturn(null); 

        try {
            Reservation res = dataAccess.createReservation(1, 99, traveler.getEmail());
            assertNull("Si ride == null, createReservation debe devolver null (catch NullPointerException)", res);
        } catch (Exception e) {
            fail("No debía lanzar excepción, debía devolver null. Excepción: " + e.getMessage());
        }
    }

    
     //Test 2: No hay suficientes asientos => NotEnoughAvailableSeatsException
    @Test
    public void test2() {
        // ride2 (id=2) tiene 1 plaza
        when(db.find(Ride.class, 2)).thenReturn(ride2); // Simula que cuando DataAccess intenta buscar un Ride con número 2, se devuelve el objeto ride2 ya creado.
        when(db.find(Traveler.class, traveler.getEmail())).thenReturn(traveler);

        try {
            dataAccess.createReservation(5, 2, traveler.getEmail()); //Se intenta reservar 5 plazas en ride2 que solo tiene 1
            fail("Se esperaba NotEnoughAvailableSeatsException");
        } catch (NotEnoughAvailableSeatsException expected) {
            // OK
        } catch (Exception e) {
            fail("Se esperaba NotEnoughAvailableSeatsException, se lanzó otra excepción: " + e.getMessage());
        }
    }

    
     // Test 3: Reserva ya existente => ReservationAlreadyExistException
     // Simulamos reserva previa creando una Reservation real y añadiéndola al ride.
    @Test
    public void test3() {
        // se asegura que ride1 y traveler existen
        when(db.find(Ride.class, 1)).thenReturn(ride1);
        when(db.find(Traveler.class, traveler.getEmail())).thenReturn(traveler);
        when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);

        // Crear reserva existente
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

    // Test 4: Flujo correcto -> se crea reserva valida
/**    @Test
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
    } */
}
