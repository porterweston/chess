package service;

public class ErrorException extends Exception{
    //Error exception thrown by service classes detailing what error occurred, along with the error code.
    public int errorCode;

    public ErrorException(int errorCode, String message) {
        super(String.format("%s %s", "Error:", message));
        this.errorCode = errorCode;
    }
}
