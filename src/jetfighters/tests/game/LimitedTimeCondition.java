package jetfighters.tests.game;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class LimitedTimeCondition {
    private final CountDownLatch conditionMetLatch;
    private final Integer unitsCount;
    private final TimeUnit unit;

    public LimitedTimeCondition(final Integer unitsCount, final TimeUnit unit) {
        conditionMetLatch = new CountDownLatch(1);
        this.unitsCount = unitsCount;
        this.unit = unit;
    }

    public boolean waitForConditionToBeMet() {
        try {
            return conditionMetLatch.await(unitsCount, unit);
        } catch (final InterruptedException e) {
            System.out.println("Someone has disturbed the condition awaiter.");
            return false;
        }

    }

    public void conditionWasMet() {
        conditionMetLatch.countDown();
    }
}