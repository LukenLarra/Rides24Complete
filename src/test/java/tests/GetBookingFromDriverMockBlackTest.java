package tests;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import data_access.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;

@SuppressWarnings("unchecked")
public class GetBookingFromDriverMockBlackTest {
    
    static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
    @Mock
    TypedQuery<Driver> query;


    @Before
    public  void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any())).thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut=new DataAccess(db);
    }

    @After
    public  void tearDown() {
		persistenceMock.close();
    }

    Driver driver;

    @Test
    //sut.getBookingFromDriver: The username doesnt fit any driver in the database. The test must return null. If  an Exception is returned the getBookingFromDriver method is not well implemented.
    public void test1() {
        try {
            String username="Driver Test";

            when(db.createQuery("SELECT d FROM Driver d WHERE d.username = :username", Driver.class)).thenReturn(query);
            when(query.setParameter("username", username)).thenReturn(query);  // Simular que el parámetro se establece correctamente
            when(query.getSingleResult()).thenReturn(null);  // Simular que no se ha encontrado ningún Driver con ese username
            
            sut.open();
            List<Booking> result = sut.getBookingFromDriver(username);
            sut.close();

            assertNull(result);
        } catch (Exception e) {
            fail();
        }
    }
	
    @Test
    //sut.getBookingFromDriver:  The username fits a driver in the database but it doesnt have any ride. The test must return an empty list of bookings. If  an Exception is returned the getBookingFromDriver method is not well implemented.
    public void test2() {
        try {
            String username="Driver Test";
            driver = new Driver(username, "123");

            Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(query);		
            Mockito.when(query.getSingleResult()).thenReturn(driver);  // Simular que se ha encontrado un Driver con ese username
            
            sut.open();
            List<Booking> result = sut.getBookingFromDriver(username);
            sut.close();

            assertNotNull(result);
            assertTrue(result.isEmpty()); 
        } catch (Exception e) {
            fail();
        }   
    }

    @Test
    // sut.getBookingFromDriver: The username fits a driver in the database and it has rides but no active ones. The test must return a list of bookings. If an Exception is returned the getBookingFromDriver method is not well implemented.
    public void test3() {
        try {
            String username="Driver Test";
            String from = "Donostia";
            String to = "Zarautz";
            Date date = new java.util.Date();
            driver = new Driver(username, "123");
            List<Ride> rides = new ArrayList<>();
            Ride ride = new Ride(from, to, date, 3, 10.0, driver);
            ride.setActive(false);
            rides.add(ride);
            driver.setCreatedRides(rides);
            
            Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(query);		
            Mockito.when(query.getSingleResult()).thenReturn(driver);  // Simular que se ha encontrado un Driver con ese username
            
            sut.open();
            List<Booking> result = sut.getBookingFromDriver(username);
            sut.close();

            assertNotNull(result);
            assertTrue(result.isEmpty()); 
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    //sut.getBookingFromDriver:  The username fits a driver in the database and it has rides and active ones. The test must return a list of bookings. If  an Exception is returned the getBookingFromDriver method is not well implemented.
    public void test4() {
        try {
            String username="Driver Test";
            String from = "Donostia";
            String to = "Zarautz";
            Date date = new java.util.Date();
            Traveler traveler = new Traveler(username, "password");
            driver = new Driver(username, "123");

            Ride ride = driver.addRide(from, to, date, 2, 10);
            Booking booking = new Booking(ride, traveler, 1);
            List<Booking> bookings = new java.util.ArrayList<Booking>();
            bookings.add(booking);
            ride.setBookings(bookings);
            
            Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(query);		
            Mockito.when(query.getSingleResult()).thenReturn(driver);  // Simular que se ha encontrado un Driver con ese username
            
            sut.open();
            List<Booking> result = sut.getBookingFromDriver(username);
            sut.close();

            assertNotNull(result);
            assertTrue(!result.isEmpty()); 
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    //sut.getBookingFromDriver:  The username is null. The test must return null. If an Exception is returned the getBookingFromDriver method is not well implemented.
    public void test5() {
        try {
            String username=null;

            Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(query);		
            Mockito.when(query.getSingleResult()).thenReturn(null);  // Simular que se ha encontrado un Driver con ese username
            
            sut.open();
            List<Booking> result = sut.getBookingFromDriver(username);
            sut.close();
            assertNull(result);

        } catch (Exception e) {
            fail();
        }
    }
}
