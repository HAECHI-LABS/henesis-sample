package io.haechi.henesis.assignment.domain.transaction;

import java.util.Map;

public class ActionSupplier<T> {
    private final Map<Situation, T> s;

    public ActionSupplier(Map<Situation, T> s) {
        this.s = s;
    }

    public T supply(Situation situation){
        T action = s.get(situation);
        if (action == null) {
            throw new IllegalArgumentException(String.format("'%s' is not supported blockchain service type", action));
        }
        return action;
    }
}
