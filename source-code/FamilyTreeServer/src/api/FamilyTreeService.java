/**
 * 
 */
package api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import api.spi.RelationServiceLoader;
import service.FamilyPerson;
import service.FamilyPersonFactory;
import service.IRelationService;
import service.exc.ServiceException;
import service.model.Person;
import service.model.Relation;
import service.model.Relationship;
import store.DBStore;

/**
 * Family Tree Facade Service. Exposes all APIs of the Family Tree Application.
 * 
 * @author Siva Datla
 *
 */
public class FamilyTreeService implements IPersonService, IRelationCRUDService {

	private static final Logger LOG = Logger.getLogger(FamilyTreeService.class.getName());

	//////////////////////////////////////////////////////
	//
	// Persons related methods
	//
	//////////////////////////////////////////////////////
	/**
	 * Persists the given Person object
	 */
	@Override
	public void createPerson(Person person) throws FTException {

		DBStore.addPerson(person);
	}

	/**
	 * Gets all Persons from the persistence store.
	 * 
	 * @return
	 */
	@Override
	public Collection<Person> getPersons() {
		return DBStore.getPersons();
	}

	/**
	 * Gets the person, if exists, from the persistence store.
	 * 
	 * @param id
	 * @return
	 */
	public Person getPersonById(String id) {
		return DBStore.getPersonById(id);
	}

	/**
	 * Updates changes of a person.
	 * 
	 * @param person
	 * @return
	 */
	public Person updatePerson(Person person) {
		return DBStore.updatePerson(person);
	}

	//////////////////////////////////////////////////////
	//
	// Relation related methods
	//
	//////////////////////////////////////////////////////

	/**
	 * Creates a new relation Relation is a tuple of person, his/her
	 * relationship, (with) person
	 * 
	 */
	@Override
	public void createRelation(String sourcePersonId, String relationshipId, String destPersonId)
			throws ServiceException {

		Person source = DBStore.getPersonById(sourcePersonId);
		if (source == null) {
			// TODO: externalize all error messages so that they can localized
			// later.
			throw new ServiceException("There is no person with given id : " + sourcePersonId);
		}

		Person dest = DBStore.getPersonById(destPersonId);
		if (dest == null) {
			throw new ServiceException("There is no person with given id : " + destPersonId);
		}

		Relationship relationship = DBStore.getRelationship(relationshipId);
		if (relationship == null) {
			throw new ServiceException("There is no person with given id : " + relationshipId);
		}

		createRelation(source, relationship, dest);

	}

	/**
	 * creates a relation with objects of Person, relationship and person
	 */
	@Override
	public Relation createRelation(Person source, Relationship relationship, Person dest) throws ServiceException {
		Relation rel = new Relation(source, relationship, dest);
		createRelation(rel);

		return getRelationByid(rel.getId());
	}

	/**
	 * Gives back all Relations created till now.
	 * 
	 * @return
	 */
	public Collection<Relation> getRelations() {
		return DBStore.getRelations();
	}

	/**
	 * Creates a relation, other overloaded methods shall be invoking this.
	 * 
	 * It looks for SPI for the relationship provided in relation and invokes
	 * beforeAddingRelation and afterAddingRelation methods appropriately.
	 * 
	 * Note: in future, we may add notifier SPI, so that we can invoke SPIs
	 * whenever there is a relation created.
	 * 
	 * @param relation
	 * @throws ServiceException
	 */
	public void createRelation(Relation relation) throws ServiceException {

		LOG.info(relation.toString());
		IRelationService relService = RelationServiceLoader.loadRelationService(relation.getRelationship());

		boolean skipCreation = false;

		// invoke before
		if (relService != null) {
			skipCreation = relService.beforeAddingRelation(relation);
		}

		// Add the relation to DB.
		if (!skipCreation && !DBStore.isDuplicate(relation)) {
			LOG.info("adding relation " + relation);

			DBStore.addRelation(relation);

			// invoke after
			if (relService != null) {
				Set<Relation> newRelations = relService.afterAddingRelation(relation);
				if (newRelations != null) {
					for (Relation rel : newRelations) {
						createRelation(rel);
					}
				}
			}
		}
	}

	/**
	 * Gets a relation from persistence store with provided id
	 * 
	 * @param id
	 * @return
	 */
	public Relation getRelationByid(String id) {
		return DBStore.getRelationById(id);
	}

	//////////////////////////////////////////////////////
	//
	// Relationship related methods
	//
	//////////////////////////////////////////////////////

	/**
	 * Gets all Relationships from persistence storage
	 * 
	 * @return
	 */
	public Collection<Relationship> getAllRelationships() {
		return DBStore.getRelationships();
	}

	/**
	 * Gets a relationship, if exists, from persistence store with given
	 * relationshipId
	 * 
	 * @param relationshipId
	 * @return
	 */
	public Relationship getRelationshipById(String relationshipId) {

		return DBStore.getRelationship(relationshipId);
	}

	//////////////////////////////////////////////////////
	//
	// Other methods
	//
	//////////////////////////////////////////////////////

	/**
	 * prints a family tree for the given person
	 * 
	 * @param person
	 */
	public FamilyPerson printFamilyTree(Person person) {

		FamilyPerson fPerson = FamilyPersonFactory.getFamilyPerson(person);

		// Build all connections
		FamilyPerson newFPerson = getImmediateRelations(fPerson);
		
		
		printFT(person.getId());
		
		// reset traversed flag for all people!
		resetTraversed();

		return newFPerson;
	}

