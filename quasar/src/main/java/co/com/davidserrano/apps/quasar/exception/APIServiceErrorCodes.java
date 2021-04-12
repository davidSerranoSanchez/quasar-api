package co.com.davidserrano.apps.quasar.exception;

import org.springframework.http.HttpStatus;

public enum APIServiceErrorCodes implements IAPIServiceErrorMsg {

    GENERAL_EXCEPTION("general.exception", HttpStatus.INTERNAL_SERVER_ERROR),
    LOCATION_NOT_DEFINABLE("quasar.location.exception", HttpStatus.NOT_FOUND),
    MESSAGE_NOT_DEFINABLE("quasar.message.exception", HttpStatus.NOT_FOUND),
    NOT_ENOUGH_INFORMATION("quasar.amountinfo.exception", HttpStatus.NOT_FOUND);

    private String message;
    private HttpStatus httpStatus;
    private String errorDetail;

    private APIServiceErrorCodes(final String message, final HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
