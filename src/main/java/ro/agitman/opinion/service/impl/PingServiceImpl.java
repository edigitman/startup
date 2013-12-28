package ro.agitman.opinion.service.impl;

import ro.agitman.opinion.service.PingService;

public class PingServiceImpl implements PingService {

	public PingServiceImpl() {
	}

	public PingServiceImpl(String s) {
	}

	@Override
	public String ping() {
		return "PONG";
	}

}
