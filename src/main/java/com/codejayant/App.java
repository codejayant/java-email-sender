package com.codejayant;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        EmailSender.sendEmailThreaded("testing html", "email body", "codejayant@gmail.com");
    }
}