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
import service.model.Relationship;
import store.DBStore;


/**
 * SP implementation for Relationship-NONE
 * @author Siva Datla
 *
 */
public class NoneRelationSPI implements IRelationService {

	@Override
	public Relationship getRelationship() {
		return DBStore.getRelationship("NONE");
	}

	@Override
	public Set<Person> process(Person person) throws ServiceException {
		
		//NONE - All Persons - full family tree
		
		System.out.println(DBStore.getPersons().size());
		
		Set<Person> allPersons = new HashSet<>();
		allPersons.addAll(DBStore.getPersons());
		System.out.println(allPersons.size() + "#########");
		
		Set<Person> allFamilyPersons = FamilyPersonFactory.getAllPersons();

		System.out.println(allFamilyPersons.size() + "#########");
		allPersons.removeAll(allFamilyPersons);
		
		
		// TODO Auto-generated method stub
		return allPersons;
	}
	
	@Override
	public List<Person> print(Person person) throws ServiceException {
		//NONE - All Persons - full family tree
		
		List<Person> allPersons = new ArrayList<>();
		
		allPersons.addAll(DBStore.getPersons());
		
		Set<Person> allFamilyPersons = FamilyPersonFactory.getAllPersons();

		allPersons.removeAll(allFamilyPersons);
		
		return allPersons;
	}
}
