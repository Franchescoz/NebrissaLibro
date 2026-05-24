package com.ejemplo.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBUtil {

    private static final String URI = "mongodb://localhost:27017";
    private static final String DATABASE = "nebrissabiblioteca";
    private static MongoClient mongoClient;
    public static MongoDatabase getDatabase() {

        if (mongoClient == null) {
            mongoClient = MongoClients.create(URI);
        }

        return mongoClient.getDatabase(DATABASE);
    }
}