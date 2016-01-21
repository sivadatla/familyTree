package service.model;

import java.io.Serializable;
import java.util.Date;


/**
 * Person POJO class
 * 
 * @author Siva Datla
 *
 */
public class Person implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int personIdCount = 1;

	private String id;

	private String firstName;

	private String lastName;

	private Date dob;

	private Sex sex;
	
//	private boolean isTraversed = false;
	
	public Person(){
		this.id = "person-" + personIdCount++;
	}

	public Person(String firstName, String lastName, Date dob, Sex sex) {
		super();
		this.id = "person-" + personIdCount++;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.sex = sex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dob == null) ? 0 : dob.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
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
		Person other = (Person) obj;
		if (dob == null) {
			if (other.dob != null)
				return false;
		} else if (!dob.equals(other.dob))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (sex != other.sex)
			return false;
		return true;
	}

	@Override
	public String toString() {
//		return "Person [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", dob=" + dob + ", sex="
//				+ sex + "]";
		return "firstName=" + firstName + ", lastName=" + lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
//
//	/**
//	 * @return the isTraversed
//	 */
//	public boolean isTraversed() {
//		return isTraversed;
//	}
//
//	/**
//	 * @param isTraversed 
//	 */
//	public void setTraversed(boolean isTraversed) {
//		this.isTraversed = isTraversed;
//	}
}
