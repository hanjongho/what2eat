package com.ddd.what2eat.trace;

import java.util.UUID;

public class TraceId {

	private String id;
	private int level;

	public TraceId(Long userId) {
		this.id = createId(userId);
		this.level = 0;
	}

	private TraceId(String id, int level) {
		this.id = id;
		this.level = level;
	}

	private String createId(Long userId) {
		// return UUID.randomUUID().toString().substring(0, 8);
		return userId.toString();
	}

	public TraceId createNextId() {
		return new TraceId(id, level + 1);
	}

	public TraceId createPreviousId() {
		return new TraceId(id, level - 1);
	}

	public boolean isFirstLevel() {
		return level == 0;
	}

	public String getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}
}
