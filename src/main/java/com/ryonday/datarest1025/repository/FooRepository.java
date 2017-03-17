package com.ryonday.datarest1025.repository;

import com.ryonday.datarest1025.domain.Foo;
import com.ryonday.datarest1025.domain.FooId;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface FooRepository extends CrudRepository<Foo, FooId> {
}
