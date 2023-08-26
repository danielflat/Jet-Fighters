package jetfighters.game.handlers;

import jetfighters.game.entities.Entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Uses an layer-system to calculate all collisions for entities registered in an EntityHandler.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class CollisionHandler {

    public static final int PLAYER_LAYER = 1;
    public static final int ENEMY_LAYER = 2;
    public static final int PROJECTILE1_LAYER = 4;
    public static final int PROJECTILE2_LAYER = 8;
    public static final int PROJECTILE_ENEMY_LAYER = 16;
    public static final int POWERUP_LAYER = 32;

    private final EntityHandler entityHandler;

    private final HashMap<Entity, Set<Entity>> currentCollisions;

    /**
     * Creates a new CollisionHandler that will detect all collisions for the supplied {@code entityHandler}.
     *
     * @param entityHandler the EntityHandler to detect collisions for
     */
    public CollisionHandler(EntityHandler entityHandler) {
        this.entityHandler = entityHandler;
        currentCollisions = new HashMap<>();
    }

    /**
     * Calculates all collisions happening between the entities of the corresponding {@link EntityHandler}. <br>
     * Automatically calls the {@link Entity#handleCollision(Entity)} methods of the colliding entities according
     * to their {@code collisionLayer} and {@code collisionMask}.
     */
    public void update() {
        for (Entity entity : entityHandler.getEntities()) {
            int entityMask = entity.getCollisionMask();
            if (!currentCollisions.containsKey(entity)) {
                currentCollisions.put(entity, new HashSet<>());
            }
            for (Entity other : entityHandler.getEntities()) {
                if (entity == other)
                    continue;
                int otherLayer = other.getCollisionLayer();
                if ((entityMask & otherLayer) == 0)
                    continue;
                Set<Entity> collidingWith = currentCollisions.get(entity);
                if (entity.isCollidingWith(other)) {
                    if (!collidingWith.contains(other)) {
                        entity.handleCollision(other);
                        collidingWith.add(other);
                    }
                } else {
                    collidingWith.remove(other);
                }
            }
        }
    }
}
