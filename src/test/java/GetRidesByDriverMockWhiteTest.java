import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Driver;
import domain.Ride;


public class GetRidesByDriverMockWhiteTest {
    
    static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;

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
    //sut.getRidesByDriver: The username doesnt fit any driver in the database. The test must return null. If  an Exception is returned the getRidesByDriver method is not well implemented.
    public void test1() {
        try {
            String username="Driver Test";

            Mockito.when(db.find(Driver.class, username)).thenReturn(null);
            
            sut.open();
            List<Ride> result = sut.getRidesByDriver(username);
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
            
            driver = new Driver(username, "123");

            Mockito.when(db.find(Driver.class, username)).thenReturn(driver);
            
            sut.open();
            List<Ride> result = sut.getRidesByDriver(username);
            sut.close();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    // sut.getRidesByDriver: The username fits a driver in the database and it has rides but no active ones. The test must return a list of activeRides. If an Exception is returned the getRidesByDriver method is not well implemented.
    public void test3() {
       
        
    }

    @Test
    //sut.getRidesByDriver:  The username fits a driver in the database and it has rides and active ones. The test must return a list of activeRides. If  an Exception is returned the getRidesByDriver method is not well implemented.
    public void test4() {
    
    
    }
}
