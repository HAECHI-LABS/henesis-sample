package io.haechi.henesis.assignment.ethKlayDomain.transaction;

import java.util.Map;

public class ActionSupplier<T> {
    private final Map<Situation, T> s;

    public ActionSupplier(Map<Situation, T> s) {
        this.s = s;
    }

    public T supply(Situation situation){
        T action = s.get(situation);
        if (action == null) {
            throw new IllegalArgumentException(String.format("'%s' is not matched this Situation", situation));
        }
        return action;
    }
}
