package jetfighters.game.entities;

import jetfighters.audio.Sound;
import jetfighters.game.Game;
import jetfighters.game.entities.projectiles.Projectile;
import jetfighters.game.entities.projectiles.ProjectileOwner;
import jetfighters.game.math.CollisionUtil;
import jetfighters.game.math.Vector2;
import jetfighters.game.powerupseffects.JetStat;
import jetfighters.game.powerupseffects.PowerUpEffect;
import jetfighters.game.powerupseffects.StatEffect;
import jetfighters.game.sprites.AnimatedSprite;

import java.awt.*;
import java.awt.geom.Area;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Represents a Jet that can fly around the screen, can shoot and collide with other jets.
 * It has various stats like speed, projectileDamage and can draw its health bar.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 21.07.22
 */
public abstract class JetEntity extends MovingEntity implements ProjectileOwner {
    protected JetStat speed;
    protected JetStat rotationSpeed;
    protected JetStat projectileSpeed;
    protected JetStat shotDelay;
    protected JetStat shootKnockback;
    protected JetStat spinSpeed;
    protected JetStat maxHealth;
    protected JetStat projectileDamage;

    protected double currentHealth;

    protected boolean shooting;
    protected double shotTimer;
    protected double spinTimer;
    private AnimatedSprite jetSprite;
    private final AnimatedSprite idleSprite;
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    private AnimatedSprite damagedSprite;
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    private double immunityDuration;
    private double immunityTimer;

    private double hitSoundTimer;

    protected final List<PowerUpEffect> powerUps;
    protected final Game game;

    /**
     * {@code jetImagePath} defaults to "jet/jet1/Jet_1_1.png".
     *
     * @see #JetEntity(Game, Vector2, Shape, int, int, double, String)
     */
    public JetEntity(Game game, Vector2 position, Shape shape, int collisionLayer, int collisionMask, double maxVelocity) {
        this(game, position, shape, collisionLayer, collisionMask, maxVelocity, "jet/jet1/Jet_1_1.png");
    }

    /**
     * Creates a new jet entity with a custom jet image
     *
     * @param game           the game object this exists in
     * @param position       the initial position of this jet
     * @param shape          the collision shape of this jet (used to draw this jet if the image from jetImagePath can't be loaded)
     * @param collisionLayer the collision layer this jet exists on
     * @param collisionMask  the collision layer this jet should detect collisions on
     * @param maxVelocity    the maximum velocity this jet should be able to reach
     * @param jetImagePath   relative path to an image to use for this jet (relative to "sprites/")
     */
    public JetEntity(Game game, Vector2 position, Shape shape, int collisionLayer, int collisionMask, double maxVelocity, String jetImagePath) {
        super(position, shape, collisionLayer, collisionMask, maxVelocity);
        this.game = game;
        this.powerUps = new LinkedList<>();

        scale = scale.mult(1.5);

        setupStats();

        velocity = Vector2.ZERO;

        String spritePath = "sprites/" + jetImagePath.substring(0, jetImagePath.length() - 5);
        idleSprite = new AnimatedSprite(spritePath, 1, 0.125);
        damagedSprite = new AnimatedSprite(spritePath, 2, 0.125);
        jetSprite = idleSprite;

        this.immunityDuration = 0.5;
    }

    private void setupStats() {
        speed = new JetStat(150);
        rotationSpeed = new JetStat(2);
        projectileSpeed = new JetStat(300);
        shotDelay = new JetStat(.25);
        shootKnockback = new JetStat(8);
        spinSpeed = new JetStat(6);
        maxHealth = new JetStat(100);
        projectileDamage = new JetStat(5);
        currentHealth = maxHealth.getValue();
    }


    @Override
    public void update(double delta) {
        updatePowerups(delta);
        updateShots(delta);
        updateStatus(delta);

        super.update(delta);

        if (immunityTimer > 0) {
            immunityTimer = Math.max(immunityTimer - delta, 0);
            if (immunityTimer == 0)
                jetSprite = idleSprite;
        }
        jetSprite.update(delta);

        applyScreenWrapping();

        if (hitSoundTimer > 0) {
            hitSoundTimer -= delta;
        }
    }

    private void updatePowerups(double delta) {
        List<PowerUpEffect> removePowerups = new LinkedList<>();
        for (PowerUpEffect powerupEffect : powerUps) {
            powerupEffect.update(delta);
            if (powerupEffect.shouldRemove()) {
                removePowerups.add(powerupEffect);
            }
        }
        removePowerups.forEach(this::removePowerUpEffect);
    }

    private void updateShots(double delta) {
        if (shotTimer < shotDelay.getValue())
            shotTimer += delta;
        if (canShoot() && shooting) {
            while (shotTimer >= shotDelay.getValue()) {
                shoot();
                shotTimer -= shotDelay.getValue();
            }
        }
    }

    private void updateStatus(double delta) {
        if (spinTimer > 0) {
            spinTimer -= delta;
            if (spinTimer <= 0) {
                spinTimer = 0;
            }
        }
    }

