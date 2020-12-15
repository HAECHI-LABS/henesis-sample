package io.haechi.henesis.assignment.domain;

public interface Alerter {
    void alert(String title, String message, String color);
}