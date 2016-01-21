package api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import service.FamilyPerson;
import service.exc.ServiceException;
import service.exc.ValidationException;
import service.model.Person;
import service.model.Relationship;
import service.model.Sex;
import store.DBStore;

public class TestService {
	
	FamilyTreeService service = null;
	
	Relationship father = DBStore.getRelationship("FATHER");
	Relationship mother = DBStore.getRelationship("MOTHER");
	Relationship spouse = DBStore.getRelationship("SPOUSE");
	Relationship sibling = DBStore.getRelationship("SIBLING");
	
	Relationship child = DBStore.getRelationship("CHILD");
	Relationship grandparent = DBStore.getRelationship("GRANDPARENT");
	Relationship other = DBStore.getRelationship("OTHER");
	Relationship none = DBStore.getRelationship("NONE");
	
	Person siva = null;
	Person rama = null;
	Person rajya = null;
	Person mani = null;
	Person ramu = null;
	Person srinu = null;
	Person anantha = null;
	Person sita = null;
	Person vaish = null;
	Person varsh = null;
	Person chinnaV = null;
	Person peddaV = null;
	Person himu = null;
	Person ashrith = null;
	Person sashank = null;
	Person sarvani = null;
	
	Person subbaR = null;
	Person venkataR = null;
	
	Person athayya = null;
	
	Person sanyasiR = null;
	Person subhadra = null;
	
	Person peddaM = null;
	Person chinnaM = null;
	Person bulliM = null;
	Person pinnamma = null;
	
	@Before
	public void before(){
		System.out.println("@Before !!!!!!!!!!!!!");
		service = new FamilyTreeService();
		
		
		
		
		siva = new Person("Siva", "Datla", null, Sex.MALE);
		mani = new Person("Subramanya", "Datla", null, Sex.MALE);
		rama = new Person("RamaKrishna", "Datla", null, Sex.MALE);
		rajya = new Person("Rajya", "Datla", null, Sex.FEMALE);
		ramu = new Person("Ramu", "Datla", null, Sex.MALE);
		srinu = new Person("Srinu", "Datla", null, Sex.MALE);
		anantha = new Person("AnanthaLakshmi", "Datla", null, Sex.FEMALE);
		sita = new Person("Seetha", "", null, Sex.FEMALE);
		vaish = new Person("Vaishnavi", "", null, Sex.FEMALE);
		varsh = new Person("Varshini", "", null, Sex.FEMALE);
		chinnaV = new Person("ChinnaVadina", "", null, Sex.FEMALE);
		himu = new Person("Himaja", "", null, Sex.FEMALE);
		ashrith = new Person("Ashrith", "", null, Sex.MALE);
		peddaV = new Person("PeddaVadina", "", null, Sex.FEMALE);
		sashank = new Person("Sashank", "", null, Sex.MALE);
		sarvani = new Person("Sarvani", "", null, Sex.FEMALE);
		subbaR = new Person("SubbaRaju", "", null, Sex.MALE);
		venkataR = new Person("VenkataR", "", null, Sex.FEMALE);
		athayya = new Person("Athayya", "", null, Sex.FEMALE);
		sanyasiR = new Person("SanyasiRaju", "", null, Sex.MALE);
		subhadra = new Person("SubhadraDevi", "", null, Sex.FEMALE);
		peddaM = new Person("PeddaM", "", null, Sex.MALE);
		chinnaM = new Person("ChinnaM", "", null, Sex.MALE);
		bulliM = new Person("BulliM", "", null, Sex.MALE);
		pinnamma = new Person("Pinnamma", "", null, Sex.FEMALE);
		
		try {
			service.createPerson(siva);
			service.createPerson(mani);
			service.createPerson(rama);
			service.createPerson(rajya);
			service.createPerson(ramu);
			service.createPerson(srinu);
			service.createPerson(anantha);
			service.createPerson(sita);
			service.createPerson(vaish);
			service.createPerson(varsh);
			service.createPerson(chinnaV);
			service.createPerson(himu);
			service.createPerson(ashrith);
			service.createPerson(peddaV);
			service.createPerson(sashank);
			service.createPerson(sarvani);
			service.createPerson(subbaR);
			service.createPerson(venkataR);
			service.createPerson(athayya);
			service.createPerson(sanyasiR);
			service.createPerson(subhadra);
			service.createPerson(peddaM);
			service.createPerson(chinnaM);
			service.createPerson(bulliM);
			service.createPerson(pinnamma);
			
		} catch (FTException e) {
			e.printStackTrace();
		}
		
	}
	
	@After
	public void after(){
		DBStore.clearAll();
	}
	

	@Test
	public void test() {
		try {
			service.createRelation(siva, father, rama);
			service.createRelation(siva, mother, rajya);
			service.createRelation(rajya, spouse, rama);

			service.createRelation(rajya, spouse, rama);

			service.createRelation(rajya, spouse, rama);

			service.createRelation(rajya, spouse, rama);
			
			System.out.println("-----SIVA------");
			service.printFamilyTree(siva);
			System.out.println("-----RAMA------");
			service.printFamilyTree(rama);
			System.out.println("-----RAJYA-----");
			service.printFamilyTree(rajya);
		} catch (ServiceException e) {
			fail("Failed with exception " + e.getMessage());
		}
		
	}
	
	@Test
	public void testCreateRelationWithNotValidRelationship(){
		try {
			service.createRelation(rama, child, siva);
		} catch (ServiceException e) {
			assertTrue(true);
		}	
		
	}
	