	public void printFT(String personId) {
		Person person = DBStore.getPersonById(personId);
//		FamilyPerson fPerson = printFamilyTree(person);

		Comparator<Relationship> compareRelationships = (e1, e2) -> Integer.compare(e1.getDepth(), e2.getDepth());

		HashMap<String, List<Person>> personsByRelationship = new HashMap<>();

		DBStore.getRelationships().stream().sorted(compareRelationships).forEach(e -> {
			IRelationService service = RelationServiceLoader.loadRelationService(e);
			try {
				List<Person> persons = service.print(person);
				personsByRelationship.put(e.getId(), persons);
				System.out.println("----------------" + e);
				System.out.println(persons);
				System.out.println("------------------");
			} catch (Exception e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}

		});

		Set<Person> relatedPersons = new HashSet<>();
		for (List<Person> persons : personsByRelationship.values()) {
			if (persons != null)
				relatedPersons.addAll(persons);
		}
		//adding this person as well!
		relatedPersons.add(person);

		// OTHERS
		Collection<Person> allFamilyPersons = FamilyPersonFactory.getAllPersons();
		allFamilyPersons.removeAll(relatedPersons);
		
		List<Person> otherPersons = new ArrayList<>();
		otherPersons.addAll(allFamilyPersons);
		System.out.println("OTHERS : " + allFamilyPersons);
		
		personsByRelationship.put(DBStore.OTHER, otherPersons);
		
		
		//
		
		//
		
/*		
		String pName[] = new String[2];
		String gpName[] = new String[4];
		
		String siblingName[] = null;
//		String gpName2;
		//grandparents
		int i = 0;
		int j = 0;
		if(fPerson == null)
		{
			System.out.println("Family Person is null!!!!");
		}
		if(fPerson.getParents() != null){
			for (FamilyPerson parent : fPerson.getParents()) {
//				if(i == 1){
//					pName[i] += ", ";
//				}
				pName[i] = parent.getPerson().getFirstName() + ", " + parent.getPerson().getLastName();
				i++;
				for(FamilyPerson gParent : parent.getParents()){
					gpName[j] = gParent.getPerson().getFirstName()+ ", " + gParent.getPerson().getLastName();
					j++;
				}
			}
		}
		
		System.out.println(pName[0]);
		System.out.println(pName[1]);
		

		System.out.println(gpName[0]);
		System.out.println(gpName[1]);

		System.out.println(gpName[2]);
		System.out.println(gpName[3]);
		
		System.out.println(person.getFirstName() + ", " + person.getLastName());
		
		String[] spouses = null;
		i = 0;
		if(fPerson.getSpouses() != null){
			spouses = new String[fPerson.getSpouses().size()];
			for (FamilyPerson spouse : fPerson.getSpouses()) {
				spouses[i] = spouse.getPerson().getFirstName() + ", " + spouse.getPerson().getLastName();
				i++;
			}
		}
		
		for (String spouse : spouses) {
			System.out.println(spouse);
		}
		
		//siblings
		Set<Relation> siblingRels = DBStore.getRelation(person, DBStore.SIBLING);
		
		i=0;
		siblingName = new String[siblingRels.size()];
		for(Relation r : siblingRels){
			siblingName[i++] = r.getDest().getFirstName() + ", " + r.getDest().getLastName();
		}
		
		for (String name : siblingName) {
			System.out.println(name);
		}
*/
		

	}

	/**
	 * 
	 */
	private void resetTraversed() {
		FamilyPersonFactory.clearCache();
	}

	private FamilyPerson getImmediateRelations(FamilyPerson fPerson) {

		if (fPerson == null || fPerson.isTraversed())
			return null;

		// connectFather
		IRelationService fatherService = RelationServiceLoader
				.loadRelationService(DBStore.getRelationship(DBStore.FATHER));

		IRelationService motherService = RelationServiceLoader
				.loadRelationService(DBStore.getRelationship(DBStore.MOTHER));

		IRelationService spouseService = RelationServiceLoader
				.loadRelationService(DBStore.getRelationship(DBStore.SPOUSE));

		IRelationService childService = RelationServiceLoader
				.loadRelationService(DBStore.getRelationship(DBStore.CHILD));

		try {
			FamilyPerson father = fatherService.connectFamilyMembers(fPerson);
			FamilyPerson mother = motherService.connectFamilyMembers(fPerson);
			FamilyPerson spouse = spouseService.connectFamilyMembers(fPerson);

			// childService returns the same person again, we need get his/her
			// children to traversal
			FamilyPerson thisPerson = childService.connectFamilyMembers(fPerson);

			fPerson.setTraversed(true);

			if (father != null) {
				getImmediateRelations(father);
			}
			if (mother != null) {
				getImmediateRelations(mother);
			}
			if (spouse != null) {
				getImmediateRelations(spouse);
			}

			if (thisPerson != null && thisPerson.getChildren() != null && thisPerson.getChildren().size() > 0) {
				for (FamilyPerson child : thisPerson.getChildren()) {
					getImmediateRelations(child);
				}
			}

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fPerson;
	}

}
