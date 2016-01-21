package api.spi;

import java.util.ServiceLoader;
import java.util.logging.Logger;

import service.IRelationService;
import service.model.Relationship;

/**
 * ServiceLoader helper class.
 * 
 * 
 * @author Siva Datla
 *
 */
public interface RelationServiceLoader {

	static final Logger LOG = Logger.getLogger(RelationServiceLoader.class.getName());
	
	/**
	 * Loads Service Provider Implementation class from classpath.
	 * 
	 * We expect an implementation is provided with each Relationship defined.
	 * 
	 * @param relationship
	 * @return
	 */
	public static IRelationService loadRelationService(Relationship relationship){
		IRelationService service = null;
		
		ServiceLoader<IRelationService> services = ServiceLoader.load(IRelationService.class);
		for (IRelationService relationService : services) {
			if(relationship.equals(relationService.getRelationship())){
				service = relationService;
				break;
			}
		}
	
		if(service == null){
			LOG.warning("There is no service provider for the given relationship!!!");
		}
		
		return service;
	}
}
