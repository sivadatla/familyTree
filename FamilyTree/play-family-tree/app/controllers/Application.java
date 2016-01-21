package controllers;

import play.*;
import play.mvc.*;
import service.model.Person;
import service.model.Relationship;
import views.html.*;
import play.data.Form;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import api.FamilyTreeService;

import static play.libs.Json.*;

/**
 * The one of the PLAY controllers
 * 
 * TODO: This shall be used for UI.
 * 
 * @author Siva Datla
 *
 */
public class Application extends Controller {
	
	private FamilyTreeService ftService = null;
	public Application() {
		ftService = new FamilyTreeService();
	}

    public Result index() {
        return ok(index.render());
    }

//    @Transactional
    public Result addPerson() {
        Person person = Form.form(Person.class).bindFromRequest().get();
        try{
        ftService.createPerson(person);
//        JPA.em().persist(person);
        }catch(Exception e){
        	e.printStackTrace();
        }
        return redirect(routes.Application.index());
    }

//    @Transactional(readOnly = true)
    public Result getPersons() {
//        List<Person> persons = (List<Person>) JPA.em().createQuery("select p from Person p").getResultList();
//        return ok(toJson(persons));
    	Collection<Person> persons = ftService.getPersons();
    	System.out.println(persons.size());
    	if(persons.size() > 0){
    		System.out.println(persons.iterator().next());
    	}
    	return ok(toJson(persons));
    }
    
    public Result getAllRelationships(){
    	
    	Collection<Relationship> relationships = ftService.getAllRelationships();
    	
    	return ok(toJson(relationships));
    }
}
