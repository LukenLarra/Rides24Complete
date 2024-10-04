package tests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import data_access.DataAccess;
import domain.*;

public class BookRideMockBlackTest {

    static DataAccess sut;

    protected MockedStatic<Persistence> persistenceMock;

    @Mock
    protected EntityManagerFactory entityManagerFactory;
    @Mock
    protected EntityManager db;
    @Mock
    protected EntityTransaction et;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
        persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
                .thenReturn(entityManagerFactory);

        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
        Mockito.doReturn(et).when(db).getTransaction();
        sut = new DataAccess(db);
    }

    @After
    public void tearDown() {
        persistenceMock.close();
    }


    
    //sut.bookRide:  The traveler is null. The test must return false, as an exception was caught.
    @Test
    public void test1() {
        boolean result=false;
        Ride ride = new Ride("City Test1", "City Test2", new Date(), 4, 10, new Driver("Driver Test", "1234"));
        int seats = 2;
        double desk = 5;
        try {
            // configure the state through mocks
            Mockito.when(db.find(Ride.class, ride.getRideNumber())).thenReturn(ride);

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


    // sut.bookRide: The ride is null. The test must return false, as an exception
    // was caught.
    @Test
    public void test2() {
        boolean result = false;
        
        Traveler traveler = new Traveler("Traveler Test", "1234");

        int seats = 2;

        double desk = 5;

        try {
            // configure the state through mocks
            Mockito.when(db.find(Traveler.class, traveler.getUsername())).thenReturn(traveler);

            // invoke System Under Test (sut)
            sut.open();
            result = sut.bookRide(traveler.getUsername(), null, seats, desk);
            sut.close();
            // verify the result
            assertFalse(result);
        } catch (Exception e) {
            fail();
        }
    }

    // sut.bookRide: The traveler is not in the database. The test must return false.
    @Test
    public void test3() {
        boolean result = false;

        int seats = 2;

        double desk = 5;

        Ride ride = new Ride("City Test1", "City Test2", new Date(), 4, 10, new Driver("Driver Test", "1234"));

        try {
            // configure the state through mocks
            Mockito.when(db.find(Traveler.class, "Traveler Test")).thenReturn(null);

            // invoke System Under Test (sut)
            sut.open();
            result = sut.bookRide("Traveler Test", ride, seats, desk);
            sut.close();
            // verify the result
            assertFalse(result);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    //sut.bookRide:  The ride is not in the database. The test must return false.
    public void test4() {
        boolean result=false;
        Traveler traveler = new Traveler("Traveler Test", "1234");
        int seats = 2;
        double price = 10;
        double desk = 5;

        try {

            //configure the state through mocks
            Mockito.when(db.find(Traveler.class, traveler.getUsername())).thenReturn(traveler);
            Mockito.when(db.find(Ride.class, "City Test1City Test2")).thenReturn(null);

            //invoke System Under Test (sut)  
            sut.open();
            Ride ride2 = new Ride("City Test1", "City Test2", new Date(), seats,  price, new Driver("Driver Test2", "1234"));
            result=sut.bookRide("Traveler Test", ride2, seats, desk);
            sut.close();
            //verify the result
            assertFalse(result);
        } catch (Exception e) {
            fail();
        }
    }

    // sut.bookRide: The traveler is in the database, the ride is not null, the number of seats left in the ride is less than the number of seats requested by the traveler. The test must return false.
    @Test
    public void test5() {
        boolean result = false;

        Traveler traveler = new Traveler("Traveler Test", "1234");

        int seats = 2;

        double desk = 5;

        Ride ride = new Ride("City Test1", "City Test2", new Date(), 4, 10, new Driver("Driver Test", "1234"));

        try {
            // configure the state through mocks
            Mockito.when(db.find(Traveler.class, traveler.getUsername())).thenReturn(traveler);
            Mockito.when(db.find(Ride.class, ride.getRideNumber())).thenReturn(ride);

            // invoke System Under Test (sut)
            sut.open();
            result = sut.bookRide(traveler.getUsername(), ride, seats + 1, desk);
            sut.close();
            // verify the result
            assertFalse(result);
        } catch (Exception e) {
            fail();
        }
    }

   
    //sut.bookRide:  The traveler is in the database, the ride is not null, the number of seats left in the ride is greater than or equal to the number of seats requested by the traveler, but the price of the ride even with the discount is greater than the amount of money the traveler has. The test must return false.
    @Test
    public void test6() {
        boolean result=false;
        Traveler traveler = new Traveler("Traveler Test", "1234");
            int seats = 2;
            double price = 10;
            double desk = 5;
            Ride ride = new Ride("City Test1", "City Test2", new Date(), 4,  price, new Driver("Driver Test", "1234"));
            
        try {
            //configure the state through mocks
            Mockito.when(db.find(Traveler.class, traveler.getUsername())).thenReturn(traveler);
            Mockito.when(db.find(Ride.class, ride.getRideNumber())).thenReturn(ride);
        
            //invoke System Under Test (sut)  
            sut.open();
            result=sut.bookRide(traveler.getUsername(), ride, seats, desk);
            sut.close();
            //verify the result
            assertFalse(result);
        } catch (Exception e) {
            fail();
        }   
    }

    // sut.bookRide: The traveler is in the database, the ride is not null, the number of seats left in the ride is greater than the number of seats requested by the traveler. The test must return true.
    @Test
    public void test7() {
        boolean result = false;

        Traveler traveler = new Traveler("Traveler Test", "1234");

        int seats = 2;

        double desk = 5;

        Ride ride = new Ride("City Test1", "City Test2", new Date(), 4, 10, new Driver("Driver Test", "1234"));

        try {
            // configure the state through mocks
            Mockito.when(db.find(Traveler.class, traveler.getUsername())).thenReturn(traveler);
            Mockito.when(db.find(Ride.class, ride.getRideNumber())).thenReturn(ride);

            // invoke System Under Test (sut)
            sut.open();
            result = sut.bookRide(traveler.getUsername(), ride, seats, desk);
            sut.close();
            // verify the result
            assertTrue(result);
        } catch (Exception e) {
            fail();
        }
    }

}

