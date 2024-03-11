package fr.fredgodard.chatop.exceptions;

public class AuthException extends Throwable {

    public AuthException(String s, Exception e) {
        super(s,e);
    }

    public AuthException(String s) {
        super (s, null);
    }
}
