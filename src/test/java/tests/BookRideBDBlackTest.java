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
public class BookRideBDBlackTest {

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

    private Ride ride = new Ride("City Test1", "City Test2", new Date(), seats,  price, new Driver("Driver Test", "1234"));

    
    @Test
    //sut.bookRide:  The traveler is null. The test must return false, as an exception was caught.
    public void test1() {
        boolean result=false;
        try {
            //invoke System Under Test (sut)  
            sut.open();
            result=sut.bookRide(null, ride, seats, desk);
            sut.close();
            //verify the result
            assertFalse(result);
        } catch (Exception e) {
            fail();
        }
    }
    
    
    @Test
    //sut.bookRide:  The ride is null. The test must return false, as an exception was caught.
    public void test2() {
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
    public void test5() {
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
    //sut.bookRide:  The ride is not in the database. The test must return false.
    public void test6() {
        boolean result=false;
        try {
            //invoke System Under Test (sut)  
            sut.open();
            Ride ride2 = new Ride("City Test1", "City Test2", new Date(), seats,  price, new Driver("Driver Test2", "1234"));
            result=sut.bookRide("Traveler Test2", ride2, seats, desk);
            sut.close();
            //verify the result
            assertFalse(result);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    //sut.bookRide:  The traveler is in the database, the ride is not null, the number of seats left in the ride is less than the number of seats requested by the traveler. The test must return false.
    public void test7() {
        boolean result=false;
        try {
            //invoke System Under Test (sut)  
            sut.open();
            result=sut.bookRide(traveler.getUsername(), ride, seats+1, desk);
            sut.close();
            //verify the result
            assertFalse(result);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    //sut.bookRide:  The traveler is in the database, the ride is not null, the number of seats left in the ride is greater than or equal to the number of seats requested by the traveler, but the price of the ride even with the discount is greater than the amount of money the traveler has. The test must return false.
    public void test8() {
        boolean result=false;
        try {
            //invoke System Under Test (sut)  
            sut.open();
            result=sut.bookRide(traveler.getUsername(), ride, seats, 0);
            sut.close();
            //verify the result
            assertTrue(result==false);
        } catch (Exception e) {
            fail();
        }
    }

  

}
