import com.codefinity.BlockingQueueImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BlockingQueueImplTest {

    private BlockingQueueImpl queue;

    @BeforeEach
    public void setUp() {
        queue = new BlockingQueueImpl(2);
    }

    @Test
    public void testPutAndTake() throws InterruptedException {
        // Test put and take with basic operations
        queue.put("Item 1");
        queue.put("Item 2");

        // Retrieve items from the queue
        assertEquals("Item 1", queue.take());
        assertEquals("Item 2", queue.take());
    }

    @Test
    public void testQueueWaitOnFull() throws InterruptedException {
        queue.put("Item 1");
        queue.put("Item 2");

        Thread producer = new Thread(() -> {
            try {
                queue.put("Item 3");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();

        // Let the producer thread wait for a bit
        Thread.sleep(100);

        // Queue should be full; producer thread should be waiting
        assertEquals(2, queue.size());

        // Take an item to free up space in the queue
        Object item = queue.take();
        assertNotNull(item);

        // Now the producer thread should have added "Item 3"
        producer.join();
        assertEquals(2, queue.size());
        assertEquals("Item 2", queue.take());
        assertEquals("Item 3", queue.take());
    }

    @Test
    public void testQueueWaitOnEmpty() throws InterruptedException {
        Thread consumer = new Thread(() -> {
            try {
                queue.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumer.start();

        // Let the consumer thread wait for a bit
        Thread.sleep(100);

        // Queue should be empty; consumer thread should be waiting
        assertEquals(0, queue.size());

        // Add an item to the queue
        queue.put("Item 1");

        assertEquals(1, queue.size());

        // Now the consumer thread should have taken "Item 1"
        consumer.join();
        assertEquals(0, queue.size());
    }
}
