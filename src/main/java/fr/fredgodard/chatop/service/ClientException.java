package fr.fredgodard.chatop.service;

public class ClientException extends Throwable {

    public ClientException(String s, Exception e) {
        super(s,e);
    }

    public ClientException(String s) {
        super (s, null);
    }
}
