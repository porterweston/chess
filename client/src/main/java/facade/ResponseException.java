package facade;

public class ResponseException extends Exception{
    //exception thrown by server facade detailing what went wrong along with an error code
    public int errorCode;

    public ResponseException(int errorCode, String message) {
        super(String.format("%s %s", "Error:", message));
        this.errorCode = errorCode;
    }

}
