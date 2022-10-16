package com.ddd.what2eat.trace;

public interface LogTrace {

	TraceStatus begin(String message, Long userId);

	void end(TraceStatus status);

	void exception(TraceStatus status, Exception e);
}
