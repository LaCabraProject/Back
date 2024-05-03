package org.lacabra.store.client.controller;

import categories.PerformanceTest;
import com.github.noconnor.junitperf.JUnitPerfRule;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.internals.type.id.ObjectId;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Category(PerformanceTest.class)
public class MainControllerPerformanceTest {
    static Process APIProcess;
    @Rule
    public JUnitPerfRule perfTestRule = new JUnitPerfRule(new HtmlReportGenerator("target/junitperf/report.html"));
    MainController controller;

    @BeforeClass
    public static void startAPI() throws IOException, InterruptedException {
        final var b = new ProcessBuilder().command(System.getProperty("os.name").startsWith("Win") ? "mvn.cmd" : "mvn"
                , "jetty:run", "-f", "pom.xml").inheritIO();
        b.environment().put("JAVA_HOME", System.getProperties().getProperty("java.home"));
        b.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        b.redirectError(ProcessBuilder.Redirect.DISCARD);

        APIProcess = b.start();

        final var controller = new MainController();
        for (int i = 0, N = 30; i < N; i++) {
            if (controller.aliveSync()) {
                controller.GET.Item.allSync();
                if (controller.GET.User.idSync("mikel") != null)
                    return;
            }

            TimeUnit.SECONDS.sleep(5);
        }

        closeAPI();
        System.exit(1);
    }

    @AfterClass
    public static void closeAPI() {
        APIProcess.destroy();
    }

    @Before
    public void setUp() throws InterruptedException {
        controller = new MainController();
    }

    @JUnitPerfTest(threads = 10, durationMs = MainController.TIMEOUT * 3)
    @Test
    public void testAuthentication() {
        assertTrue(controller.authSync("mikel", "1234"));
        assertEquals(controller.getUser(), "mikel");

        controller.unauth();
        assertNull(controller.getUser());
    }

    @JUnitPerfTest(threads = 2, durationMs = MainController.TIMEOUT * 3)
    @Test
    public void testItems() {
        final var item = controller.GET.Item.idSync(ObjectId.from(0));
        assertNotNull(item);

        assertEquals(item.name(), "Camiseta de grupo genérico");

        assertEquals(item.id(), 0);
        assertEquals(item.stock().toString(), "1000");

        List<ItemDTO> items;
        try {
            items = controller.GET.Item.allSync();
        } catch (Exception e) {
            items = null;
        }

        assertNotNull(items);

        for (var i = 0; i < Math.min(items.size(), 20); i++) {
            assertEquals(items.get(i).id(), i);
        }
    }

    @JUnitPerfTest(threads = 2, durationMs = MainController.TIMEOUT * 3)
    @Test
    public void testUsers() {
        final var user = controller.GET.User.idSync("mikel");

        assertNotNull(user);
        assertEquals(user.id(), "mikel");
    }
}
