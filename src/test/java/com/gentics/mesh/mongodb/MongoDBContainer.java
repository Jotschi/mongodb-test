package com.gentics.mesh.mongodb;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class MongoDBContainer extends GenericContainer<MongoDBContainer> {

	public static final int MONGODB_PORT = 27017;

	public static final String DEFAULT_IMAGE_AND_TAG = "mongo:4.2";

	public MongoDBContainer() {
		this(DEFAULT_IMAGE_AND_TAG);
	}

	public MongoDBContainer(String image) {
		super(image);
		addExposedPort(MONGODB_PORT);
		waitingFor(Wait.forLogMessage(".*waiting for connections on port.*", 1));
	}

	public Integer getPort() {
		return getMappedPort(MONGODB_PORT);
	}
}
