package ch.epicerielacanopee.statistics;

import ch.epicerielacanopee.statistics.config.AsyncSyncConfiguration;
import ch.epicerielacanopee.statistics.config.EmbeddedSQL;
import ch.epicerielacanopee.statistics.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { StatisticsApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
