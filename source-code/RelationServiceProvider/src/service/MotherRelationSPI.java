package service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import service.exc.ServiceException;
import service.exc.ValidationException;
import service.model.Person;
import service.model.Relation;
import service.model.Relationship;
import service.model.Sex;
import service.util.ServiceUtil;
import store.DBStore;


/**
 * SP implementation for Relationship-MOTHER
 * @author Siva Datla
 *
 */
public class MotherRelationSPI implements IRelationService{

	private static final Logger LOG = Logger.getLogger(MotherRelationSPI.class.getName());
	
	public MotherRelationSPI() {
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
		
		return DBStore.getRelationship("MOTHER");
	}


	@Override
	public boolean beforeAddingRelation(Relation relation) throws ValidationException {
		
		if(LOG.isLoggable(Level.FINEST)){
			LOG.finest(">>> validateBeforeAdding");
		}
		
		Person me = relation.getSource();
		Person mother = relation.getDest();
		
		if(mother.getSex() != Sex.FEMALE){
			throw new ValidationException("Mother should be a female!");
		}
		
		boolean isDuplicate = false;
		//A person can not have more than a Mother!
		Set<Relation> rel = DBStore.getRelation(me, relation.getRelationship());
		if(rel.size() > 0){
			Person existingMother = rel.iterator().next().getDest();
			LOG.info(existingMother.toString());
			LOG.info(relation.getDest().toString());
			if(existingMother.equals(relation.getDest())){
				isDuplicate = true;
			}
			else{
				throw new ValidationException("This person got a mother already!");
			}
		}
		
		return isDuplicate;
	}



	@Override
	public Set<Relation> afterAddingRelation(Relation relation) throws ServiceException {
		
		Set<Relation> newRelations = new HashSet<>();
		Relation rel = null;
		
		Person me = relation.getSource();
		Person mother = relation.getDest();
		
		//If I have a father, he should have my mother as one of his spouses.
		Person father = ServiceUtil.getParentOf(me, DBStore.FATHER);
		if(father != null){
			rel = new Relation(mother, DBStore.getRelationship(DBStore.SPOUSE), father);
			if( ! DBStore.isDuplicate(rel)){
				newRelations.add(rel);
			}
		}
		
		//Add the mother to all my existing siblings
		Set<Relation> siblingRels = DBStore.getRelation(me, DBStore.SIBLING);
		for (Relation siblingRel : siblingRels) {
			rel = new Relation(siblingRel.getDest(), DBStore.getRelationship(DBStore.MOTHER), mother);
			if( ! DBStore.isDuplicate(rel)){
				newRelations.add(rel);
			}
		}
		
		LOG.info(newRelations.toString());
		return newRelations;
	}

	@Override
	public FamilyPerson connectFamilyMembers(FamilyPerson fPerson) throws ServiceException {
	
		//TODO: validation of null and more no. of fathers!
		if(fPerson == null || fPerson.isTraversed()){
			return null;
		}
		
		Set<Relation> motherSet = DBStore.getRelation(fPerson.getPerson(), DBStore.MOTHER);
		
		FamilyPerson mother = null;
		
		for (Relation relation : motherSet) {
			mother = FamilyPersonFactory.getFamilyPerson(relation.getDest());
			fPerson.addParent(mother);
		}
		
		return mother;
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
