package controllers;

import static play.libs.Json.toJson;

import java.util.Collection;

import com.fasterxml.jackson.databind.JsonNode;

import api.FamilyTreeService;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import service.exc.ServiceException;
import service.model.Person;
import service.model.Relation;
import service.model.Relationship;


/**
 * REST API controller for PLAY
 * 
 * UI built around non-play technologies can use this!
 * 
 * Convention in route file is that, all URIs like /api/{xyz} are defined here.
 * 
 * @author Siva Datla
 *
 */
public class RESTController  extends Controller {

	private FamilyTreeService ftService = null;
	
	/**
	 * Constructor 
	 */
	public RESTController() {
		ftService = new FamilyTreeService();
	}

	
	/**
	 * Gets all persons
	 * 
	 * REST METHOD: GET 
	 * REST URL: /person
	 * 
	 * @return
	 */
    public Result getPersons() {
    	Collection<Person> persons = ftService.getPersons();
    	
        return ok(toJson(persons));
    }
    
    
    /**
     * Add a new person
     * 
	 * REST METHOD: POST 
	 * REST URL: /person
	 * 
     * @return
     */
    public Result addPerson(){
    	 Person person = Form.form(Person.class).bindFromRequest().get();
    	 Person newPerson = null;
         try{
        	 ftService.createPerson(person);
        	 newPerson = ftService.getPersonById(person.getId());
         }catch(Exception e){
         	Logger.error("Exception while adding a person", e);
         	return badRequest(person.toString());
         }
         return created(toJson(newPerson));
    }
    
    
    /**
     * updates an existing person
     * 
	 * REST METHOD: PUT
	 * REST URL: /person
     * @return
     */
    public Result updatePerson(){
    	 Person person = Form.form(Person.class).bindFromRequest().get();
         ftService.updatePerson(person);
         
         return ok(toJson(person));
         
    }
    
    
    /**
     * Gets all relationships
     * 
	 * REST METHOD: GET 
	 * REST URL: /relationship
     * @return
     */
    public Result getRelationships(){
    	Collection<Relationship> relationships = ftService.getAllRelationships();
    	return ok(toJson(relationships));
    }
    
    
    /**
     * Gets all relations
     * 
	 * REST METHOD: GET 
	 * REST URL: /relation
     * @return
     */
    public Result getRelations(){
    	Collection<Relation> relations = ftService.getRelations();
    	return ok(toJson(relations));
    }
    
    /**
     * Add a relation between two persons
     * 
	 * REST METHOD: PUT
	 * REST URL: /relation
     * @return
     */
    public Result addRelation(){
    	JsonNode json = request().body().asJson();
    	if(json == null) {
    		return badRequest("Expecting Json data");
  	  	} else {
  	  		String sourcePersonId = json.findPath("sourcePersonId").asText();
  	  		if(sourcePersonId == null) {
  	  			return badRequest("Missing parameter [sourcePersonId]");
  	  		}
  	  		
  	  		String destPersonId = json.findPath("destPersonId").asText();
	  		if(destPersonId == null) {
	  			return badRequest("Missing parameter [destPersonId]");
	  		}
  	  		
	  		String relationshipId = json.findPath("relationshipId").asText();
  	  		if(relationshipId == null) {
  	  			return badRequest("Missing parameter [relationshipId]");
  	  		}
  	  		
  	  		//Load source Person
  	  		Person sourcePerson = ftService.getPersonById(sourcePersonId);
  	  		if(sourcePerson == null){
  	  			return badRequest("There is no person with given Source Person id : " + sourcePersonId);
  	  		}
  	  		
  	  		//Load dest Person
  	  		Person destPerson = ftService.getPersonById(destPersonId);
  	  		if(destPerson == null){
	  			return badRequest("There is no person with given Destination Person id : " + destPersonId);
	  		}
  	  		
  	  		Relationship relationship = ftService.getRelationshipById(relationshipId);
  	  		if(relationship == null){
  	  			return badRequest("There is no relationship with give relationshipId : " + relationshipId);
  	  		}
  	  		
  	  		Relation newRelation;
			try {
				newRelation = ftService.createRelation(sourcePerson, relationship, destPerson);
			} catch (ServiceException e) {
				Logger.error("Exception while creating relation ", e);
				return badRequest("Exception while creating relation ", e.getMessage());
			}
  	  	
  	  		
  	  		return created(toJson(newRelation));
  	  		
  	  	}
    	
    }
    
    
    /**
     * prints Family Tree of the given person
     * 
     * REST METHOD: GET
	 * REST URL: /familyTree?id=?
     * @return
     */
    public Result printFamilyTree(){
    	String id = request().getQueryString("id");
    	
    	Person person = ftService.getPersonById(id);
    	if(person == null){
	  		return badRequest("There is no person with give id : " + id);
	  	}
    	
    	ftService.printFamilyTree(person);
    	
    	return ok();
    	
    }
    
}
