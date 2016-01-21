package service.util;

import java.util.Set;

import service.model.Person;
import service.model.Relation;
import store.DBStore;

/**
 * UTILITY class
 * 
 * @author Siva Datla
 *
 */
public interface ServiceUtil {

	/**
	 * Gets parent (person) by relationship (FATHER/MOTHER) 
	 * 
	 * @param person
	 * @param relationshipId
	 * @return
	 */
	public static Person getParentOf(Person person, String relationshipId){
		Person parent = null;
		
		Set<Relation> parentRel = DBStore.getRelation(person, relationshipId);
		if(parentRel != null && parentRel.size() > 0){
			parent = parentRel.iterator().next().getDest();
		}

		return parent;
	}
}
