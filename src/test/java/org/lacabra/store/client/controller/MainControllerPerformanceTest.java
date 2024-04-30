package org.lacabra.store.client.controller;

import categories.PerformanceTest;
import com.github.noconnor.junitperf.JUnitPerfRule;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.lacabra.store.internals.type.id.ObjectId;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@Category(PerformanceTest.class)
public class MainControllerPerformanceTest {
    MainController controller;

    @Rule
    public JUnitPerfRule perfTestRule = new JUnitPerfRule(new HtmlReportGenerator("target/junitperf/report.html"));
    @BeforeClass
    public static void launchAPI() throws IOException {
        Runtime.getRuntime().exec(new String [] { "mvn", "jetty:run", "-f pom.xml" });
    }

    @Before
    public void setUp() {
        controller = new MainController();
    }
    @Test
    public void testAuthentication() {
        controller.auth("mikel", "1234");
        assertEquals(controller.getUser().toString(), "mikel");
        controller.unauth();
        assertNull(controller.getUser());
    }
    @Test
    public void testItems() {
        var items = controller.GET.Item.id(ObjectId.from(0)).join();
        assertNotNull(items);
        assertEquals(items.name(), "Camiseta de grupo genérico");
        assertEquals(items.id(), 0);
        assertEquals(items.stock().toString(), "1000");
        // item post
    }
    @Test
    public void testUsers() {
        controller.auth("mikel", "1234");
        var user = controller.GET.User;
        assertNotNull(user);
        assertEquals(user.toString(), "mikel");

        // user post
    }
}
