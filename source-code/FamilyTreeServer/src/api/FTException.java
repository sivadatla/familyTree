package api;

/**
 * FamilyTree base exception
 * 
 * @author Siva Datla
 *
 */
public class FTException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FTException() {
		super();
	}

	public FTException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FTException(String message, Throwable cause) {
		super(message, cause);
	}

	public FTException(String message) {
		super(message);
	}

	public FTException(Throwable cause) {
		super(cause);
	}
	
	

}
