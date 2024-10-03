package testOperations;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import configuration.ConfigXML;
import domain.Driver;
import domain.Ride;
import domain.Booking;
import domain.Traveler;


public class TestDataAccess {
	protected  EntityManager  db;
	protected  EntityManagerFactory emf;

	ConfigXML  c=ConfigXML.getInstance();


	public TestDataAccess()  {
		
		System.out.println("TestDataAccess created");

		//open();
		
	}

	
	public void open(){
		

		String fileName=c.getDbFilename();
		
		if (c.isDatabaseLocal()) {
			  emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			  db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);

			  db = emf.createEntityManager();
    	   }
		System.out.println("TestDataAccess opened");

		
	}
	public void close(){
		db.close();
		System.out.println("TestDataAccess closed");
	}

	public boolean removeDriver(String name) {
		System.out.println(">> TestDataAccess: removeDriver");
		Driver d = db.find(Driver.class, name);
		if (d!=null) {
			db.getTransaction().begin();
			db.remove(d);
			db.getTransaction().commit();
			return true;
		} else 
		return false;
    }
	public Driver createDriver(String name, String pass) {
		System.out.println(">> TestDataAccess: addDriver");
		Driver driver=null;
			db.getTransaction().begin();
			try {
			    driver=new Driver(name,pass);
				db.persist(driver);
				db.getTransaction().commit();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return driver;
    }
	public boolean existDriver(String email) {
		 return  db.find(Driver.class, email)!=null;
		 

	}
		
		public Driver addDriverWithRide(String name, String from, String to,  Date date, int nPlaces, float price) {
			System.out.println(">> TestDataAccess: addDriverWithRide");
				Driver driver=null;
				db.getTransaction().begin();
				try {
					 driver = db.find(Driver.class, name);
					if (driver==null) {
						System.out.println("Entra en null");
						driver=new Driver(name,null);
				    	db.persist(driver);
					}
				    driver.addRide(from, to, date, nPlaces, price);
					db.getTransaction().commit();
					System.out.println("Driver created "+driver);
					
					return driver;
					
				}
				catch (Exception e){
					e.printStackTrace();
				}
				return null;
	    }

		public Driver addDriverWithRideAndBooking(String driverName, String from, String to, Date date, int nPlaces, float price, Traveler traveler, int seats) {
			System.out.println(">> TestDataAccess: addDriverWithRideAndBooking");
			Driver driver = null;
			db.getTransaction().begin();
			try {
				driver = db.find(Driver.class, driverName);
				if (driver == null) {
					System.out.println("Entra en null");
					driver = new Driver(driverName, null);
					db.persist(driver);
				}
				Ride ride = driver.addRide(from, to, date, nPlaces, price);
				Booking booking = new Booking(ride, traveler, seats);
				List<Booking> bookings = new java.util.ArrayList<Booking>();
				bookings.add(booking);
				ride.setBookings(bookings);
				traveler.addBookedRide(booking);
				db.persist(booking);
				db.getTransaction().commit();
				System.out.println("Driver created " + driver);
				return driver;
			} catch (Exception e) {
				e.printStackTrace();
				db.getTransaction().rollback();
			}
			return null;
		}
		
		
		public boolean existRide(String name, String from, String to, Date date) {
			System.out.println(">> TestDataAccess: existRide");
			Driver d = db.find(Driver.class, name);
			if (d!=null) {
				return d.doesRideExists(from, to, date);
			} else 
			return false;
		}
		public Ride removeRide(String name, String from, String to, Date date ) {
			System.out.println(">> TestDataAccess: removeRide");
			Driver d = db.find(Driver.class, name);
			if (d!=null) {
				db.getTransaction().begin();
				Ride r= d.removeRide(from, to, date);
				db.getTransaction().commit();
				System.out.println("created rides" +d.getCreatedRides());
				return r;

			} else 
			return null;

		}

		public void setActiveRide(boolean status){
			System.out.println(">> TestDataAccess: setActiveRide");
			Driver d = db.find(Driver.class, "Driver Test");
			if (d!=null) {
				db.getTransaction().begin();
				d.getCreatedRides().get(0).setActive(status);
				db.getTransaction().commit();
			}
		}


		public boolean existTraveler(String email) {
			System.out.println(">> TestDataAccess: existTraveler");
			return  db.find(Traveler.class, email)!=null;
		}

		public Traveler createTraveler(Traveler traveler) {
			System.out.println(">> TestDataAccess: createTraveler");
				db.getTransaction().begin();
				if (db.find(Traveler.class, traveler.getUsername())!=null) {
					db.getTransaction().rollback();
					return null;
				}
				try {
					db.persist(traveler);
					db.getTransaction().commit();
				}
				catch (Exception e){
					e.printStackTrace();
				}
				return traveler;
	    }

		public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverName) {
			System.out.println(">> TestDataAccess: creatRide");
			Driver driver = db.find(Driver.class, driverName);
			if (driver != null) {
				db.getTransaction().begin();
				Ride ride = driver.addRide(from, to, date, nPlaces, price);
				db.getTransaction().commit();
				return ride;
			}
			return null;
		}

		public boolean removeTraveler(String name) {
			System.out.println(">> TestDataAccess: removeTraveler");
			Traveler t = db.find(Traveler.class, name);
			if (t!=null) {
				db.getTransaction().begin();
				db.remove(t);
				db.getTransaction().commit();
				return true;
			} else 
			return false;
		}	


		
}