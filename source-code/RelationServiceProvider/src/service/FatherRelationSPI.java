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
 * SP implementation for Relationship-FATHER
 * @author Siva Datla
 *
 */
public class FatherRelationSPI implements IRelationService{

	private static final Logger LOG = Logger.getLogger(FatherRelationSPI.class.getName());
	
	public FatherRelationSPI() {
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
		
		return DBStore.getRelationship("FATHER");
	}


	@Override
	public boolean beforeAddingRelation(Relation relation) throws ValidationException {
		
		if(LOG.isLoggable(Level.FINEST)){
			LOG.finest(">>> validateBeforeAdding");
		}
		
		Person me = relation.getSource();
		Person father = relation.getDest();
		
		if(father.getSex() != Sex.MALE){
			throw new ValidationException("Father should be a male! - " + relation);
		}
		
		boolean isDuplicate = false;
		
		//A person can not have more than one Father!
		Set<Relation> rel = DBStore.getRelation(me, relation.getRelationship());
		if(rel.size() > 0){
			Person existingFather = rel.iterator().next().getDest();
			LOG.info(existingFather.toString());
			LOG.info(relation.getDest().toString());
			if(existingFather.equals(relation.getDest())){
				isDuplicate = true;
			}
			else{
				throw new ValidationException("This person got a father already! Can not add another father to it!");
			}
		}
		
		return isDuplicate;
	}



	@Override
	public Set<Relation> afterAddingRelation(Relation relation) throws ServiceException {
		

		Set<Relation> newRelations = new HashSet<>();
		Relation rel = null;
		
		Person me = relation.getSource();
		Person father = relation.getDest();
		
		//If I have a mother, she should have my father as one of her spouses.
		Person mother = ServiceUtil.getParentOf(me, DBStore.MOTHER);
		if(mother != null){
			rel = new Relation(father, DBStore.getRelationship(DBStore.SPOUSE), mother);
			if( ! DBStore.isDuplicate(rel)){
				newRelations.add(rel);
			}
		}
		
		//Add the father to all my existing siblings
		Set<Relation> siblingRels = DBStore.getRelation(me, DBStore.SIBLING);
		for (Relation siblingRel : siblingRels) {
			rel = new Relation(siblingRel.getDest(), DBStore.getRelationship(DBStore.FATHER), father);
			if( ! DBStore.isDuplicate(rel)){
				newRelations.add(rel);
			}
		}
		
		LOG.info(newRelations.toString());
		return newRelations;
	}


	@Override
	public FamilyPerson connectFamilyMembers(FamilyPerson fPerson) throws ServiceException {
		
		if(fPerson == null || fPerson.isTraversed()){
			return null;
		}
	
		//TODO: validation of null and more no. of fathers!
		
		Set<Relation> fatherSet = DBStore.getRelation(fPerson.getPerson(), DBStore.FATHER);
		
		FamilyPerson father = null;
		
		for (Relation relation : fatherSet) {
			father = FamilyPersonFactory.getFamilyPerson(relation.getDest());
			fPerson.addParent(father);
		}
		
		return father;
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
