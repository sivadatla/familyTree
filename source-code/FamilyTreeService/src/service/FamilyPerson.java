package service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import service.model.Person;

public class FamilyPerson {
	
	private Person person;
	
	private Set<FamilyPerson> parents;
	
	private Set<FamilyPerson> spouses;
	
	private Set<FamilyPerson> children;
	
	private boolean isTraversed;

	FamilyPerson(Person person){
		this.person = person;
		this.isTraversed = false;
		this.spouses = new HashSet<>();
		this.children = new HashSet<>();
		this.parents = new HashSet<>();
	}
	
	public void addSpouse(FamilyPerson person){
		this.spouses.add(person);
	}
	
	public void addChildren(FamilyPerson person){
		this.children.add(person);
	}
	
//	public void addAllChildren(Set<FamilyPerson> person){
//		this.children.addAll(person);
//	}
	
	public void addParent(FamilyPerson person){
		this.parents.add(person);
	}
	
	public Set<FamilyPerson> getParents(){
		return Collections.unmodifiableSet(parents);
	}
	
	public Set<FamilyPerson> getChildren(){
		return Collections.unmodifiableSet(children);
	}
	
	public Set<FamilyPerson> getSpouses(){
		return Collections.unmodifiableSet(spouses);
	}

	public Person getPerson(){
		return this.person;
	}
	
	public boolean isTraversed() {
//		return this.getPerson().isTraversed();
		return this.isTraversed;
	}
	
	public void setTraversed(boolean isTraversed){
//		this.getPerson().setTraversed(isTraversed);
		this.isTraversed = isTraversed;
	}

	@Override
	public String toString() {
		return person.toString();
//		return "FamilyPerson [person=" + person + ", parents=" + parents + ", spouses=" + spouses + ", children="
//				+ children + "]";
	}
	
	
	
}
