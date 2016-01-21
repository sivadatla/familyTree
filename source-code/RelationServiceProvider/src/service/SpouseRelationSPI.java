package service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import service.exc.ServiceException;
import service.exc.ValidationException;
import service.model.Person;
import service.model.Relation;
import service.model.Relationship;
import store.DBStore;


/**
 * SP implementation for Relationship-SIBLING
 * @author Siva Datla
 *
 */
public class SpouseRelationSPI implements IRelationService{

	private static final Logger LOG = Logger.getLogger(SpouseRelationSPI.class.getName());
	
	public SpouseRelationSPI() {
		
	}

	

	@Override
	public Set<Person> process(Person person) throws ServiceException {
		Set<Person> persons = new HashSet<>();
		
		Set<Relation> relations = DBStore.getRelation(person, getRelationship());
		
		for (Relation relation : relations) {
			persons.add(relation.getDest());
		}
		
		return persons;
	}

	@Override
	public Relationship getRelationship() {
		
		return DBStore.getRelationship(DBStore.SPOUSE);
	}


	@Override
	public boolean beforeAddingRelation(Relation relation) throws ValidationException {
		
		if(LOG.isLoggable(Level.FINEST)){
			LOG.finest(">>> validateBeforeAdding");
		}
		
		Person me = relation.getSource();
		Person spouse = relation.getDest();
		
		if(me.getSex() == spouse.getSex()){
			LOG.warning("Allowing same sex as spouse as per new rules! ");
		}
		
		
		Set<Relation> spouseRels = DBStore.getRelation(me, DBStore.SPOUSE);
		Optional<Relation> spouseRel = spouseRels.parallelStream().filter(e -> e.getDest().equals(spouse)).findFirst();
		
		return spouseRel.isPresent();
	}



	@Override
	public Set<Relation> afterAddingRelation(Relation relation) throws ServiceException {
		
		Person me = relation.getSource();
		Person spouse = relation.getDest();
		
		Set<Relation> newRels = new HashSet<>();
		Relation rel = null;
		
		// If I am one of the spouses, the spouse should have me in it's spouses list.
		Set<Relation> spousesSpouseRels = DBStore.getRelation(spouse, DBStore.SPOUSE);
		Optional<Relation> spousesSpouse = spousesSpouseRels.parallelStream().filter(e -> e.getDest().equals(me)).findFirst();
		if(!spousesSpouse.isPresent()){
			rel = new Relation(spouse, DBStore.getRelationship(DBStore.SPOUSE), me);
			if( ! DBStore.isDuplicate(rel)){
				newRels.add(rel);
			}
		}
		
		LOG.info(newRels.toString());
		return newRels;
	}


	@Override
	public FamilyPerson connectFamilyMembers(FamilyPerson fPerson) throws ServiceException {
	
		if(fPerson == null || fPerson.isTraversed()){
			return null;
		}
		
		//TODO: validation of null.
		Set<Relation> spouseRels = DBStore.getRelation(fPerson.getPerson(), DBStore.SPOUSE);
		
		FamilyPerson spouse = null;
		for (Relation rel : spouseRels) {
			spouse = FamilyPersonFactory.getFamilyPerson(rel.getDest());
			fPerson.addSpouse(spouse);
		}
		
		return spouse;
	}
	
	@Override
	public List<Person> print(Person person) throws ServiceException {
		List<Person> persons = new ArrayList<>();
		
		Set<Relation> relations = DBStore.getRelation(person, getRelationship());
		
		for (Relation relation : relations) {
			persons.add(relation.getDest());
		}
		
		return persons;
	}

}
