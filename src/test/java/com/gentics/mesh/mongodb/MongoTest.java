package com.gentics.mesh.mongodb;

import static io.vertx.core.logging.LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME;

import java.io.IOException;

import org.junit.ClassRule;
import org.junit.Test;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Log4j2LogDelegateFactory;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;

public class MongoTest {

	@ClassRule
	public static MongoDBContainer mongodb = new MongoDBContainer();

	public static Vertx vertx = Vertx.vertx();

	static {
		initLogger();
	}

	private static void initLogger() {
		// Configurator.setRootLevel(Level.DEBUG);
		System.setProperty(LOGGER_DELEGATE_FACTORY_CLASS_NAME, Log4j2LogDelegateFactory.class.getName());
		LoggerFactory.initialise();
	}

	@Test
	public void testMongo() throws InterruptedException, IOException {
		String uri = "mongodb://127.0.0.1:" + mongodb.getPort();
		System.out.println(uri);
		JsonObject config = new JsonObject();
		config.put("connection_string", uri);
		config.put("db_name", "test");

		MongoClient mongoClient = MongoClient.createShared(vertx, config);

		JsonObject product1 = new JsonObject().put("itemId", "12345").put("name", "Cooler").put("price", "100.0");

		mongoClient.save("products", product1, id -> {
			System.out.println("Inserted id: " + id.result());

			mongoClient.find("products", new JsonObject().put("itemId", "12345"), res -> {
				System.out.println("Name is " + res.result().get(0).getString("name"));

				mongoClient.removeDocument("products", new JsonObject().put("itemId", "12345"), rs -> {
					if (rs.succeeded()) {
						System.out.println("Product removed ");
					}
				});

			});

		});

		System.in.read();
	}

}
