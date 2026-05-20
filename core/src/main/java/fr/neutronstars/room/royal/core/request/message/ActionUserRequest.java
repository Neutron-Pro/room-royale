package fr.neutronstars.room.royal.core.request.message;

public record ActionUserRequest(Object id, String action, Object parameter) implements Request {
    public ActionUserRequest(Object id, String action) {
        this(id, action, null);
    }
}