/**
 * 
 */
package service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import service.exc.ServiceException;
import service.model.Person;
import service.model.Relation;
import service.model.Relationship;
import service.model.Sex;
import store.DBStore;

/**
 * SPI for Child Relation
 * 
 * @author Siva Datla
 *
 */
public class ChildRelationSPI implements IRelationService {

	@Override
	public Relationship getRelationship() {
		return DBStore.getRelationship("CHILD");
	}
	
	@Override
	public Set<Person> process(Person person) throws ServiceException {
		Set<Person> persons = new HashSet<>();
		
		boolean isFather = false;
		if(person.getSex() == Sex.MALE){
			isFather = true;
		}
		
		Set<Relation> spouses = DBStore.getRelation(person, DBStore.SPOUSE);
		
		Set<Person> children = null;
		for (Relation spouse : spouses) {
				
			if( ! isFather){
				children = getChildren(person, spouse.getDest());
			}
			else{
				children = getChildren(spouse.getDest(), person);
			}

			persons.addAll(children);
				
		}
		
		return persons;
	}


	private Set<Person> getChildren(Person mother, Person father){
		
		Set<Relation> childsOfMotherRel = DBStore.getReverseRelation(DBStore.getRelationship(DBStore.MOTHER), mother);
		Set<Relation> childsOfFatherRel = DBStore.getReverseRelation(DBStore.getRelationship(DBStore.FATHER), father);
		
		Set<Person> childsOfMother = new HashSet<>();
		for (Relation childRel : childsOfMotherRel) {
			childsOfMother.add(childRel.getSource());
		}
		
		
		Set<Person> childs = new HashSet<>();
		
		for (Relation rel : childsOfFatherRel) {
			if(childsOfMother.contains(rel.getSource())){
				childs.add(rel.getSource());
			}
		}
		
		return childs;
	}
	
	@Override
	public FamilyPerson connectFamilyMembers(FamilyPerson fPerson) throws ServiceException {
	
		if(fPerson == null || fPerson.isTraversed()){
			return null;
		}
		
		Set<Person> children = process(fPerson.getPerson());
		
		for (Person child : children) {
			fPerson.addChildren(FamilyPersonFactory.getFamilyPerson(child));
		}
		
		//NOTE: it returns parent - caller should invoke getChildren() to get his/her children.
		return fPerson;
	}
	
	@Override
	public List<Person> print(Person person) throws ServiceException {

		List<Person> persons = new ArrayList<>();
		
		boolean isFather = false;
		if(person.getSex() == Sex.MALE){
			isFather = true;
		}
		
		Set<Relation> spouses = DBStore.getRelation(person, DBStore.SPOUSE);
		
		Set<Person> children = null;
		for (Relation spouse : spouses) {
				
			if( ! isFather){
				children = getChildren(person, spouse.getDest());
			}
			else{
				children = getChildren(spouse.getDest(), person);
			}

			persons.addAll(children);
				
		}
		
		return persons;
	}
}
