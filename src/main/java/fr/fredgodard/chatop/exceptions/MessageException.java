package fr.fredgodard.chatop.exceptions;

public class MessageException extends Throwable {

    public MessageException(String s, Exception e) {
        super(s,e);
    }

    public MessageException(String s) {
        super (s, null);
    }
}
