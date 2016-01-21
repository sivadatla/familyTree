package service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import service.exc.ServiceException;
import service.model.Person;
import service.model.Relation;
import service.model.Relationship;
import service.util.ServiceUtil;
import store.DBStore;


/**
 * SP implementation for Relationship-Grandparent
 * @author Siva Datla
 *
 */
public class GrandParentRelationSPI implements IRelationService{

	@Override
	public Relationship getRelationship() {
		
		return DBStore.getRelationship("GRANDPARENT");
	}

	@Override
	public Set<Person> process(Person person) throws ServiceException {
		
		Set<Relation> father = DBStore.getRelation(person, DBStore.FATHER);
		Set<Relation> mother = DBStore.getRelation(person, DBStore.MOTHER);
		
		Set<Relation> grandParentsRel = new HashSet<>();
		
		for (Relation rel : father) {
			grandParentsRel.addAll(DBStore.getRelation(rel.getDest(), DBStore.FATHER));
			grandParentsRel.addAll(DBStore.getRelation(rel.getDest(), DBStore.MOTHER));
		}

		for (Relation rel : mother) {
			grandParentsRel.addAll(DBStore.getRelation(rel.getDest(), DBStore.FATHER));
			grandParentsRel.addAll(DBStore.getRelation(rel.getDest(), DBStore.MOTHER));
		}
		
		Set<Person> grandParents = new HashSet<>();
		
		for (Relation rel : grandParentsRel) {
			grandParents.add(rel.getDest());
		}
		
		return grandParents;
	}
	
	@Override
	public FamilyPerson connectFamilyMembers(FamilyPerson fPerson) throws ServiceException {
	
		//TODO: validation of null.
		
		Set<FamilyPerson> fParents = fPerson.getParents();
		for (FamilyPerson fParent : fParents) {
			Person father = ServiceUtil.getParentOf(fParent.getPerson(), DBStore.FATHER);
			fParent.addParent(FamilyPersonFactory.getFamilyPerson(father));
			
			Person mother = ServiceUtil.getParentOf(fParent.getPerson(), DBStore.MOTHER);
			fParent.addParent(FamilyPersonFactory.getFamilyPerson(mother));
		}
		
		
		return fPerson;
	}
	
	@Override
	public List<Person> print(Person person) throws ServiceException {
		
		FamilyPerson fPerson = FamilyPersonFactory.getFamilyPerson(person);
		
		List<Person> grandParents = new ArrayList<>();
		
		int i = 0;
		if(fPerson.getParents() != null){
			for (FamilyPerson parent : fPerson.getParents()) {
				for(FamilyPerson gParent : parent.getParents()){
					grandParents.add(i++, gParent.getPerson());
				}
			}
		}
		// TODO Auto-generated method stub
		return grandParents;
	}
}
