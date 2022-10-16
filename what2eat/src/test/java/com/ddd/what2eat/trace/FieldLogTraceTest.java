package com.ddd.what2eat.trace;

import org.junit.jupiter.api.Test;

class FieldLogTraceTest {

	ThreadFieldLogTrace trace = new ThreadFieldLogTrace();

	@Test
	void begin_end_level2() throws Exception {
	    TraceStatus status1 = trace.begin("hello1", 1L);
		TraceStatus status2 = trace.begin("hello2", 2L);
		trace.end(status2);
		trace.end(status1);
	}

	@Test
	void begin_exception_level2() throws Exception {
		TraceStatus status1 = trace.begin("hello1", 1L);
		TraceStatus status2 = trace.begin("hello2", 2L);
		trace.exception(status2, new IllegalStateException());
		trace.exception(status1, new IllegalStateException());
	}


}