package ro.agitman.opinion.service.impl;

import com.google.inject.Inject;

import ro.agitman.opinion.db.MongoProvider;
import ro.agitman.opinion.service.PingService;
import ro.agitman.opinion.service.PongService;

public class PongServiceImpl implements PongService {

	private PingService pingService;
	private MongoProvider mongoProvider;
	
	
	@Inject
	public PongServiceImpl(PingService pg, MongoProvider mongoProvider) {
		this.pingService = pg;
		this.mongoProvider = mongoProvider;
	}
	
	@Override
	public String pong() {
		mongoProvider.getDs().ensureCaps();
		return "PONG - " + pingService.ping();
	}	
}