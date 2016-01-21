package service.model;

import java.io.Serializable;

/**
 * Relationship POJO
 * 
 * Defines a name to relationship
 * 
 * @author Siva Datla
 *
 */
public class Relationship implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
//	private String serviceName;
	private boolean validForNewRelation;
	private int depth;
	
	public Relationship(){
		
	}
	
	public Relationship(String id, String name, boolean validForNewRelation, int depth) {
		super();
		this.id = id;
		this.name = name;
//		this.serviceName = serviceName;
		this.validForNewRelation = validForNewRelation;
		this.depth = depth;
	}

	
	@Override
	public String toString() {
		return "Relationship [id=" + id + ", name=" + name + "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Relationship other = (Relationship) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}


//	public String getServiceName() {
//		return serviceName;
//	}
//
//
//	public void setServiceName(String serviceName) {
//		this.serviceName = serviceName;
//	}


	/**
	 * @return the validForNewRelation
	 */
	public boolean isValidForNewRelation() {
		return validForNewRelation;
	}


	/**
	 * @param validForNewRelation the validForNewRelation to set
	 */
	public void setValidForNewRelation(boolean validForNewRelation) {
		this.validForNewRelation = validForNewRelation;
	}

	/**
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * @param depth the depth to set
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	
}
