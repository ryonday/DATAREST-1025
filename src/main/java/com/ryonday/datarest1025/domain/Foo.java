package com.ryonday.datarest1025.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

@DynamoDBTable(tableName = Foo.tableName)
public class Foo {
    public static final String tableName = "DATAREST-1025";

    @Id
    private FooId id;

    public String data;

    public String moreData;

    public String alsoData;

    public Foo() {
        this.id = new FooId();
    }

    @DynamoDBHashKey
    public String getHash() {
        return id.getHash();
    }

    public Foo setHash(String hash) {
        id.setHash(hash);
        return this;
    }

    @DynamoDBRangeKey
    public String getRange() {
        return id.getRange();
    }

    public Foo setRange(String range) {
        id.setRange(range);
        return this;
    }

    /**
     * This has to be named like this; if a getter with the same name as the @id field is
     * annotated with @DynamoDBIgnore, Spring-Data-REST will not recognize it as the id.
     */
    @DynamoDBIgnore
    @JsonIgnore
    public FooId getCompositeId() {
        return id;
    }

    @JsonIgnore
    public Foo setCompositeId(FooId id) {
        this.id = id;
        return this;
    }

    public String getData() {
        return data;
    }

    public Foo setData(String data) {
        this.data = data;
        return this;
    }

    public String getMoreData() {
        return moreData;
    }

    public Foo setMoreData(String moreData) {
        this.moreData = moreData;
        return this;
    }

    public String getAlsoData() {
        return alsoData;
    }

    public Foo setAlsoData(String alsoData) {
        this.alsoData = alsoData;
        return this;
    }
}
