package fr.fredgodard.chatop.exceptions;

public class ClientNotFoundException extends Throwable {

    public ClientNotFoundException(String s, Exception e) {
        super(s,e);
    }

    public ClientNotFoundException(String s) {
        super (s, null);
    }
}
