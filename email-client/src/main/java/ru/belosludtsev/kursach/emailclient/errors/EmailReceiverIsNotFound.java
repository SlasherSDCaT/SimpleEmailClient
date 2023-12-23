package ru.belosludtsev.kursach.emailclient.errors;


public class EmailReceiverIsNotFound extends Exception{
    public EmailReceiverIsNotFound(String message){
        super(message);
    }
}
