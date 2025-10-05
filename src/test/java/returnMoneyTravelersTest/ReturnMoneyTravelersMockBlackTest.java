package returnMoneyTravelersTest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Reservation;
import domain.Ride;
import domain.Traveler;

public class ReturnMoneyTravelersMockBlackTest {

    @Mock
    private DataAccess dataAccess;

    @Mock
    private Driver driver;

    @Mock
    private Traveler traveler;

    @Mock
    private Ride ride;

    @Mock
    private Reservation reservation;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private void executeReturnMoneyTravelers(List<Reservation> resList, String driverEmail) throws Exception {
        Method method = DataAccess.class.getDeclaredMethod("returnMoneyTravelers", List.class, String.class);
        method.setAccessible(true);
        method.invoke(dataAccess, resList, driverEmail);
    }

    // Test 1: d ∈ DB ∧ r ∈ resList ∧ isPayed = True
    @Test
    public void testDriverInDB_ReservationInList_IsPayedTrue() throws Exception {
        // Configurar mocks
        when(reservation.isPayed()).thenReturn(true);
        when(reservation.getTraveler()).thenReturn(traveler);
        when(reservation.getCost()).thenReturn((float) 50);
        when(reservation.getDriver()).thenReturn(driver);
        
        when(traveler.getEmail()).thenReturn("traveler@test.com");
        when(driver.getEmail()).thenReturn("driver@test.com");
        
        // Configurar que driver y traveler existen en BD
        when(dataAccess.getDriverByEmail("driver@test.com", anyString())).thenReturn(driver);
        when(dataAccess.getTravelerByEmail("traveler@test.com", anyString())).thenReturn(traveler);

        // Ejecutar
        executeReturnMoneyTravelers(Arrays.asList(reservation), "driver@test.com");
    }

    // Test 2: d ∈ DB ∧ r ∈ resList ∧ isPayed = False
    @Test
    public void testDriverInDB_ReservationInList_IsPayedFalse() throws Exception {
        // Configurar mocks
        when(reservation.isPayed()).thenReturn(false);
        when(reservation.getTraveler()).thenReturn(traveler);
        when(reservation.getDriver()).thenReturn(driver);
        
        when(traveler.getEmail()).thenReturn("traveler@test.com");
        when(driver.getEmail()).thenReturn("driver@test.com");
        
        // Configurar que driver y traveler existen en BD
        when(dataAccess.getDriverByEmail("driver@test.com", anyString())).thenReturn(driver);
        when(dataAccess.getTravelerByEmail("traveler@test.com", anyString())).thenReturn(traveler);

        // Ejecutar
        executeReturnMoneyTravelers(Arrays.asList(reservation), "driver@test.com");
    }

    // Test 3: d ∉ DB ∧ r ∈ resList
    @Test
    public void testDriverNotInDB_ReservationInList() throws Exception {
        // Configurar mocks
        when(reservation.isPayed()).thenReturn(true);
        when(reservation.getTraveler()).thenReturn(traveler);
        when(reservation.getCost()).thenReturn((float) 50);
        
        when(traveler.getEmail()).thenReturn("traveler@test.com");

        // Configurar que driver NO existe (lanza excepción)
        when(dataAccess.getDriverByEmail("noexiste@test.com", anyString()))
            .thenThrow(new exceptions.UserDoesNotExistException("Driver no existe"));

        // Configurar que traveler existe
        when(dataAccess.getTravelerByEmail("traveler@test.com", anyString())).thenReturn(traveler);

        // Ejecutar
        executeReturnMoneyTravelers(Arrays.asList(reservation), "noexiste@test.com");
    }

    // Test 4: d ∈ DB ∧ r ∉ resList (lista vacía)
    @Test
    public void testDriverInDB_ReservationNotInList() throws Exception {
        // Configurar que driver existe
        when(dataAccess.getDriverByEmail("driver@test.com", anyString())).thenReturn(driver);

        // Ejecutar con lista vacía
        executeReturnMoneyTravelers(Collections.emptyList(), "driver@test.com");
    }

    // Test 5: d ∉ DB ∧ r ∉ resList (lista vacía)
    @Test
    public void testDriverNotInDB_ReservationNotInList() throws Exception {
        // Configurar que driver NO existe
        when(dataAccess.getDriverByEmail("noexiste@test.com", anyString()))
            .thenThrow(new exceptions.UserDoesNotExistException("Driver no existe"));

        // Ejecutar con lista vacía
        executeReturnMoneyTravelers(Collections.emptyList(), "noexiste@test.com");
    }
}
