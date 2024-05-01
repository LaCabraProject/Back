package org.lacabra.store.client.controller;

import categories.IntegrationTest;
import com.github.noconnor.junitperf.JUnitPerfTest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.lacabra.store.internals.type.id.ObjectId;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@Category(IntegrationTest.class)
public class MainControllerIntegrationTest {
    MainController controller;

    @BeforeClass
    public static void launchAPI() throws IOException {
        Runtime.getRuntime().exec(new String[]{"mvn", "jetty:run", "-f", "pom.xml"});
    }

    @Before
    public void setUp() throws InterruptedException {
        controller = new MainController();
        for (int i = 0, N = 10; i < N; i++) {
            if (controller.alive().join())
                break;

            TimeUnit.SECONDS.sleep(1);
        }

        System.exit(1);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 1000)
    public void testAuthentication() {
        controller.auth("mikel", "1234");
        assertEquals(controller.getUser().toString(), "mikel");
        controller.unauth();
        assertNull(controller.getUser());
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000)
    public void testItems() {
        var item = controller.GET.Item.id(ObjectId.from(0)).join();
        assertNotNull(item);
        assertEquals(item.name(), "Camiseta de grupo genérico");
        assertEquals(item.id(), 0);
        assertEquals(item.stock().toString(), "1000");

        var items = controller.GET.Item.all().join();
        assertNotNull(items);
        for (var i = 0; i < items.size(); i++) {
            assertEquals(items.get(i).id(), i);
        }
        // item post
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 1000)
    public void testUsers() {
        controller.auth("mikel", "1234");
        var user = controller.GET.User;
        assertNotNull(user);
        assertEquals(user.toString(), "mikel");

        // user post
    }
}
