package returnMoneyTravelersTest;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Reservation;

public class ReturnMoneyTravelersMockWhiteTest {

    @Mock
    private DataAccess dataAccess;

    @Mock
    private Reservation res1, res2;

    public void ReturnMoneyTravelersDBWhiteTestMock() {
        MockitoAnnotations.openMocks(this);
    }

    // Test 1: Lista vacía - no se ejecuta el bucle
    @Test
    public void test1() throws Exception {
        executeReturnMoneyTravelers(Collections.emptyList(), "driver@test.com");
    }

    // Test 2: Reservas no pagadas - no se procesan
    @Test
    public void test2() throws Exception {
        List<Reservation> resList = Arrays.asList(res1, res2);
        executeReturnMoneyTravelers(resList, "driver@test.com");
    }

    // Test 3: Reservas pagadas - procesamiento exitoso
    @Test
    public void test3() throws Exception {
        List<Reservation> resList = Arrays.asList(res1, res2);
        executeReturnMoneyTravelers(resList, "driver@test.com");
    }

    // Test 4: Driver no existe
    @Test
    public void test4() throws Exception {
        List<Reservation> resList = Arrays.asList(res1);
        executeReturnMoneyTravelers(resList, "noexiste@test.com");
    }

    // Test 5: Traveler no existe
    @Test
    public void test5() throws Exception {
        List<Reservation> resList = Arrays.asList(res1, res2);
        executeReturnMoneyTravelers(resList, "driver@test.com");
    }

    // Método auxiliar para invocar el método privado
    private void executeReturnMoneyTravelers(List<Reservation> resList, String driverEmail) throws Exception {
        Method method = DataAccess.class.getDeclaredMethod("returnMoneyTravelers", List.class, String.class);
        method.setAccessible(true);
        method.invoke(dataAccess, resList, driverEmail);
    }
}
