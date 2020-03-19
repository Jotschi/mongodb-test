package com.gentics.mesh.mongodb;

import static io.vertx.core.logging.LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME;

import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Log4j2LogDelegateFactory;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;

public class MongoTest {

	@ClassRule
	public static GenericContainer mongodb = new GenericContainer("mongo:4.2").withExposedPorts(27017);

	static {
		initLogger();
	}

	private static void initLogger() {
		//Configurator.setRootLevel(Level.DEBUG);
		System.setProperty(LOGGER_DELEGATE_FACTORY_CLASS_NAME, Log4j2LogDelegateFactory.class.getName());
		LoggerFactory.initialise();
	}

	@Test
	public void testMongo() {
		Vertx vertx = Vertx.vertx();
		JsonObject config = new JsonObject();
		config.put("port", mongodb.getFirstMappedPort());
		config.put("host", "localhost");

		MongoClient mongoClient = MongoClient.createShared(vertx, config);

		// Save document
		JsonObject document = new JsonObject();
		document.put("title", "The Hobbit");
		mongoClient.save("books", document, res -> {
			if (res.succeeded()) {
				String id = res.result();
				System.out.println("Saved book with id " + id);
			} else {
				res.cause().printStackTrace();
			}
		});

		// Find documents
		JsonObject query = new JsonObject();
		mongoClient.find("books", query, res -> {
			if (res.succeeded()) {
				for (JsonObject json : res.result()) {
					System.out.println(json.encodePrettily());
				}
			} else {
				res.cause().printStackTrace();
			}
		});
	}

}
