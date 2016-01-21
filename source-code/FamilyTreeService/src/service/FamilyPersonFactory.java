package service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import service.model.Person;

public class FamilyPersonFactory {
	
	private static HashMap<String, FamilyPerson> familyPersons = new HashMap<>();

	
	/**
	 * don't allow creation of new object of this class!
	 */
	private FamilyPersonFactory() {
	}
	
	
	/**
	 * Factory for FamilyPerson Objects
	 * 
	 * @param person
	 * @return
	 */
	public static FamilyPerson getFamilyPerson(Person person){
		FamilyPerson fPerson = null;
		
		if(person != null){
			fPerson = familyPersons.get(person.getId());
			
			if(fPerson == null){
				fPerson = new FamilyPerson(person);
				familyPersons.put(person.getId(), fPerson);
			}
		}
		
		return fPerson;
	}
	
	public static Collection<FamilyPerson> getAllFamilyPersons(){
		return familyPersons.values();
	}
	
	public static Set<Person> getAllPersons(){
		Set<Person> persons = new HashSet<>();
		
		for (FamilyPerson fPerson : familyPersons.values()) {
			persons.add(fPerson.getPerson());
		}
		
		return persons;
	}
	
	public static void clearCache(){
		familyPersons.clear();
	}
	
}
