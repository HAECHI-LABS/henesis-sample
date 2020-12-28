package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.exception.InternalServerException;

import java.util.Map;

public class HenesisClientSupplier {
    private final Map<Blockchain, HenesisClient> map;

    public HenesisClientSupplier(Map<Blockchain, HenesisClient> map) {
        this.map = map;
    }

    public HenesisClient supply(Blockchain blockchain) {
        HenesisClient client = this.map.get(blockchain);
        if (client == null) {
            throw new InternalServerException(String.format("'%s' is not supported henesis client type", blockchain));
        }
        return client;
    }
}