	@Test
	public void testValidationOfSiblings(){
		try {
			service.createRelation(siva, father, rama);
			service.createRelation(mani, father, siva);
			service.createRelation(siva, sibling, mani);
		} catch (ValidationException e) {
			assertTrue(true);
		} catch (ServiceException e) {
			fail("Failed with exception " + e);
		}
	}
	
	
	@Test
	public void testSiblings(){
		try {
			service.createRelation(siva, father, rama);
			service.createRelation(mani, mother, rajya);
			service.createRelation(siva, sibling, mani);
			service.createRelation(mani, sibling, ramu);
			service.createRelation(rama, spouse, anantha);
			service.createRelation(srinu, mother, anantha);
			service.createRelation(srinu, father, rama);
			
			System.out.println("------rama------");
			service.printFamilyTree(rama);
		} catch (ServiceException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	
	}
	
	private void createRelations() throws ServiceException{
		service.createRelation(siva, father, rama);
		service.createRelation(mani, mother, rajya);
		service.createRelation(siva, sibling, mani);
		service.createRelation(mani, sibling, ramu);
		service.createRelation(rama, spouse, anantha);
		service.createRelation(srinu, mother, anantha);//Only one spouse to anantha, i.e. rama
		service.createRelation(srinu, father, rama);
		
		service.createRelation(sita, spouse, siva);
		service.createRelation(vaish, mother, sita);
		service.createRelation(varsh, sibling, vaish);
		service.createRelation(varsh, father, siva);
		
		
		service.createRelation(ramu, spouse, chinnaV);
		service.createRelation(ashrith, sibling, himu);
		service.createRelation(himu, sibling, ashrith);
		service.createRelation(ashrith, father, ramu);
		service.createRelation(ashrith, mother, chinnaV);
		
		service.createRelation(sashank, sibling, sarvani);
		service.createRelation(sarvani, mother, peddaV);
		service.createRelation(mani, spouse, peddaV);
		service.createRelation(sashank, father, mani);
		
		service.createRelation(rajya, father, sanyasiR);
		service.createRelation(rajya, sibling, chinnaM);
		service.createRelation(bulliM, sibling, peddaM);
		service.createRelation(bulliM, mother, subhadra);
		service.createRelation(pinnamma, sibling, rajya);
		service.createRelation(peddaM, sibling, chinnaM);
		
		service.createRelation(rama, father, subbaR);
		service.createRelation(rama, mother, venkataR);
		service.createRelation(rama, sibling, athayya);
	}

	@Test
	public void testFamilyTree4Siva() throws ServiceException{
		
		createRelations();
		
		FamilyPerson fPerson = service.printFamilyTree(siva);
		
		//itself!
		assertTrue(fPerson.getPerson().equals(siva));

		//parents
		int i = 0;
		for (FamilyPerson parent : fPerson.getParents()) {
			//father
			if(parent.getPerson().equals(rama))
				i++;
			else if(parent.getPerson().equals(rajya))
				i++;
		}
		
		//2 parents
		assertTrue(i == 2);
		
		
		//spouse - only one - that is sita!
		assertEquals(sita, fPerson.getSpouses().iterator().next().getPerson());
		
		//children
		i = 0;
		for (FamilyPerson child : fPerson.getChildren()) {
			
			if(child.getPerson().equals(vaish))
				i++;
			else if(child.getPerson().equals(varsh))
				i++;
		}
		
		//2 children
		assertTrue(i == 2);
		
		i = 0;
		//grandparents
		for (FamilyPerson parent : fPerson.getParents()) {
			for (FamilyPerson gParent : parent.getParents()) {
				
				if(gParent.getPerson().equals(subbaR))
					i++;
				else if(gParent.getPerson().equals(venkataR))
					i++;
				else if(gParent.getPerson().equals(sanyasiR))
					i++;
				else if(gParent.getPerson().equals(subhadra))
					i++;
			}
			
		}
		// 4 grandparents
		assertTrue(i == 4);
	}
	
	
	@SuppressWarnings("unused")
	@Test
	public void testFamilyTree4Rama() throws ServiceException{
		
		createRelations();
		
		FamilyPerson fPerson = service.printFamilyTree(rama);
		
		//itself!
		assertTrue(fPerson.getPerson().equals(rama));

		//parents
		int i = 0;
		for (FamilyPerson parent : fPerson.getParents()) {
			//father
			if(parent.getPerson().equals(subbaR))
				i++;
			else if(parent.getPerson().equals(venkataR))
				i++;
		}
		
		//2 parents
		assertTrue(i == 2);
		
		
		//spouse
		i = 0;
		for (FamilyPerson spouse : fPerson.getSpouses()) {
			if(spouse.getPerson().equals(rajya)){
				i++;
			}
			else if(spouse.getPerson().equals(anantha)){
				i++;
			}
				
		}
		
		//2 spouses for rama
		assertTrue(i == 2);
		
		
		//children
		i = 0;
		for (FamilyPerson child : fPerson.getChildren()) {
			
			if(child.getPerson().equals(siva))
				i++;
			else if(child.getPerson().equals(ramu))
				i++;
			else if(child.getPerson().equals(mani))
				i++;
			else if(child.getPerson().equals(srinu))
				i++;
		}
		
		//4 children
		assertTrue(i == 4);
		
		i = 0;
		//grandparents
		for (FamilyPerson parent : fPerson.getParents()) {
			for (FamilyPerson gParent : parent.getParents()) {
				i++;
			}
			
		}
		// 4 grandparents
		assertTrue(i == 0);
	}

	@Test
	public void testFT() throws ServiceException, FTException{
		createRelations();
		Person a = new Person("A", "", null, Sex.MALE);
		service.createPerson(a);
		
		Person b = new Person("B", "", null, Sex.MALE);
		service.createPerson(b);
		
		service.createRelation(a, father, b);
		
		service.printFamilyTree(siva);
	}
}
