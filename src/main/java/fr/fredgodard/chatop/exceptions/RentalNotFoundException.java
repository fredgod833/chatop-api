package fr.fredgodard.chatop.exceptions;

public class RentalNotFoundException extends Throwable {

    public RentalNotFoundException(String s, Exception e) {
        super(s,e);
    }

}
