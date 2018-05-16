package de.dis2018.core;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import de.dis2018.data.House;
import de.dis2018.data.Estate;
import de.dis2018.data.PurchaseContract;
import de.dis2018.data.EstateAgent;
import de.dis2018.data.TenancyContract;
import de.dis2018.data.Person;
import de.dis2018.data.Apartment;

/**
 *  Class for managing all database entities.
 * 
 * TODO: All data is currently stored in memory. 
 * The aim of the exercise is to gradually outsource data management to the 
 * database. When the work is done, all sets in this class become obsolete!
 */
public class EstateService {
	//TODO All these sets should be commented out after successful implementation.
	//private Set<EstateAgent> estateAgents = new HashSet<EstateAgent>();
	//private Set<Person> persons = new HashSet<Person>();
	//private Set<House> houses = new HashSet<House>();
	//private Set<Apartment> apartments = new HashSet<Apartment>();
	//private Set<TenancyContract> tenancyContracts = new HashSet<TenancyContract>();
	//private Set<PurchaseContract> purchaseContracts = new HashSet<PurchaseContract>();
	
	//Hibernate Session
	private SessionFactory sessionFactory;
	
	public EstateService() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}
	
	/**
	 * Find an estate agent with the given id
	 * @param id The ID of the agent
	 * @return Agent with ID or null
	 */
	public EstateAgent getEstateAgentByID(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		EstateAgent ea = (EstateAgent) session.get(EstateAgent.class, id);
		session.getTransaction().commit();
		return ea;
	}
	
	/**
	 * Find estate agent with the given login.
	 * @param login The login of the estate agent
	 * @return Estate agent with the given ID or null
	 */
	public EstateAgent getEstateAgentByLogin(String login) {
		if( login.compareTo("") != 0 ) {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			EstateAgent ea = (EstateAgent)session.createQuery("FROM EstateAgent WHERE login = :login").setParameter("login",login).uniqueResult();
			session.getTransaction().commit();
			return ea;
		}
		return null;
	}
	
	/**
	 * Returns all estateAgents
	 */
	public Set<EstateAgent> getAllEstateAgents() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<EstateAgent> l = session.createQuery("FROM EstateAgent").list();
		Set<EstateAgent> m = new HashSet<EstateAgent>(l);
		session.getTransaction().commit();
		return m;
	}
	
	/**
	 * Find an person with the given id
	 * @param id The ID of the person
	 * @return Person with ID or null
	 */
	public Person getPersonById(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Person p = null;
		try {
			p = (Person) session.get(Person.class, id);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return p;
	}
	
	/**
	 * Adds an estate agent
	 * @param ea The estate agent
	 */
	public void addEstateAgent(EstateAgent ea) {
		Session session = null;
		if( this.getEstateAgentByLogin(ea.getLogin()) == null ) {
			try {
				session = sessionFactory.getCurrentSession();
				session.beginTransaction();
				Integer did = (Integer)session.save(ea);
				ea.setId(did);
			} catch( Exception e ) {
				e.printStackTrace();
				session.getTransaction().rollback();
			}
			try {
				session.getTransaction().commit();
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
		else System.out.println("Login already used.");
	}
	
	/**
	 * Update a EstateAgent
	 * @param m The EstateAgent
	 */
	public void updateEstateAgent(EstateAgent ea) {
		Session session = null;
		if( this.getEstateAgentByLogin(ea.getLogin()) == null ) {
			try {
				session = sessionFactory.getCurrentSession();
				session.beginTransaction();
				session.update(ea);
				session.getTransaction().commit();
			} catch( Exception e ) {
				session.getTransaction().rollback();
				e.printStackTrace();
			}
		}
		else System.out.println("Login already used.");
	}
	
	/**
	 * Deletes an estate agent
	 * @param ea The estate agent
	 */
	public void deleteEstateAgent(EstateAgent ea) {
		Set<PurchaseContract> l_pc = this.getAllPurchaseContractsForEstateAgent(ea);
		Set<TenancyContract> l_tc = this.getAllTenancyContractsForEstateAgent(ea);
		Iterator it_pc = l_pc.iterator();
		Iterator it_tc = l_tc.iterator();
		Set<Estate> contract_estates = new HashSet<Estate>();
		
		while(it_pc.hasNext()) {
			contract_estates.add(((PurchaseContract)it_pc.next()).getHouse());
		}
		
		while(it_tc.hasNext()) {
			contract_estates.add(((TenancyContract)it_tc.next()).getApartment());
		}
		
		if( contract_estates.isEmpty() ) {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			try {
				session.delete(ea);
				session.getTransaction().commit();
			} catch( Exception e ) {
				session.getTransaction().rollback();
				e.printStackTrace();
			}
		}
		else System.out.println("Estate agent cannot be deleted because there are some contracts attached to him.");
		
		
	}
	
	/**
	 * Adds a person
	 * @param p The person
	 */
	public void addPerson(Person p) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			Integer did = (Integer)session.save(p);
			p.setId(did);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	/**
	 * Update a Person
	 * @param p The Person
	 */
	public void updatePerson(Person p) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			session.update(p);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns all persons
	 */
	public Set<Person> getAllPersons() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Person> l = session.createSQLQuery("SELECT * FROM Person").addEntity(Person.class).list();
		Set<Person> p = new HashSet<Person>(l);
		session.getTransaction().commit();
		
		return p;	
	}
	
	/**
	 * Deletes a person
	 * @param p The person
	 */
	public void deletePerson(Person p) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			//Person managed_p = (Person)session.merge(p);
			session.delete(p);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a house
	 * @param h The house
	 */
	public void addHouse(House h) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			session.save(h);
			session.persist(h);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns all houses of an estate agent
	 * @param ea the estate agent
	 * @return All houses managed by the estate agent
	 */
	public Set<House> getAllHousesForEstateAgent(EstateAgent ea) {
		/*
		Set<Estate> s = ea.getEstates();
		Iterator it = s.iterator();
		
		Set<House> ret = new HashSet<House>();
		while( it.hasNext() ) {
			House h = (House)it.next();
			if( h.getClass() == House.class ) {
				ret.add(h);
			}
		}
		
		return ret;
		*/
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<House> l = session.createQuery("from House h where h.id in (select e.id from Estate e where e.manager = :manager ) ").setEntity("manager",ea).list();
		Set<House> p = new HashSet<House>(l);
		session.getTransaction().commit();
		
		return p;
	}
	
	/**
	 * Find a house with a given ID
	 * @param  id the house id
	 * @return The house or null if not found
	 */
	public House getHouseById(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		House h = null;
		try {
			h = (House)session.get(House.class, id);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return h;
	}
	
	/**
	 * Deletes a house
	 * @param h The house
	 */
	public void deleteHouse(House h) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			session.delete(h);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates a house
	 * @param h The house
	 */
	public void updateHouse(House h) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			session.update(h);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds an apartment
	 * @param w the apartment
	 */
	public void addApartment(Apartment w) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			Integer did = (Integer)session.save(w);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns all apartments of an estate agent
	 * @param ea The estate agent
	 * @return All apartments managed by the estate agent
	 */
	public Set<Apartment> getAllApartmentsForEstateAgent(EstateAgent ea) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Apartment> l = session.createQuery("from Apartment WHERE id in (select id FROM Estate WHERE manager = :manager) ").setEntity("manager", ea).list();
		Set<Apartment> p = new HashSet<Apartment>(l);
		session.getTransaction().commit();
		
		return p;
	}
	
	/**
	 * Find an apartment with given ID
	 * @param id The ID
	 * @return The apartment or zero, if not found
	 */
	public Apartment getApartmentByID(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Apartment w = null;
		try {
			w = (Apartment) session.get(Apartment.class, id);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return w;
	}
	
	/**
	 * Deletes an apartment
	 * @param p The apartment
	 */
	public void deleteApartment(Apartment w) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			session.delete(w);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates an apartment
	 * @param p The apartment
	 */
	public void updateApartment(Apartment w) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			session.update(w);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Adds a tenancy contract
	 * @param t The tenancy contract
	 */
	public void addTenancyContract(TenancyContract t) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			Integer did = (Integer)session.save(t);
			t.setId(did);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a purchase contract
	 * @param p The purchase contract
	 */
	public void addPurchaseContract(PurchaseContract p) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			if( !session.contains(p.getHouse()) )
					session.save(p.getHouse());
			
			Integer did = (Integer)session.save(p);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	/**
	 * Finds a tenancy contract with a given ID
	 * @param id Die ID
	 * @return The tenancy contract or zero if not found
	 */
	public TenancyContract getTenancyContractByID(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		TenancyContract m = null;
		try {
			m = (TenancyContract) session.get(TenancyContract.class, id);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return m;
	}
	
	/**
	 * Finds a purchase contract with a given ID
	 * @param id The id of the purchase contract
	 * @return The purchase contract or null if not found
	 */
	public PurchaseContract getPurchaseContractById(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		PurchaseContract k = null;
		try {
			k = (PurchaseContract) session.get(PurchaseContract.class, id);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return k;
	}
	
	/**
	 * Returns all tenancy contracts for apartments of the given estate agent
	 * @param m The estate agent
	 * @return All contracts belonging to apartments managed by the estate agent
	 */
	public Set<TenancyContract> getAllTenancyContractsForEstateAgent(EstateAgent ea) {
		/*
		Session session = sessionFactory.getCurrentSession();
		
		session.beginTransaction();
		//List<House> l = session.createQuery("from House h where h.id in (select e.id from Estate e where e.manager = :manager ) ").setEntity("manager",ea).list();
		//String q = "FROM TenancyContract WHERE apartment IN (SELECT id FROM Estate WHERE manager IN (SELECT id FROM EstateAgent WHERE id = :id";
		String q = "FROM TenancyContract WHERE apartment.id IN (Select id from Apartment where id in (select id FROM Estate WHERE manager = :manager) )";
		List<TenancyContract> l = session.createQuery(q).setEntity("manager", ea).list();
		Set<TenancyContract> p = new HashSet<TenancyContract>(l);
		session.getTransaction().commit();
		
		return p;
		*/
		Set<Apartment> apartments_ea = this.getAllApartmentsForEstateAgent(ea);

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		Set<TenancyContract> contracts_ea = new HashSet<TenancyContract>();
		Iterator<Apartment> it = apartments_ea.iterator();
		String SQL_statement = "FROM de.dis2018.data.TenancyContract TC where TC.apartment = ?";

		while(it.hasNext()){
			try{
				Apartment apartment = it.next();
				TenancyContract tc = (TenancyContract) session.createQuery(SQL_statement).setEntity(0, apartment).uniqueResult();
				if (tc != null) {
					contracts_ea.add(tc);
				}
			}
			catch (Exception e){
				e.printStackTrace();
				transaction.rollback();
			}
		}
		transaction.commit();

		return contracts_ea;
	}
	
	/**
	 * Returns all purchase contracts for houses of the given estate agent
	 * @param m The estate agent
	 * @return All purchase contracts belonging to houses managed by the given estate agent
	 */
	public Set<PurchaseContract> getAllPurchaseContractsForEstateAgent(EstateAgent ea) {
		/*
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		String q = "SELECT * FROM PurchaseContract WHERE house_id IN (SELECT id FROM Estate WHERE manager IN (SELECT id FROM EstateAgent WHERE id = :id";
		List<PurchaseContract> l = session.createSQLQuery(q).addEntity(PurchaseContract.class).setParameter("id", ea.getId()).list();
		Set<PurchaseContract> p = new HashSet<PurchaseContract>(l);
		session.getTransaction().commit();
		
		return p;
		*/
		Set<House> houses_ea = this.getAllHousesForEstateAgent(ea);

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		Set<PurchaseContract> contracts_ea = new HashSet<PurchaseContract>();
		Iterator<House> it = houses_ea.iterator();
		String SQL_statement = "FROM de.dis2018.data.PurchaseContract PC where PC.house = ";

		while(it.hasNext()){
			try{
				House house = it.next();
				PurchaseContract c = (PurchaseContract) session.createQuery(SQL_statement + Integer.toString(house.getId())).uniqueResult();
				if (c != null){
					contracts_ea.add(c);
					System.out.print(c.getId());
					System.out.print('\n');
				}
			} catch (Exception e) {
				e.printStackTrace();
				transaction.rollback();
			}
		}
		transaction.commit();

		return contracts_ea;
	}
	
	/**
	 * Finds all tenancy contracts relating to the apartments of a given estate agent	 
	 * @param ea The estate agent
	 * @return set of tenancy contracts
	 */
	public Set<TenancyContract> getTenancyContractByEstateAgent(EstateAgent ea) {
		return getAllTenancyContractsForEstateAgent(ea);
	}
	
	/**
	 * Finds all purchase contracts relating to the houses of a given estate agent
	 * @param  ea The estate agent
	 * @return set of purchase contracts
	 */
	public Set<PurchaseContract> getPurchaseContractByEstateAgent(EstateAgent ea) {
		return getAllPurchaseContractsForEstateAgent(ea);
	}

	
	/**
	 * Deletes a tenancy contract
	 * @param tc the tenancy contract
	 */
	public void deleteTenancyContract(TenancyContract tc) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			session.delete(tc);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes a purchase contract
	 * @param tc the purchase contract
	 */
	public void deletePurchaseContract(PurchaseContract pc) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			session.delete(pc);
			session.getTransaction().commit();
		} catch( Exception e ) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds some test data
	 */
	public void addTestData() {
		//Hibernate Session erzeugen
		Session session = sessionFactory.openSession();
		
		//session.beginTransaction();
		
		EstateAgent m = new EstateAgent();
		m.setName("Max Mustermann");
		m.setAddress("Am Informatikum 9");
		m.setLogin("max");
		m.setPassword("max");
		
		//TODO: This estate agent is kept in memory and the DB
		this.addEstateAgent(m);
		//session.save(m);
		//session.getTransaction().commit();

		//session.beginTransaction();
		
		Person p1 = new Person();
		p1.setAddress("Informatikum");
		p1.setName("Mustermann");
		p1.setFirstname("Erika");
		
		
		Person p2 = new Person();
		p2.setAddress("Reeperbahn 9");
		p2.setName("Albers");
		p2.setFirstname("Hans");
		
		//session.save(p1);
		//session.save(p2);
		
		//TODO: These persons are kept in memory and the DB
		this.addPerson(p1);
		this.addPerson(p2);
		//session.getTransaction().commit();
		
		
		//session.beginTransaction();
		House h = new House();
		h.setCity("Hamburg");
		h.setPostalcode(22527);
		h.setStreet("Vogt-Kölln-Street");
		h.setStreetnumber("2a");
		h.setSquareArea(384);
		h.setFloors(5);
		h.setPrice(10000000);
		h.setGarden(true);
		h.setManager(m);
		System.out.println("a");
		System.out.println(h.getId());
		System.out.println("");
		
		
		//session.save(h);
		
		//TODO: This house is held in memory and the DB
		this.addHouse(h);
		//session.getTransaction().commit();
		System.out.println("b");
		System.out.println(h.getId());
		System.out.println("");
		
		// Create Hibernate Session
		//session = sessionFactory.openSession();
		/*
		session.beginTransaction();
		EstateAgent m2 = (EstateAgent)session.get(EstateAgent.class, m.getId());
		Set<Estate> immos = m2.getEstates();
		Iterator<Estate> it = immos.iterator();
		
		while(it.hasNext()) {
			Estate i = it.next();
			System.out.println("Estate: "+i.getCity());
		}
		session.getTransaction().commit();
		//session.close();
		*/
		Apartment w = new Apartment();
		w.setCity("Hamburg");
		w.setPostalcode(22527);
		w.setStreet("Vogt-Kölln-Street");
		w.setStreetnumber("3");
		w.setSquareArea(120);
		w.setFloor(4);
		w.setRent(790);
		w.setKitchen(true);
		w.setBalcony(false);
		w.setManager(m);
		this.addApartment(w);

		System.out.println("c");
		System.out.println(h.getId());
		System.out.println("");
		
		w = new Apartment();
		w.setCity("Berlin");
		w.setPostalcode(22527);
		w.setStreet("Vogt-Kölln-Street");
		w.setStreetnumber("3");
		w.setSquareArea(120);
		w.setFloor(4);
		w.setRent(790);
		w.setKitchen(true);
		w.setBalcony(false);
		w.setManager(m);
		this.addApartment(w);
		
		System.out.println("d");
		System.out.println(h.getId());
		System.out.println("");
		
		PurchaseContract pc = new PurchaseContract();
		pc.setHouse(h);
		pc.setContractPartner(p1);
		pc.setContractNo(9234);
		pc.setDate(new Date(System.currentTimeMillis()));
		pc.setPlace("Hamburg");
		pc.setNoOfInstallments(5);
		pc.setIntrestRate(4);
		this.addPurchaseContract(pc);
		
		TenancyContract tc = new TenancyContract();
		tc.setApartment(w);
		tc.setContractPartner(p2);
		tc.setContractNo(23112);
		tc.setDate(new Date(System.currentTimeMillis()-1000000000));
		tc.setPlace("Berlin");
		tc.setStartDate(new Date(System.currentTimeMillis()));
		tc.setAdditionalCosts(65);
		tc.setDuration(36);
		this.addTenancyContract(tc);
	}
}
