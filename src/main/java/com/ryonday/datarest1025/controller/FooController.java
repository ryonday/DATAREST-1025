package com.ryonday.datarest1025.controller;

import com.ryonday.datarest1025.domain.Foo;
import com.ryonday.datarest1025.repository.FooRepository;
import org.slf4j.Logger;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.slf4j.LoggerFactory.getLogger;


@BasePathAwareController
public class FooController {

    private static final Logger logger = getLogger(FooController.class);

    private final FooRepository fooRepository;

    public FooController(FooRepository fooRepository) {
        this.fooRepository = fooRepository;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @ResponseBody
    public List<Foo> randomFoos(@RequestParam(name = "howMany", defaultValue = "5") int howMany,
                                @RequestParam(name = "hashKey", defaultValue = "foo") String hashKey) {

        List<Foo> newFoos = IntStream.range(1, howMany)
                .mapToObj(Integer::toString)
                .map(r -> new Foo()
                    .setHash(hashKey)
                    .setRange(r)
                    .setData(UUID.randomUUID().toString())
                    .setMoreData(UUID.randomUUID().toString())
                    .setAlsoData(UUID.randomUUID().toString()))
                .collect(Collectors.toList());

        fooRepository.save(newFoos);

        logger.info("Saved some Foos: {}", newFoos);

        return newFoos;
    }
}
