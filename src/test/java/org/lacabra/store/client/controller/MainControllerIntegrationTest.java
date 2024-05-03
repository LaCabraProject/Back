package org.lacabra.store.client.controller;

import categories.IntegrationTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.lacabra.store.client.dto.ItemDTO;
import org.lacabra.store.client.dto.UserDTO;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@Category(IntegrationTest.class)
public class MainControllerIntegrationTest {
    static Process APIProcess;
    MainController controller;

    @BeforeClass
    public static void launchAPI() throws IOException, InterruptedException {
        final var b = new ProcessBuilder().command(System.getProperty("os.name").startsWith("Win") ? "mvn.cmd" : "mvn"
                , "jetty:run", "-f", "pom.xml").inheritIO();
        b.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        b.redirectError(ProcessBuilder.Redirect.DISCARD);

        b.environment().put("JAVA_HOME", System.getProperties().getProperty("java.home"));

        APIProcess = b.start();

        final var controller = new MainController();
        for (int i = 0, N = 30; i < N; i++) {
            if (controller.aliveSync()) {
                controller.GET.Item.allSync();
                if (controller.GET.User.idSync("mikel") != null)
                    return;
            }

            TimeUnit.SECONDS.sleep(1);
        }

        closeAPI();
        System.exit(1);
    }

    @Before
    public void setUp() throws InterruptedException {
        controller = new MainController();
        controller.GET.Item.allSync();
        controller.GET.User.idSync("mikel");

        TimeUnit.SECONDS.sleep(2);
    }

    @AfterClass
    public static void closeAPI() {
        APIProcess.destroy();
    }

    @Test
    public void testAuthentication() {
        controller.authSync("mikel", "1234");
        assertEquals(controller.getUser().toString(), "mikel");

        controller.unauth();
        assertNull(controller.getUser());
    }

    @Test
    public void testItems() throws InterruptedException {
        ItemDTO item = null;
        for (int i = 0, N = 5; i < N; i++) {
            item = controller.GET.Item.idSync(0);
            if (item != null)
                break;

            TimeUnit.SECONDS.sleep(1);
        }

        assertNotNull(item);

        assertEquals(item.name(), "Camiseta de grupo genérico");

        assertEquals(item.id(), 0);
        assertEquals(item.stock(), BigInteger.valueOf(1000L));

        final var items = controller.GET.Item.allSync();
        assertNotNull(items);

        for (var i = 0; i < Math.min(items.size(), 20); i++) {
            assertEquals(items.get(i).id(), i);
        }
    }

    @Test
    public void testUsers() throws InterruptedException {
        UserDTO user = null;
        for (int i = 0, N = 5; i < N; i++) {
            user = controller.GET.User.idSync("mikel");
            if (user != null)
                break;

            TimeUnit.SECONDS.sleep(1);
        }

        assertNotNull(user);
        assertEquals(user.id(), "mikel");
    }
}
