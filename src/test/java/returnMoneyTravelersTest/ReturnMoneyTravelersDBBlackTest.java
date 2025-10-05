package returnMoneyTravelersTest;

import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Reservation;
import domain.Ride;
import domain.Traveler;

public class ReturnMoneyTravelersDBBlackTest {

    private DataAccess dataAccess;
    private Driver driver;
    private Traveler traveler;
    private Ride ride;
    private Reservation reservation;

    @Before
    public void setUp() {
        dataAccess = new DataAccess();
        dataAccess.open();

        try {
            // Crear solo 1 driver, 1 traveler, 1 ride
            driver = dataAccess.createDriver("driver@test.com", "Driver", "123");
            traveler = dataAccess.createTraveler("traveler@test.com", "Traveler", "456");
            dataAccess.addCarToDriver("driver@test.com", "CAR123", 4, false);
            
            ride = dataAccess.createRide("Donostia", "Bilbao", 
                                       new Date(System.currentTimeMillis() + 86400000), 
                                       25, "driver@test.com", "CAR123");
            
        } catch (Exception e) {
            fail("Error en setUp: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        try {
            dataAccess.open();
            if (ride != null) {
                dataAccess.removeRideDriver(ride.getRideNumber(), "driver@test.com");
            }
            dataAccess.deleteAccountTraveler("traveler@test.com");
            dataAccess.deleteAccountDriver("driver@test.com");
        } catch (Exception e) {
        } finally {
            dataAccess.close();
        }
    }

    private void executeReturnMoneyTravelers(List<Reservation> resList, String driverEmail) throws Exception {
        Method method = DataAccess.class.getDeclaredMethod("returnMoneyTravelers", List.class, String.class);
        method.setAccessible(true);
        method.invoke(dataAccess, resList, driverEmail);
    }

    // Test 1: d ∈ DB ∧ r ∈ resList ∧ isPayed = True
    @Test
    public void testDriverInDB_ReservationInList_IsPayedTrue() throws Exception {
        // Configurar: crear y pagar reserva
        reservation = dataAccess.createReservation(2, ride.getRideNumber(), "traveler@test.com");
        dataAccess.pay(reservation);
        
        // Ejecutar - test pasa si no se lanza excepción
        executeReturnMoneyTravelers(Arrays.asList(reservation), "driver@test.com");
    }

    // Test 2: d ∈ DB ∧ r ∈ resList ∧ isPayed = False
    @Test
    public void testDriverInDB_ReservationInList_IsPayedFalse() throws Exception {
        // Configurar: crear reserva pero NO pagar
        reservation = dataAccess.createReservation(2, ride.getRideNumber(), "traveler@test.com");
        // NO llamar a dataAccess.pay(reservation)
        
        // Ejecutar - test pasa si no se lanza excepción
        executeReturnMoneyTravelers(Arrays.asList(reservation), "driver@test.com");
    }

    // Test 3: d ∉ DB ∧ r ∈ resList
    @Test
    public void testDriverNotInDB_ReservationInList() throws Exception {
        // Configurar: crear y pagar reserva
        reservation = dataAccess.createReservation(2, ride.getRideNumber(), "traveler@test.com");
        dataAccess.pay(reservation);
        
        // Ejecutar con driver que no existe - test pasa si no se lanza excepción
        executeReturnMoneyTravelers(Arrays.asList(reservation), "noexiste@test.com");
    }

    // Test 4: d ∈ DB ∧ r ∉ resList (lista vacía)
    @Test
    public void testDriverInDB_ReservationNotInList() throws Exception {
        // Ejecutar con lista vacía - test pasa si no se lanza excepción
        executeReturnMoneyTravelers(Collections.emptyList(), "driver@test.com");
    }

    // Test 5: d ∉ DB ∧ r ∉ resList (lista vacía)
    @Test
    public void testDriverNotInDB_ReservationNotInList() throws Exception {
        // Ejecutar con driver que no existe y lista vacía - test pasa si no se lanza excepción
        executeReturnMoneyTravelers(Collections.emptyList(), "noexiste@test.com");
    }
}	
