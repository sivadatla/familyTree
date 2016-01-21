package service;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import service.exc.ServiceException;
import service.exc.ValidationException;
import service.model.Person;
import service.model.Relation;
import service.model.Relationship;


/**
 * Service provider interface.
 * We expect each relationship created shall have a corresponding implementation in classpath.
 * 
 * 
 * @author Siva Datla
 *
 */
public interface IRelationService {

	static final Logger LOG = Logger.getLogger(IRelationService.class.getName());
	
	/**
	 * This method shall be invoked before adding a relation into persistence storage
	 * 
	 * @param relation
	 * @return
	 * @throws ValidationException
	 */
	default boolean beforeAddingRelation(Relation relation) throws ValidationException{
		LOG.info("This is default implementation of IRelationService!");
		if(!relation.getRelationship().isValidForNewRelation()){
			throw new ValidationException("Given relationship is not allowed to create a relation");
		}
		return false;
	}
	
	/**
	 * This method shall be invoked after successful creation of Relation
	 * @param relation
	 * @return
	 * @throws ServiceException
	 */
	default Set<Relation> afterAddingRelation(Relation relation) throws ServiceException{
		LOG.info("This is default implementation of IRelationService!");
		return null;
	}
	
	
	/**
	 * This method shall be invoked while processing family tree
	 * @param person
	 * @return
	 * @throws ServiceException
	 */
	default Set<Person> process(Person person) throws ServiceException{
		LOG.info("This is default implementation of IRelationService!");
		
		return null;
	}
	
	default List<Person> print(Person person) throws ServiceException{
		return null;
	}
	
	default FamilyPerson connectFamilyMembers(FamilyPerson fPerson) throws ServiceException{
		return null;
	}
	
	/**
	 * non-default method - should override by each Relationship defined.
	 * It is expected to return corresponding relationship from this method implementation.
	 * 
	 * @return
	 */
	Relationship getRelationship();
}
