package ro.agitman.opinion.guice;

import ro.agitman.opinion.db.MongoProvider;
import ro.agitman.opinion.resource.PingResource;
import ro.agitman.opinion.service.PingService;
import ro.agitman.opinion.service.PongService;
import ro.agitman.opinion.service.impl.PingServiceImpl;
import ro.agitman.opinion.service.impl.PongServiceImpl;

import com.google.inject.AbstractModule;

public class MyGuiceConfig extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(PongService.class).to(PongServiceImpl.class);
		bind(PingService.class).to(PingServiceImpl.class);
		bind(PingResource.class);
		
		//database pool
		bind(MongoProvider.class).asEagerSingleton();
	}
}
