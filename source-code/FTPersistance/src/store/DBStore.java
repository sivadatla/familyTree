package store;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import service.model.Person;
import service.model.Relation;
import service.model.Relationship;

/**
 * Persistence store (temporary)
 * 
 * TODO: replace this class with actual DB persistency.
 * 
 * @author Siva Datla
 *
 */
public class DBStore {

	public static final String MOTHER = "MOTHER";
	public static final String FATHER = "FATHER";
	public static final String SPOUSE = "SPOUSE";
	public static final String SIBLING = "SIBLING";
	public static final String OTHER = "OTHER";
	public static final String NONE = "NONE";
	public static final String CHILD = "CHILD";
	public static final String GRANDPARENT = "GRANDPARENT";
	
	
	private static Map<String, Person> persons = new HashMap<>();
	private static Map<String, Relation> relations = new HashMap<>();
	private static Map<String, Relationship> relationships = new HashMap<>();
	
	static{
		relationships.put(MOTHER, new Relationship(MOTHER, MOTHER, true, -1));
		relationships.put(FATHER, new Relationship(FATHER, FATHER, true, -1));
		relationships.put(SPOUSE, new Relationship(SPOUSE, SPOUSE, true, 0));
		relationships.put(SIBLING, new Relationship(SIBLING, SIBLING, true, 0));
		relationships.put(OTHER, new Relationship(OTHER, OTHER, false, 90));
		relationships.put(NONE, new Relationship(NONE, NONE, false,100));
		relationships.put(CHILD, new Relationship(CHILD, CHILD, false, 1));
		relationships.put(GRANDPARENT, new Relationship(GRANDPARENT, GRANDPARENT, false, -2));
	}
	
	private DBStore(){
	}
	
	/**
	 * SQL: INSERT INTO person(....)
	 * @param person
	 */
	public static void addPerson(Person person){
		
		persons.put(person.getId(), person);
	}
	
	
	/**
	 * SQL: INSERT INTO relation(...)
	 * @param relation
	 */
	public static void addRelation(Relation relation){
		relations.put(relation.getId(), relation);
	}
	
	/**
	 * SQL: SELECT * FROM person WHERE id = ?
	 * @param id
	 * @return
	 */
	public static Person getPersonById(String id){
		return persons.get(id);
	}
	
	
	/**
	 * Gives all persons
	 * @return
	 */
	public static Collection<Person> getPersons() {
		return Collections.unmodifiableCollection(persons.values());
	}
	
	
	
	/**
	 * update a person
	 * 
	 * UPDATE person SET (dob=?, sex = ?, firstName = ?, lastName=?) WHERE id = ?
	 * 
	 * @param person
	 * @return
	 */
	public static Person updatePerson(Person person) {
		Person existingPerson = persons.get(person.getId());
		
		if(existingPerson != null){
			existingPerson.setDob(person.getDob());
			existingPerson.setFirstName(person.getFirstName());
			existingPerson.setLastName(person.getLastName());
			existingPerson.setSex(person.getSex());
			
		}
		else{
			throw new IllegalArgumentException("there is no person with given id!");
		}
	
		return existingPerson;
	}
	
	
	
	
	/**
	 * SQL: SELECT * FROM relation WHERE source_id = ?
	 * @param person
	 * @return
	 */
	public static Set<Relation> getRelation(Person person){
		Set<Relation> rels = relations.values().parallelStream().filter(e -> e.getSource().equals(person)).collect(Collectors.toSet());
		return rels;
	}
	
	/**
	 * SQL: SELECT * FROM relation WHERE source_id = ? AND relationship_id = ?
	 * @param person
	 * @param relationship
	 * @return
	 */
	public static Set<Relation> getRelation(Person person, Relationship relationship){
		Set<Relation> rels = relations.values()
				.parallelStream()
				.filter(e -> e.getSource().equals(person) 
						&& e.getRelationship().equals(relationship))
				.collect(Collectors.toSet());
		return rels;
	}
	
	/**
	 * Gives a relation by given person (as source) and relationship
	 * 
	 * SQL: SELECT dest_id FROM relation WHERE source_id = person.getId() AND relationsipId = relationshipId
	 * 
	 * @param person
	 * @param relationshipId
	 * @return
	 */
	public static Set<Relation> getRelation(Person person, String relationshipId){
		Set<Relation> rels = relations.values()
				.parallelStream()
				.filter(e -> e.getSource().equals(person) 
						&& e.getRelationship().getId().equals(relationshipId))
				.collect(Collectors.toSet());
		return rels;
	} 
	
	/**
	 * Gets a Relation by its ID, if exists, null otherwise
	 * 
	 * SQL: SELECT * FROM relation WHERE id = ?
	 * 
	 * @param relationId
	 * @return
	 */
	public static Relation getRelationById(String relationId){
		return relations.get(relationId);
	}
	
	/**
	 * Gets All relations from the persistence storage
	 * 
	 * SQL: SELECT * FROM relation 
	 * 
	 * @return
	 */
	public static Collection<Relation> getRelations() {
		return Collections.unmodifiableCollection(relations.values());
	}
	
	
	/**
	 * Gives a Reverse relation - 
	 * means given person is associated to someX person with given relationship
	 * 
	 * SQL: SELECT * from relation WHERE relationshipId = ? (relationship.id) AND dest_id = ? (person.id)
	 * 
	 * @param relationship
	 * @param person
	 * @return
	 */
	public static Set<Relation> getReverseRelation(Relationship relationship, Person person){
		Set<Relation> rels = relations.values()
				.parallelStream()
				.filter(e -> e.getDest().equals(person) 
						&& e.getRelationship().equals(relationship))
				.collect(Collectors.toSet());
		return rels;
	}
	
	
	
	
	
	/**
	 * SQL: SELECT * FROM relationship WHERE id = ?
	 * @param id
	 * @return
	 */
	public static Relationship getRelationship(String id){
		return relationships.get(id);
	}
	
	
	/**
	 * SELECT * from relationships
	 * @return
	 */
	public static Collection<Relationship> getRelationships(){
		return Collections.unmodifiableCollection(relationships.values());
	}
	
	
	
	
	
	
	
	
	/**
	 * Verifies, if the given relation is already exists in DB.
	 * 
	 * @param relation
	 * @return
	 */
	public static boolean isDuplicate(Relation relation){
		
		Set<Relation> rels = DBStore.getRelation(relation.getSource(), relation.getRelationship());
		Optional<Relation> optionalRel = rels.parallelStream().filter(e -> e.getDest().equals(relation.getDest())).findFirst();
		
		return optionalRel.isPresent();
	}
	
	
	
	/**
	 * Clears Persons and relations objects!
	 * Helpful for JUnit
	 */
	public static void clearAll(){
		persons.clear();
		relations.clear();
		//Intentionally not clearing relationships! - they are kind of static/domain data
	}

}
