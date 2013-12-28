package ro.agitman.opinion.db;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

public class MongoProvider {

	private MongoClient mongoClient = null;
	private Morphia morphia = null;
	private Datastore ds = null;

	public MongoProvider() throws UnknownHostException {
	}

	@PostConstruct
	public void init() {
		if (mongoClient == null) {
			try {
				mongoClient = new MongoClient("localhost");
			} catch (UnknownHostException ex) {
				ex.printStackTrace();
			}
		}
		if (morphia == null && mongoClient != null) {
			morphia = new Morphia();
			// edit: corrected wrong package name
			morphia.mapPackage("ro.agitman.opinion.db.model");
		}
		if (ds == null && morphia != null) {
			ds = morphia.createDatastore(mongoClient, "opinion");
		}
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public Morphia getMorphia() {
		return morphia;
	}

	public Datastore getDs() {
		return ds;
	}
}