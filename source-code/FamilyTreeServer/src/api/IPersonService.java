package api;

import java.util.Collection;

import service.model.Person;

/**
 * APIs/services available on Person object
 * 
 * @author Siva Datla
 *
 */
public interface IPersonService {

	public void createPerson(Person person) throws FTException;
	
	public Collection<Person> getPersons();
	
	public Person getPersonById(String id) ;
	
	public Person updatePerson(Person person);

		
//	public void deletePerson(String id) throws FTException;
}
