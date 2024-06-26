package com.hedera.hashgraph.identity.hcs.did.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedera.hashgraph.identity.DidError;
import com.hedera.hashgraph.identity.hcs.did.event.HcsDidEventTargetName;
import com.hedera.hashgraph.identity.utils.Hashing;
import com.hedera.hashgraph.sdk.PrivateKey;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
public class HcsDidRevokeServiceEventTest {

    PrivateKey privateKey = PrivateKey.fromString(
            "302e020100300506032b6570042204209044d8f201e4b0aa7ba8ed577b0334b8cb6e38aad6c596171b5b1246737f5079"
    );
    String identifier = "did:hedera:testnet:" + Hashing.Multibase.encode(privateKey.getPublicKey().toBytes()) + "_0.0.29613327";

    HcsDidRevokeServiceEvent event = new HcsDidRevokeServiceEvent(identifier + "#service-1");


    public HcsDidRevokeServiceEventTest() throws DidError {
    }

    @Test
    void itTargetsService() {
        assertEquals(event.getTargetName(), HcsDidEventTargetName.SERVICE);
    }

    @Test
    void itThrowsErrorIfIdIsNULL() {
        try {
            new HcsDidRevokeServiceEvent(null);
        } catch (DidError error) {
            assertEquals("Validation failed. Services args are missing", error.getMessage());
        }
    }

    @Test
    void itThrowsErrorIfIdIsNotValid() {
        try {
            new HcsDidRevokeServiceEvent(identifier);
        } catch (DidError error) {
            assertEquals("Event ID is invalid. Expected format: {did}#service-{integer}", error.getMessage());
        }
    }

    @Test
    void getId() {
        assertEquals(event.getId(), "did:hedera:testnet:z6MkogVzoGJMVVLhaz82cA5jZQKAAqUghhCrpzkSDFDwxfJa_0.0.29613327#service-1");
    }

    @Test
    void getBase64() {
        assertEquals("eyJTZXJ2aWNlIjp7ImlkIjoiZGlkOmhlZGVyYTp0ZXN0bmV0Ono2TWtvZ1Z6b0dKTVZWTGhhejgyY0E1alpRS0FBcVVnaGhDcnB6a1NERkR3eGZKYV8wLjAuMjk2MTMzMjcjc2VydmljZS0xIn19", event.getBase64());
    }


    @Test
    void toJsonTree() throws JsonProcessingException {

        String jsonString = "{\"Service\":{\"id\":\"did:hedera:testnet:z6MkogVzoGJMVVLhaz82cA5jZQKAAqUghhCrpzkSDFDwxfJa_0.0.29613327#service-1\"}}";
        JsonNode serviceEventJsonNode = new ObjectMapper().readTree(jsonString);
        assertEquals(event.toJsonTree(), serviceEventJsonNode);

    }


    @Test
    void toJSON() {
        assertEquals(
                "{\"Service\":{\"id\":\"did:hedera:testnet:z6MkogVzoGJMVVLhaz82cA5jZQKAAqUghhCrpzkSDFDwxfJa_0.0.29613327#service-1\"}}"
                , event.toJSON());
    }

    @Test
    void fromJsonTree() throws JsonProcessingException, DidError {

        String jsonString = "{\"id\":\"did:hedera:testnet:z6MkogVzoGJMVVLhaz82cA5jZQKAAqUghhCrpzkSDFDwxfJa_0.0.29613327#service-1\"}";
        JsonNode serviceEventJsonNode = new ObjectMapper().readTree(jsonString);

        HcsDidRevokeServiceEvent eventFromJson = HcsDidRevokeServiceEvent.fromJsonTree(serviceEventJsonNode);


        String expectedJsonString = "{\"Service\":{\"id\":\"did:hedera:testnet:z6MkogVzoGJMVVLhaz82cA5jZQKAAqUghhCrpzkSDFDwxfJa_0.0.29613327#service-1\"}}";
        JsonNode expected = new ObjectMapper().readTree(expectedJsonString);

        assertEquals(expected, eventFromJson.toJsonTree());
        assertEquals(expectedJsonString, eventFromJson.toJSON());

    }


}