    private void applyScreenWrapping() {
        // Wrap the jet at screen edges
        if (!getTransformedShape().intersects(new Rectangle(0, 0, game.getCanvasWidth(), game.getCanvasHeight()))) {
            // TODO: make wrapping smoother by adding margin.
            double newX = ((int) position.x + game.getCanvasWidth()) % game.getCanvasWidth();
            double newY = ((int) position.y + game.getCanvasHeight()) % game.getCanvasHeight();
            position = new Vector2(newX, newY);
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        jetSprite.draw(g2d);
        if (Game.debug) {
            g2d.setColor(Color.BLUE);
            g2d.draw(getShape());
        }
    }

    @Override
    public void handleCollision(Entity otherEntity) {
        // Knockback code
        if (otherEntity instanceof JetEntity otherJet) {
            Vector2 knockbackVelocity;
            double knockbackStrength;
            Vector2 ownVelocity = getLastVelocity();
            Vector2 otherVelocity = otherJet.getLastVelocity();
            if (ownVelocity.length() > otherVelocity.length()) { // Compute if this is the aggressor of the crash
                knockbackVelocity = ownVelocity;
                knockbackStrength = -ownVelocity.length() / 2.0;
                game.addScreenShake(Math.min(.9, knockbackVelocity.length() / 350));
                game.playSound(Sound.Collision);
            } else { // Compute if this is the victim of the crash
                knockbackVelocity = otherVelocity;
                knockbackStrength = otherVelocity.length() / 1.5;

                if (knockbackVelocity.length() > 50 && getDirection().dot(knockbackVelocity.reversed().normalized()) <= .25) {
                    int direction = (int) Math.signum(angularVelocity);
                    startSpinning(direction == 0 ? 1 : direction, 1.5);
                }
            }
            if (knockbackVelocity.length() > 10) { // Apply knockback
                acceleration = Vector2.ZERO;
                velocity = knockbackVelocity.normalized().mult(knockbackStrength);
            } else {
                velocity = Vector2.ZERO;
            }
        }
    }

    /**
     * Draws the health bar of this jet above its sprite.
     *
     * @param g2d the graphics context to draw to
     */
    public void drawHealthBar(Graphics2D g2d) {
        double fillPercent = currentHealth / maxHealth.getValue();
        int barWidth = (int) (60 * scale.x);
        int barHeight = (int) (6 * scale.y);
        int borderSize = 1;
        double verticalOffset = -getHeight() / 2.0 - barHeight / 2.0 - 20;
        Vector2 centerPosition = getCenteredPosition().add(0, verticalOffset);
        int barLeft = (int) (centerPosition.x - barWidth / 2.0);
        int barCenter = (int) (barLeft + barWidth * fillPercent);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(barLeft - borderSize, (int) centerPosition.y - borderSize, barWidth + borderSize * 2, barHeight + borderSize * 2);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(barLeft, (int) centerPosition.y, (int) (barWidth * fillPercent), barHeight);
        g2d.setColor(Color.RED);
        g2d.fillRect(barCenter, (int) centerPosition.y, (int) (barWidth * (1.0 - fillPercent)), barHeight);
    }

    /**
     * Fires a bullet from the front of the jet with this jets {@link #projectileSpeed} applying {@link #shootKnockback} to itself.
     */
    public void shoot() {
        int horizontalOffset = 15;
        Vector2 centerPosition = getCenteredPosition();
        Vector2 shootPositionCenter = centerPosition.add(getDirection().mult(getHeight() / 3));
        Vector2 shootPositionRight = shootPositionCenter.add(getDirection().rotated(Math.PI / 2).mult(horizontalOffset));
        Vector2 shootPositionLeft = shootPositionCenter.add(getDirection().rotated(-Math.PI / 2).mult(horizontalOffset));
        Vector2 bulletVelocity = getDirection().mult(projectileSpeed.getValue()).add(velocity);
        Projectile bulletR = new Projectile(this, shootPositionRight, bulletVelocity);
        Projectile bulletL = new Projectile(this, shootPositionLeft, bulletVelocity);
        game.addEntity(bulletR);
        game.addEntity(bulletL);
        velocity = velocity.sub(getDirection().mult(shootKnockback.getValue()));
    }

    /**
     * Starts the "spinning" status effect that makes this jet unable to move or shoot for the duration of the effect.
     *
     * @param rotationDirection the direction to spin in (1 = clockwise, -1 = counter-clockwise),
     *                          if 0 it will not spin (should be avoided, add a new status effect for this)
     * @param duration          the length of time this effect should last for
     */
    public void startSpinning(int rotationDirection, double duration) {
        angularVelocity = rotationDirection * spinSpeed.getValue();
        spinTimer = duration;
    }

    /**
     * Heals this jet by {@code amount} (add amount to current health) will not heal over {@link #maxHealth}.
     *
     * @param amount the amount to heal
     */
    public void heal(double amount) {
        currentHealth = Math.min(currentHealth + amount, maxHealth.getValue());
        game.playSound(Sound.Heal);
    }

    /**
     * This method does the same as calling {@link #damage(double, boolean)} as {@code damage(amount, false)}.
     */
    public void damage(double amount) {
        damage(amount, false);
    }

    /**
     * Removes {@code amount} health from current health of this jet and makes it immune to more damage for {@link #immunityDuration} (in seconds).
     * If health reaches 0 this jet will die ({@link #die()}).
     *
     * @param ignoreImmunity if true the damage will be dealt regardless of immunity and no immunity will be given.
     * @param amount         the amount of damage this jet should take
     */
    public void damage(double amount, boolean ignoreImmunity) {
        if (!ignoreImmunity && immunityTimer > 0)
            return;
        currentHealth -= amount;
        jetSprite = damagedSprite;
        if (!ignoreImmunity)
            immunityTimer = immunityDuration;
        game.addScreenShake(.19);
        if (currentHealth <= 0) {
            die();
        } else {
            if (hitSoundTimer <= 0) {
                game.playSound(Sound.Hit);
                hitSoundTimer = .2;
            }
        }
    }

    /**
     * Destroys this jet, plays the death sound and creates an explosion.
     */
    public void die() { // Should be overwritten in Player to make one player win the game or add a point.
        game.addEntity(new Explosion(position, 3));
        game.playSound(Sound.Death);
        destroy();
    }

    /**
     * Applies a copy of the effect of a power up to this jet. <br>
     * After the effects' duration is over the effect will be removed automatically.
     *
     * @param powerUpEffect the effect to be applied
     */
    public void addPowerUpEffect(PowerUpEffect powerUpEffect) { //TODO: Sound
        for (PowerUpEffect effect : powerUps) {
            if (effect instanceof StatEffect statEffect && powerUpEffect instanceof StatEffect otherEffect) {
                if (!statEffect.getStatName().equals(otherEffect.getStatName()))
                    continue;
                statEffect.resetTime();
                return;
            }
        }
        powerUps.add(powerUpEffect);
        powerUpEffect.applyEffect(this);
    }

    /**
     * Removed the effect of a power up, should only be called manually if it shouldn't be removed automatically.
     *
     * @param powerUpEffect the effect that should be removed
     */
    public void removePowerUpEffect(PowerUpEffect powerUpEffect) {
        powerUpEffect.removeEffect(this);
        powerUps.remove(powerUpEffect);
    }

    /**
     * Resets this jet to a state that it can respawn in and respawn it.
     */
    @SuppressWarnings("unused")
    public void respawn() {
        powerUps.forEach(powerUp -> powerUp.removeEffect(this));
        powerUps.clear();
    }

    /**
     * @return an outline generated from the currently used {@link #jetSprite} or if that is {@code null} it will return the shape as an area.
     */
    public Area getOutline() {
        return getOutline(200);
    }

    private Area getOutline(int alphaThreshold) {
        if (jetSprite == null)
            return new Area(shape);
        return CollisionUtil.getOutline(alphaThreshold, jetSprite.getImage());
    }

    /**
     * Gets the JetStat that is named {@code stat} in this class.
     *
     * @param statName the name of the stat field
     * @return the JetStat with the field-name {@code stat} or null if it doesn't exist
     */
    public JetStat getStat(String statName) {
        for (Class<?> currentClass = getClass(); currentClass != null; currentClass = currentClass.getSuperclass()) {
            try {
                Field statField = currentClass.getDeclaredField(statName);
                return (JetStat) statField.get(this);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }
        return null;
    }

    /**
     * The width of this jet. Either the width of its sprite or (if sprite is null) of its shape.
     *
     * @return the width of this jet
     */
    public double getWidth() {
        if (jetSprite.hasValidImage()) {
            return jetSprite.getWidth() * scale.x;
        }
        return shape.getBounds2D().getWidth() * scale.x;
    }

    /**
     * The height of this jet. Either the height of its sprite or (if sprite is null) of its shape.
     *
     * @return the height of this jet
     */
    public double getHeight() {
        if (jetSprite.hasValidImage()) {
            return jetSprite.getHeight() * scale.y;
        }
        return shape.getBounds2D().getHeight() * scale.x;
    }

    /**
     * The position in the center of this jet.
     *
     * @return the centered position
     */
    public Vector2 getCenteredPosition() {
        return position.add(getWidth() / 2.0, getHeight() / 2.0);
    }

    /**
     * Set this to true to start shooting and false to stop.
     *
     * @param shooting true to shoot, false to stop shooting
     */
    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    @Override
    public double getProjectileBaseDamage() {
        return projectileDamage.getValue();
    }

    /**
     * @return the amount of health this jet has at the moment
     */
    public double getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Sets the current amount of health of this jet.
     *
     * @param currentHealth the new health of this jet
     */
    public void setCurrentHealth(double currentHealth) {
        this.currentHealth = currentHealth;
    }

    /**
     * @return true if this jet is currently spinning
     */
    public boolean isSpinning() {
        return spinTimer > 0;
    }

    /**
     * @return true if this jet can currently be controlled by a player or AI
     */
    public boolean canControl() {
        return !isSpinning();
    }

    /**
     * @return true if this jet can currently shoot
     */
    public boolean canShoot() {
        return !isSpinning();
    }

    /**
     * @return true if this should lose speed when no input is given (or stunned)
     */
    public boolean shouldSlowDown() {
        return !isSpinning();
    }
}
