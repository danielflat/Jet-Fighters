package jetfighters.game.handlers;

import jetfighters.game.entities.Entity;

import java.awt.*;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.List;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Handles updating and drawing entities. <br>
 * Entities can be added, removed and the list of all registered entities can be retrieved.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class EntityHandler {

    private final List<Entity> entities;
    private final List<Entity> entitiesToAdd;
    private final List<Entity> entitiesToRemove;

    public EntityHandler() {
        entities = new ArrayList<>();
        entitiesToAdd = new ArrayList<>();
        entitiesToRemove = new ArrayList<>();
    }

    public void updateEntities(double delta) {
        entities.removeAll(entitiesToRemove);
        entitiesToRemove.clear();
        entities.addAll(entitiesToAdd);
        entitiesToAdd.clear();
        for (Entity entity : entities) {
            if (entity.shouldDestroy()) {
                removeEntity(entity);
                continue;
            }
            entity.update(delta);
        }
    }

    public void drawEntities(Graphics2D g2d) {
        for (Entity entity : entities) {
            g2d.transform(entity.getTransform());
            entity.draw(g2d);
            try {
                g2d.transform(entity.getTransform().createInverse());
            } catch (NoninvertibleTransformException e) {
                System.err.println("Couldn't inverse the transform of " + entity.getClass().getName());
                e.printStackTrace();
            }
        }
    }

    public void addEntity(Entity entity) {
        entitiesToAdd.add(entity);
    }

    public void removeEntity(Entity entity) {
        entitiesToRemove.add(entity);
    }

    public List<Entity> getEntities() {
        return new ArrayList<>(entities);
    }
}
