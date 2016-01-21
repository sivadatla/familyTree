/**
 * 
 */
package service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import service.exc.ServiceException;
import service.exc.ValidationException;
import service.model.Person;
import service.model.Relation;
import service.model.Relationship;
import service.util.ServiceUtil;
import store.DBStore;


/**
 * SP implementation for Relationship-SIBLING
 * @author Siva Datla
 *
 */
public class SiblingRelationSPI implements IRelationService {

	@Override
	public boolean beforeAddingRelation(Relation relation) throws ValidationException {

		
		Person me = relation.getSource();
		Person sibling = relation.getDest();
		
		Person myFather = ServiceUtil.getParentOf(me, DBStore.FATHER);
		Person fatherOfSibling = ServiceUtil.getParentOf(sibling, DBStore.FATHER);
		if(myFather != null && fatherOfSibling != null && ! myFather.equals(fatherOfSibling)){
			throw new ValidationException("Father of these siblings doesn't match!");
		}
		
		Person myMother = ServiceUtil.getParentOf(me, DBStore.MOTHER);
		Person motherOfSibling = ServiceUtil.getParentOf(sibling, DBStore.MOTHER);
		if(myMother != null && motherOfSibling != null && ! myMother.equals(motherOfSibling)){
			throw new ValidationException("Mother of these siblings doesn't match!");
		}
		
		return DBStore.isDuplicate(relation);
	}
	
	@Override
	public Set<Relation> afterAddingRelation(Relation relation) throws ServiceException {

		Set<Relation> newRels = new HashSet<>();
		
		Relation rel = null;

		//Add the relation in reverse way
		rel = new Relation(relation.getDest(), relation.getRelationship(), relation.getSource());
		
		if( ! DBStore.isDuplicate(rel)){
			newRels.add(rel);
		}

		
		Person me = relation.getSource();
		Person sibling = relation.getDest();

		//add the sibling to all my existing siblings
		Set<Relation> sibRels = DBStore.getRelation(me, DBStore.SIBLING);
		for (Relation sibRel : sibRels) {
			if(sibling.equals(sibRel.getDest())) continue;
			rel = new Relation(sibRel.getDest(), DBStore.getRelationship(DBStore.SIBLING), sibling);
			if( ! DBStore.isDuplicate(rel)){
				newRels.add(rel);
			}
		}

		//
		
		//sync father
		Person myFather = ServiceUtil.getParentOf(me, DBStore.FATHER);
		Person fatherOfSibling = ServiceUtil.getParentOf(sibling, DBStore.FATHER);
		if(myFather == null && fatherOfSibling != null){
			rel = new Relation(me, DBStore.getRelationship(DBStore.FATHER), fatherOfSibling);
			if( ! DBStore.isDuplicate(rel)){
				newRels.add(rel);
			}
		}
		//
		
		//sync mother
		Person myMother = ServiceUtil.getParentOf(me, DBStore.MOTHER);
		Person motherOfSibling = ServiceUtil.getParentOf(sibling, DBStore.MOTHER);
		
		if(myMother == null && motherOfSibling != null){
			rel = new Relation(me, DBStore.getRelationship(DBStore.MOTHER), motherOfSibling);
			if( ! DBStore.isDuplicate(rel)){
				newRels.add(rel);
			}
		}
		
		
		LOG.info(newRels.toString());
		return newRels;
	}
	
	@Override
	public Relationship getRelationship() {
		return DBStore.getRelationship("SIBLING");
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
	public List<Person> print(Person person) throws ServiceException {

		List<Person> persons = new ArrayList<>();
		
		Set<Relation> relations = DBStore.getRelation(person, getRelationship());
		
		for (Relation relation : relations) {
			persons.add(relation.getDest());
		}
		
		return persons;
	}

}
