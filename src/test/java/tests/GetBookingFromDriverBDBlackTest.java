package tests;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

import data_access.DataAccess;
import domain.Booking;
import domain.Driver;
import testOperations.TestDataAccess;

public class GetBookingFromDriverBDBlackTest {
    //sut:system under test
	static DataAccess sut=new DataAccess();
	 
	//additional operations needed to execute the test 
	static TestDataAccess testDA=new TestDataAccess();

	@SuppressWarnings("unused")
	private Driver driver; 

    @Test  
    //sut.getBookingFromDriver:  The username doesnt fit any driver in the database. The test must return null. If an Exception is returned the getBoookingFromDriver method is not well implemented.
    public void test1() {
        try {

            //define parameters
            String driverUsername="DTest";

            sut.open();
            List<Booking> result =sut.getBookingFromDriver(driverUsername);
            sut.close();    

            assertNull(result);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    //sut.getBookingFromDriver:  The username fits a driver in the database but it doesnt have any ride. The test must return an empty list of bookings. If an Exception is returned the getBookingFromDriver method is not well implemented.
    public void test2() {
        String username = "DTest";
        try {

            // Inicializar la base de datos con datos de prueba
            sut.open();
            testDA.open();
            testDA.createDriver(username,"123"); // Añadir un conductor sin viajes
            testDA.close();

            List<Booking> result = sut.getBookingFromDriver(username);
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
    //sut.getBookingFromDriver:  The username fits a driver in the database and it has rides but no active ones. The test must return a list of empty bookings. If an Exception is returned the getBookingFromDriver method is not well implemented.
    public void test3() {
        String username = "DTest";
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
            List<Booking> result = sut.getBookingFromDriver(username);
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
    //sut.getBookingFromDriver:  The username fits a driver in the database and it has active rides. The test must return a list of bookings. If an Exception is returned the getBookingFromDriver method is not well implemented.
    public void test4() {

        String username = "DTest";
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
            List<Booking> result = sut.getBookingFromDriver(username);
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

    @Test
    //sut.getRidesByDriver:  The username is null. The test must return null. If  an Exception is returned the getBookingFromDriver method is not well implemented.
    public void test5() {
        try {
            //define parameters
            String username=null;

            //invoke System Under Test (sut)  
            sut.open();
            List<Booking> result = sut.getBookingFromDriver(username);
            sut.close();   
            //verify the results
            assertNull(result);
        } catch (Exception e) {
            fail();
        }
    }
}