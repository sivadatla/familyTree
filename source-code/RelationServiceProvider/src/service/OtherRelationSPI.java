/**
 * 
 */
package service;

import service.model.Relationship;
import store.DBStore;


/**
 * SP implementation for Relationship-OTHER
 * @author Siva Datla
 *
 */
public class OtherRelationSPI implements IRelationService {

	@Override
	public Relationship getRelationship() {
		return DBStore.getRelationship("OTHER");
	}
	
	

}
