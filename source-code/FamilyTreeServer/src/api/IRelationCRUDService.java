package api;

import java.util.Collection;

import service.exc.ServiceException;
import service.model.Person;
import service.model.Relation;
import service.model.Relationship;

/**
 * Interface of Relation Service
 * 
 * @author Siva Datla
 *
 */
public interface IRelationCRUDService {

	public Relation createRelation(Person a, Relationship r, Person b) throws ServiceException;
	public void createRelation(String sourcePersonId, String relationshipId, String destPersonId) throws ServiceException;
	public void createRelation(Relation relation) throws ServiceException;
	
	
	public Collection<Relation> getRelations();
	public Relation getRelationByid(String id);
	
	//TODO:
	//delete
	//update
}
