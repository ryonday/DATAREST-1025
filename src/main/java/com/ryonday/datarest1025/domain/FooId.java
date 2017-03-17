package com.ryonday.datarest1025.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

public class FooId implements Serializable {

    private String hash;

    private String range;

    @DynamoDBHashKey
    public String getHash() {
        return hash;
    }

    public FooId setHash(String hash) {
        this.hash = hash;
        return this;
    }

    @DynamoDBRangeKey
    public String getRange() {
        return range;
    }

    public FooId setRange(String range) {
        this.range = range;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FooId fooId = (FooId) o;
        return Objects.equals(hash, fooId.hash) &&
            Objects.equals(range, fooId.range);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, range);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("hash", hash)
            .add("range", range)
            .toString();
    }
}
