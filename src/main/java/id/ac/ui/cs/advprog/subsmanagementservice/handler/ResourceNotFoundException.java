package id.ac.ui.cs.advprog.subsmanagementservice.handler;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
