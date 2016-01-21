package service.model;

import java.io.Serializable;

/**
 * Relation POJO
 * Relation represents relation between two persons
 * 
 * It is actually a tuple of thisPerson, relationship (with), thatPeron
 * 
 * @author Siva Datla
 *
 */
public class Relation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int relationIdCount = 1;
	
	private String id;
	
	private Person source;
	
	private Relationship relationship;
	
	private Person dest;
	
	public Relation(){
		this.id = "rel-" + relationIdCount++;
	}

	public Relation(Person source, Relationship relationship, Person dest) {
		super();
		this.id = "rel-" + relationIdCount++;
		this.source = source;
		this.relationship = relationship;
		this.dest = dest;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dest == null) ? 0 : dest.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((relationship == null) ? 0 : relationship.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relation other = (Relation) obj;
		if (dest == null) {
			if (other.dest != null)
				return false;
		} else if (!dest.equals(other.dest))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (relationship != other.relationship)
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}



	@Override
	public String toString() {
//		return "Relation [id=" + id + ", source=" + source + ", relationship=" + relationship + ", dest=" + dest + "]";
		
		return source.getFirstName() + "  " + relationship.getId() + "  " + dest.getFirstName();
	}



	public Person getSource() {
		return source;
	}

	public void setSource(Person source) {
		this.source = source;
	}

	public Relationship getRelationship() {
		return relationship;
	}

	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}

	public Person getDest() {
		return dest;
	}

	public void setDest(Person dest) {
		this.dest = dest;
	}

	public String getId() {
		return id;
	}
	
	
	


}
