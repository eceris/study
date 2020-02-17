package com.prodo.event;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EventApplicationTests {

	@Captor
	protected ArgumentCaptor<Object> publishEventCaptor;

	@Mock
	private ApplicationEventPublisher applicationEventPublisher;

	@Test
	void contextLoads() {

	}

	protected void verifyPublishedEvents(Object... events) {
		Mockito.verify(applicationEventPublisher, Mockito.times(events.length)).publishEvent(publishEventCaptor.capture());
		List<Object> capturedEvents = publishEventCaptor.getAllValues();

		for (int i = 0; i < capturedEvents.size(); i++) {
//			assertThat(capturedEvents.get(i), instanceOf(events[i].getClass()));
			System.out.println(events[i].getClass().getName());
			assertEquals(capturedEvents.get(i), events[i]);
		}
	}

}
