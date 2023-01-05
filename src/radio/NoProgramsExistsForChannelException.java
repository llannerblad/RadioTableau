package radio;

public class NoProgramsExistsForChannelException extends Exception {
    public NoProgramsExistsForChannelException(String errorMessage) {
        super(errorMessage);
    }
}
