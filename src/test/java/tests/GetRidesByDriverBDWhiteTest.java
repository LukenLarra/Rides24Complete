package tests;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;
import testOperations.TestDataAccess;

public class GetRidesByDriverBDWhiteTest {
    //sut:system under test
	static DataAccess sut=new DataAccess();
	 
	//additional operations needed to execute the test 
	static TestDataAccess testDA=new TestDataAccess();

	@SuppressWarnings("unused")
	private Driver driver; 

    @Test  
    //sut.getRidesByDriver:  The username doesnt fit any driver in the database. The test must return null. If  an Exception is returned the getRidesByDriver method is not well implemented.
    public void test1() {
        try {
            //define parameters
            String driverUsername="Driver Test";

            sut.open();
            List<Ride> result =sut.getRidesByDriver(driverUsername);
            sut.close();    

            assertNull(result);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    //sut.getRidesByDriver:  The username fits a driver in the database but it doesnt have any ride. The test must return an empty list of activeRides. If  an Exception is returned the getRidesByDriver method is not well implemented.
    public void test2() {
        String username = "Driver Test";
        try {
            // Inicializar la base de datos con datos de prueba
            sut.open();
            testDA.open();
            testDA.createDriver(username,"123"); // Añadir un conductor sin viajes
            testDA.close();

            List<Ride> result = sut.getRidesByDriver(username);
            sut.close();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        } catch (Exception e) {
            fail("An exception was thrown: " + e.getMessage());
        } finally {
            testDA.open();
            testDA.removeDriver(username); // Eliminar el conductor de prueba
            testDA.close();
        }
    }

    @Test
    //sut.getRidesByDriver:  The username fits a driver in the database and it has rides but no active ones. The test must return a list of activeRides. If  an Exception is returned the getRidesByDriver method is not well implemented.
    public void test3() {
        String username = "Driver Test";
        String from = "Donostia";
        String to = "Zarautz";
        Date date = new java.util.Date();
        try {
            // Inicializar la base de datos con datos de prueba
            sut.open();
            testDA.open();
            testDA.addDriverWithRide(username, from, to, date, 2, 10); // Añadir un viaje no activo
            testDA.setActiveRide(false); // Desactivar el viaje
            testDA.close();

            // Invoke System Under Test (sut)
            List<Ride> result = sut.getRidesByDriver(username);
            sut.close();
            // Verify the results
            assertNotNull(result);
            assertTrue(result.isEmpty());
        } catch (Exception e) {
            fail("An exception was thrown: " + e.getMessage());
        } finally {
            // Clean up the database
            testDA.open();
            testDA.removeRide(username, from, to, date); // Eliminar el viaje de prueba
            testDA.removeDriver(username); // Eliminar el conductor de prueba
            testDA.close();
        }
    }

    @Test
    //sut.getRidesByDriver:  The username fits a driver in the database and it has rides and active ones. The test must return a list of activeRides. If  an Exception is returned the getRidesByDriver method is not well implemented.
    public void test4() {
        String username = "Driver Test";
        String from = "Donostia";
        String to = "Zarautz";
        Date date = new java.util.Date();
        try {
            // Inicializar la base de datos con datos de prueba
            sut.open();
            testDA.open();
            testDA.addDriverWithRide(username, from, to, date, 2, 10); // Añadir un viaje activo
            testDA.close();

            // Invoke System Under Test (sut)
            List<Ride> result = sut.getRidesByDriver(username);
            sut.close();
            // Verify the results
            assertNotNull(result);
            assertTrue(!result.isEmpty());
        } catch (Exception e) {
            fail("An exception was thrown: " + e.getMessage());
        } finally {
            // Clean up the database
            testDA.open();
            testDA.removeRide(username, from, to, date); // Eliminar el viaje de prueba
            testDA.removeDriver(username); // Eliminar el conductor de prueba
            testDA.close();
        }
    }


}
