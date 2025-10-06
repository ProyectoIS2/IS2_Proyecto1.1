package returnMoneyTravelersTest;

import static org.junit.Assert.assertEquals;
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
import testOperations.TestDataAccess;

public class ReturnMoneyTravelersDBWhiteTest {

    private DataAccess dataAccess;
    private TestDataAccess testDA;

    private Driver driver1;
    private Traveler traveler1, traveler2;
    private Ride ride1;
    private Reservation res1, res2;

    @Before
    public void setUp() {
        dataAccess = new DataAccess();
        testDA = new TestDataAccess();

        testDA.open();
        dataAccess.open();

        try {
            // Crear usuarios usando métodos de DataAccess
            driver1 = dataAccess.createDriver("driver1@test.com", "Driver1", "123");
            traveler1 = dataAccess.createTraveler("traveler1@test.com", "Traveler1", "456");
            traveler2 = dataAccess.createTraveler("traveler2@test.com", "Traveler2", "789");
            
            // Añadir dinero usando métodos de DataAccess
            dataAccess.putMoneyTraveler("traveler1@test.com", 100);
            dataAccess.putMoneyTraveler("traveler2@test.com", 50);
            
            // Añadir coche al conductor
            dataAccess.addCarToDriver("driver1@test.com", "CAR123", 4, false);
            
            // Crear viaje
            ride1 = dataAccess.createRide("Donostia", "Bilbao", new Date(System.currentTimeMillis() + 86400000), 25, "driver1@test.com", "CAR123");
            
            // Crear reservas
            res1 = dataAccess.createReservation(2, ride1.getRideNumber(), "traveler1@test.com");
            res2 = dataAccess.createReservation(1, ride1.getRideNumber(), "traveler2@test.com");
            
            // Pagar reservas usando método pay de DataAccess
            dataAccess.pay(res1);
            dataAccess.pay(res2);
            
        } catch (Exception e) {
            fail("Error en setUp: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        // Limpiar usando solo métodos de DataAccess
        try {
            dataAccess.open();
            
            // Eliminar el ride primero (esto manejará las reservas automáticamente)
            if (ride1 != null) {
                dataAccess.removeRideDriver(ride1.getRideNumber(), "driver1@test.com");
            }
            
            // Eliminar cuentas
            dataAccess.deleteAccountTraveler("traveler1@test.com");
            dataAccess.deleteAccountTraveler("traveler2@test.com");
            dataAccess.deleteAccountDriver("driver1@test.com");
            
        } catch (Exception e) {
            // Ignorar excepciones en cleanup
        } finally {
            dataAccess.close();
            testDA.close();
        }
    }

    // Método auxiliar para invocar el método privado returnMoneyTravelers usando reflexión
    private void executeReturnMoneyTravelers(List<Reservation> resList, String driverEmail) throws Exception {
        Method method = DataAccess.class.getDeclaredMethod("returnMoneyTravelers", List.class, String.class);
        method.setAccessible(true);
        method.invoke(dataAccess, resList, driverEmail);
    }

    // Test 1: Lista vacía - no se ejecuta el bucle
    @Test
    public void test1() {
        try {
            executeReturnMoneyTravelers(Collections.emptyList(), "driver1@test.com");
            
            Traveler updatedT1 = dataAccess.getTravelerByEmail("traveler1@test.com", "456");
            Traveler updatedT2 = dataAccess.getTravelerByEmail("traveler2@test.com", "789");
            Driver updatedD1 = dataAccess.getDriverByEmail("driver1@test.com", "123");
            
            // Verificar que el dinero no cambió (después del pago inicial)
            assertEquals(50, updatedT1.getMoney(), 0.01); // 100 - 50 (del pago)
            assertEquals(25, updatedT2.getMoney(), 0.01); // 50 - 25 (del pago)
            assertEquals(75, updatedD1.getMoney(), 0.01); // 0 + 75 (del pago)
            
        } catch (Exception e) {
            fail("Error inesperado en test1: " + e.getMessage());
        }
    }

    // Test 2: Reservas no pagadas
    @Test
    public void test2() {
        try {
            // Crear una reserva no pagada
            Reservation res3 = dataAccess.createReservation(1, ride1.getRideNumber(), "traveler1@test.com");
            
            
            List<Reservation> resList = Arrays.asList(res3);
            
            executeReturnMoneyTravelers(resList, "driver1@test.com");
            
            Traveler updatedT1 = dataAccess.getTravelerByEmail("traveler1@test.com", "456");
            Driver updatedD1 = dataAccess.getDriverByEmail("driver1@test.com", "123");
            
            
            assertEquals(50, updatedT1.getMoney(), 0.01);
            assertEquals(75, updatedD1.getMoney(), 0.01);
            
        } catch (Exception e) {
            fail("Error inesperado en test2: " + e.getMessage());
        }
    }

    // Test 3: Reservas pagadas - procesamiento exitoso
    @Test
    public void test3() {
        try {
            List<Reservation> resList = Arrays.asList(res1, res2);
            
            executeReturnMoneyTravelers(resList, "driver1@test.com");
            
            Traveler updatedT1 = dataAccess.getTravelerByEmail("traveler1@test.com", "456");
            Traveler updatedT2 = dataAccess.getTravelerByEmail("traveler2@test.com", "789");
            Driver updatedD1 = dataAccess.getDriverByEmail("driver1@test.com", "123");
            
            // Verificar que el dinero fue devuelto correctamente
            // Traveler1: 50 + 50 = 100
            // Traveler2: 25 + 25 = 50  
            // Driver: 75 - 75 = 0
            assertEquals(100, updatedT1.getMoney(), 0.01);
            assertEquals(50, updatedT2.getMoney(), 0.01);
            assertEquals(0, updatedD1.getMoney(), 0.01);
            
        } catch (Exception e) {
            fail("Error inesperado en test3: " + e.getMessage());
        }
    }

    // Test 4: Driver no existe - manejo de excepción
    @Test
    public void test4() {
        try {
            List<Reservation> resList = Arrays.asList(res1);
            
            executeReturnMoneyTravelers(resList, "noexiste@test.com");
            
            Traveler updatedT1 = dataAccess.getTravelerByEmail("traveler1@test.com", "456");
            // El dinero no debe cambiar porque el driver no existe
            assertEquals(50, updatedT1.getMoney(), 0.01);
            
        } catch (Exception e) {
            fail("No debería lanzar excepción en test4: " + e.getMessage());
        }
    }

    // Test 5: Email de driver no coincide con ningún driver - manejo de NullPointerException
    @Test
    public void test5() {
        try {
            List<Reservation> resList = Arrays.asList(res1, res2);
            
            // Usar un email que no corresponde a ningún driver en la base de datos
            executeReturnMoneyTravelers(resList, "driver_que_no_existe@test.com");
            
            // Verificar que el dinero de los travelers y driver no cambió
            Traveler updatedT1 = dataAccess.getTravelerByEmail("traveler1@test.com", "456");
            Traveler updatedT2 = dataAccess.getTravelerByEmail("traveler2@test.com", "789");
            Driver updatedD1 = dataAccess.getDriverByEmail("driver1@test.com", "123");
            
            // El dinero debería permanecer igual porque el driver no existe
            assertEquals(50, updatedT1.getMoney(), 0.01);
            assertEquals(25, updatedT2.getMoney(), 0.01);
            assertEquals(75, updatedD1.getMoney(), 0.01);
            
        } catch (Exception e) {
            fail("No debería lanzar excepción en test5: " + e.getMessage());
        }
    
    }
}

 
