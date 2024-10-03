package tests;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

import data_access.DataAccess;
import domain.*;
import testOperations.TestDataAccess;
public class BookRideBDWhiteTest {
     //sut:system under test
	 static DataAccess sut=new DataAccess();
	 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();

	@SuppressWarnings("unused")
    //initialize a traveler in the database
    private Traveler traveler = new Traveler("Traveler Test", "1234");

    private int seats = 2;

    private double price = 10;

    private double desk = 5;

    private Ride ride = new Ride("City Test1", "City Test2", new Date(), 4,  price, new Driver("Driver Test", "1234"));

    @Test
    //sut.bookRide:  The ride is null. The test must return false, as an exception was caught.
    public void test1() {
        boolean result=false;
        try {
            //invoke System Under Test (sut)  
            sut.open();
            result=sut.bookRide(traveler.getUsername(), null, seats, desk);
            sut.close();
            //verify the result
            assertFalse(result);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    //sut.bookRide:  The traveler is not in the database. The test must return false.
    public void test2() {
        boolean result=false;
        try {
            //invoke System Under Test (sut)  
            sut.open();
            result=sut.bookRide("Traveler Test2", ride, seats, desk);
            sut.close();
            //verify the result
            assertFalse(result);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    //sut.bookRide:  The traveler is in the database, the ride is not null, the number of seats left in the ride is less than the number of seats requested by the traveler. The test must return false.
    public void test3() {
        boolean result=false;
        try {
            //invoke System Under Test (sut)  
            sut.open();
            testDA.open();
            testDA.createTraveler(traveler);
            result=sut.bookRide(traveler.getUsername(), ride, seats+1, desk);
            sut.close();
            testDA.close();
            //verify the result
            assertFalse(result);
        } catch (Exception e) {
            fail();
        } finally {
            try {
                testDA.open();
                testDA.removeTraveler(traveler.getUsername());
                testDA.close();
            } catch (Exception e) {
                fail();
            }
        }
    }

    @Test
    //sut.bookRide:  The traveler is in the database, the ride is not null, the number of seats left in the ride is greater than or equal to the number of seats requested by the traveler, but the price of the ride even with the discount is greater than the amount of money the traveler has. The test must return false.
    public void test4() {
        boolean result=false;
        try {
            //invoke System Under Test (sut)  
            sut.open();
            testDA.open();
            traveler.setMoney(0);
            testDA.createTraveler(traveler);
            result=sut.bookRide(traveler.getUsername(), ride, seats, 0);
            sut.close();
            testDA.close();
            //verify the result
            assertFalse(result);
        } catch (Exception e) {
            fail();
        } finally {
            try {
                testDA.open();
                testDA.removeTraveler(traveler.getUsername());
                testDA.close();
                traveler.setMoney(10);
            } catch (Exception e) {
                fail();
            }
        }   
    }

    @Test
    //sut.bookRide:  The traveler is in the database, the ride is not null, the number of seats left in the ride is greater than or equal to the number of seats requested by the traveler, the price of the ride even with the discount is less than or equal to the amount of money the traveler has. The test must return true.
    public void test5() {
        boolean result=false;
        try {
            //invoke System Under Test (sut)  
            traveler.setMoney(15);
            testDA.open();
            testDA.createTraveler(traveler);
            testDA.addDriverWithRide("Driver Test", ride.getFrom(), ride.getTo(), ride.getDate(), ride.getnPlaces(), (float) ride.getPrice());
            testDA.close();
            sut.open();
            result=sut.bookRide(traveler.getUsername(), ride, seats, desk);
            sut.close();
            
            //verify the result
            assertTrue(result);
        } catch (Exception e) {
            fail();
        } finally {
            try {
                testDA.open();
                testDA.removeTraveler(traveler.getUsername());
                testDA.removeRide("Driver Test", ride.getFrom(), ride.getTo(), ride.getDate());
                testDA.removeDriver("Driver Test");
                testDA.close();
            } catch (Exception e) {
                fail();
            }
        }
    }


    
}
