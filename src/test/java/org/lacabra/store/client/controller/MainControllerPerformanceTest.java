package org.lacabra.store.client.controller;

import categories.PerformanceTest;
import com.github.noconnor.junitperf.JUnitPerfRule;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.internals.type.id.ObjectId;
import org.lacabra.store.internals.type.id.UserId;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@Category(PerformanceTest.class)
public class MainControllerPerformanceTest {
    static Process APIProcess;
    MainController controller;

    @Rule
    public JUnitPerfRule perfTestRule = new JUnitPerfRule(new HtmlReportGenerator("target/junitperf/report.html"));

    @BeforeClass
    public static void startAPI() throws IOException {
        final var b = new ProcessBuilder().command(System.getProperty("os.name").startsWith("Win") ? "mvn.cmd" : "mvn"
                , "jetty:run", "-f", "pom.xml").inheritIO();
        b.environment().put("JAVA_HOME", System.getProperties().getProperty("java.home"));
        b.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        b.redirectError(ProcessBuilder.Redirect.DISCARD);

        APIProcess = b.start();
    }

    @AfterClass
    public static void closeAPI() {
        APIProcess.destroy();
    }

    @Before
    public void setUp() throws InterruptedException {
        controller = new MainController();
        for (int i = 0, N = 30; i < N; i++) {
            if (controller.aliveSync()) return;

            TimeUnit.SECONDS.sleep(1);
        }

        closeAPI();
        System.exit(1);
    }

    @JUnitPerfTest(threads = 10, durationMs = 2000)
    @Test
    public void testAuthentication() {
        controller.authSync("mikel", "1234");
        assertEquals(controller.getUser(), "mikel");

        controller.unauth();
        assertNull(controller.getUser());
    }

    @JUnitPerfTest(threads = 10, durationMs = 2000)
    @Test
    public void testItems() {
        final var item = controller.GET.Item.id(ObjectId.from(0)).join();
        assertNotNull(item);

        assertEquals(item.name(), "Camiseta de grupo genérico");

        assertEquals(item.id(), 0);
        assertEquals(item.stock().toString(), "1000");

        List<ItemDTO> items;
        try {
            items = controller.GET.Item.all().join();
        } catch (Exception e) {
            items = null;
        }

        assertNotNull(items);

        for (var i = 0; i < Math.min(items.size(), 20); i++) {
            assertEquals(items.get(i).id(), i);
        }
    }

    @JUnitPerfTest(threads = 10, durationMs = 2000)
    @Test
    public void testUsers() {
        final var user = controller.GET.User.id(UserId.from("mikel")).join();

        assertNotNull(user);
        assertEquals(user.id(), "mikel");
    }
}
