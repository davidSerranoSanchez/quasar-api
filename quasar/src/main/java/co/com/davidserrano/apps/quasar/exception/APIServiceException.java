package co.com.davidserrano.apps.quasar.exception;

public class APIServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	private final APIServiceErrorCodes code;

	public APIServiceException(final String message, final APIServiceErrorCodes code) {
		super(message);
		this.code = code;
	}

	public APIServiceErrorCodes getCode() {
		return code;
	}

}
