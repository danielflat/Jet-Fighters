package jetfighters.game.spawner;

import jetfighters.game.entities.powerups.*;
import jetfighters.game.handlers.EntityHandler;
import jetfighters.game.math.Vector2;

import java.util.Random;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Automatically spawns PowerUps by calling update method.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class PowerUpSpawner {

    private final EntityHandler entityHandler;
    private final int powerUps;
    private final Random random;
    private final double spawnTime;
    private double time;
    private final int offset;

    //TODO: Add doc comments
    public PowerUpSpawner(EntityHandler entityHandler) {
        this.entityHandler = entityHandler;
        this.powerUps = 5;
        this.random = new Random();
        this.spawnTime = 5.0;
        this.time = spawnTime;
        this.offset = 32;
    }

    public void update(double delta) {
        time += delta;
        if (time >= spawnTime) {
            PowerUpEntity powerUp = null;
            time -= spawnTime;
            int number = random.nextInt(powerUps);
            int x = random.nextInt(1068 - 2 * offset) + offset;
            switch (number) {
                case 0 -> powerUp = new HealthKitEntity(new Vector2(x, 0), new Vector2(0, 100.0), 50.0);
                case 1 -> powerUp = new AttackBuffEntity(new Vector2(x, 0), new Vector2(0, 100.0),
                        2, 5);
                case 2 -> powerUp = new AttackDebuffEntity(new Vector2(x, 0), new Vector2(0, 100.0),
                        0.5, 5);
                case 3 -> powerUp = new SpeedBuffEntity(new Vector2(x, 0), new Vector2(0, 100.0),
                        2, 5);
                case 4 -> powerUp = new SpeedDebuffEntity(new Vector2(x, 0), new Vector2(0, 100.0),
                        0.5, 5);
            }
            if (powerUp != null) {
                entityHandler.addEntity(powerUp);
            }
        }
    }
}
