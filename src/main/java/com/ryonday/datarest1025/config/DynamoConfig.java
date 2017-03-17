package com.ryonday.datarest1025.config;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.google.common.collect.ImmutableList;
import com.ryonday.datarest1025.domain.Foo;
import org.joda.time.field.FieldUtils;
import org.slf4j.Logger;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBMappingContext;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.socialsignin.spring.data.dynamodb.repository.support.DynamoDBRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.context.MappingContext;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.ryonday.datarest1025.repository")
public class DynamoConfig {
    private static final Logger logger = getLogger(DynamoConfig.class);

    @Bean("amazonDynamoDB")
    public AmazonDynamoDB dynamo(@Value("${DYNAMO_URL:http://192.168.99.100:8000}") String endpoint) throws InterruptedException {
        logger.info("DynamoDB URL: {}", endpoint);

        BasicAWSCredentials credentials = new BasicAWSCredentials("irrelevant", "nugatory");

        AmazonDynamoDB dynamoClient = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1"))
            .build();

        DynamoDB dynamo = new DynamoDB(dynamoClient);

        List<AttributeDefinition> attribs = ImmutableList.of(
            new AttributeDefinition("hash", "S"),
            new AttributeDefinition("range", "S"));

        List<KeySchemaElement> keys = ImmutableList.of(
            new KeySchemaElement("hash", KeyType.HASH),
            new KeySchemaElement("range", KeyType.RANGE));

        CreateTableRequest createReq = new CreateTableRequest()
            .withTableName(Foo.tableName)
            .withAttributeDefinitions(attribs)
            .withKeySchema(keys)
            .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L));

        logger.info("Deleting Table '{}'...", Foo.tableName);

        try {
            DeleteTableResult deleteResult = dynamoClient.deleteTable(Foo.tableName);
            logger.info("Table Deletion Result: {}", deleteResult);
        } catch (ResourceNotFoundException ex) {
            logger.warn("Table '{}' did not already exist.", Foo.tableName);
        }

        logger.info("Creating Dynamo Table '{}': {}", Foo.tableName, createReq);

        Table table = dynamo.createTable(createReq);

        logger.info("Waiting for table activation...");
        table.waitForActive();

        logger.info("Dynamo Table: {}", table);

        return dynamoClient;
    }

    @Bean
    public DynamoDBMappingContext getMappingContext() {
        DynamoDBMappingContext context = new DynamoDBMappingContext();
        context.getPersistentEntity(Foo.class);
        logger.info("Set up DynamoDBMappingContext.");
        return context;
    }
}
