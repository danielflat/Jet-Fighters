package jetfighters.game;

import jetfighters.audio.AudioPlayer;
import jetfighters.audio.Sound;
import jetfighters.game.camera.ShakeCamera;
import jetfighters.game.entities.Entity;
import jetfighters.game.entities.Player;
import jetfighters.game.handlers.CollisionHandler;
import jetfighters.game.handlers.EntityHandler;
import jetfighters.game.handlers.TweenHandler;
import jetfighters.game.math.Vector2;
import jetfighters.game.spawner.PowerUpSpawner;
import jetfighters.game.states.GameState;
import jetfighters.game.states.GameStateManager;
import jetfighters.game.transitions.RectangleFadeTransition;
import jetfighters.game.transitions.VocalCircleTransition;
import jetfighters.game.tween.Tween;
import jetfighters.game.tween.TweenMode;
import jetfighters.windows.GameOverWindow;
import jetfighters.windows.Window;
import jetfighters.windows.exceptions.StateNotFoundException;
import jetfighters.windows.states.MenuState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class Game extends Canvas implements Runnable {

    private final Window window;
    public static final boolean debug = false;
    private EntityHandler entityHandler;
    private CollisionHandler collisionHandler;
    private PowerUpSpawner powerUpSpawner;
    private TweenHandler tweenHandler;
    private boolean running;
    private Thread thread;

    private Player player1;
    private Player player2;

    private final ShakeCamera camera;

    private HUD hud;

    private final int canvasWidth;
    private final int canvasHeight;

    private double currentGameTime; // The time that has passed in between every update call when the game was resumed
    private double gameSpeed;

    private final GameStateManager gms;
    private final Image backgroundImage;

    private final AudioPlayer audioPlayer;

    /**
     * Creates a new Game instance (which is a canvas) to run the game in. <br>
     * Needs to be added to a {@link Window} to work.
     * @param canvasWidth the width of the game-canvas
     * @param canvasHeight the height of the game-canvas
     * @param window the window this was added to
     */
    public Game(int canvasWidth, int canvasHeight, Window window) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        gms = new GameStateManager();
        gms.setState(GameState.Running);

        gameSpeed = 1;

        setupEntities();

        camera = new ShakeCamera();

        this.window = window;
        this.backgroundImage = window.getBackgroundImage().getBackgroundImageScaled(1.1, 1.1).getImage();
        this.audioPlayer = window.getAudioPlayer();
    }

    private void setupEntities() {
        gameSpeed = 1;

        entityHandler = new EntityHandler();
        //Enemy strafer = new StrafeEnemy(this, new Vector2(-100, -100), new Vector2(800, 700), TweenMode.Quadratic);
        powerUpSpawner = new PowerUpSpawner(entityHandler);
        if (player1 == null && player2 == null) {
            player1 = new Player(1, new Vector2(200, 350), this);
            player2 = new Player(2, new Vector2(800, 350), this);
            player1.setOpponent(player2);
            player2.setOpponent(player1);
        }
        entityHandler.addEntity(player1);
        entityHandler.addEntity(player2);
        //entityHandler.addEntity(strafer);

        collisionHandler = new CollisionHandler(entityHandler);
        tweenHandler = new TweenHandler();
        hud = new HUD(entityHandler);
    }

    /**
     * Needs to be called after the game has been added to the window (and therefore validated by swing).
     */
    public void initBuffer() {
        setSize(canvasWidth, canvasHeight);
        setVisible(true);
        setFocusable(false);
        this.createBufferStrategy(2); // Use buffering to draw every frame at once instead of drawing everything one by one
    }

    private void render() {
        if (gms.inAny(GameState.Running)) {
            BufferStrategy bs = this.getBufferStrategy();
            try {
                Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();

                g2d.setColor(Color.white);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                AffineTransform cameraTransform = camera.getTransform(getCanvasWidth(), getCanvasHeight());
                AffineTransform cameraInverseTransform = camera.getInverseTransform(getCanvasWidth(), getCanvasHeight());
                g2d.transform(cameraTransform);

                g2d.drawImage(backgroundImage, -20, -20, null);

                entityHandler.drawEntities(g2d);

                g2d.transform(cameraInverseTransform);

                hud.draw(g2d, cameraTransform, cameraInverseTransform, currentGameTime);

                g2d.dispose();
                bs.show();
            } catch (IllegalStateException e) {
                System.out.println("Dropped frame because of invalid peer");
            }
        }

    }

    private void update(double delta) {
        double realDelta = delta;
        delta *= gameSpeed;
        if (gms.inAny(GameState.Running)) {
            currentGameTime += delta;

            entityHandler.updateEntities(delta);
            collisionHandler.update();
            powerUpSpawner.update(delta);
            camera.update(delta);

            hud.update(realDelta);
            tweenHandler.updateTweens(realDelta);
        }
    }

    /**
     * Will end the game by fading into the "Game Over"-screen. <br>
     * The fade will be centered on the player who lost.
     * @param playerLost the player who lost the game
     */
    public void gameOver(Player playerLost) {
        addTween(new Tween.Double(2, getGameSpeed(), 0, TweenMode.Linear, this::setGameSpeed));
        hud.transition(new VocalCircleTransition(
                playerLost.getPosition(), 0, 1600, false, 2, 2,
                TweenMode.Cubic, () -> {
            try {
                gms.setState(GameState.Menu);
                window.updateMenuState(MenuState.GameOver);
                GameOverWindow gameOverWindow = window.getGameOverWindow();
                if (playerLost.getPlayerID() == 1)
                    gameOverWindow.setDescription(2);
                else
                    gameOverWindow.setDescription(1);
            } catch (StateNotFoundException e) {
                throw new RuntimeException(e);
            }
        }, Color.BLACK
        ));
    }

    /**
     * Fades in the game screen using a {@link RectangleFadeTransition}. <br>
     * This expects the screen to have been black when starting.
     */
    @SuppressWarnings("unused")
    public void fadeIn() {
        setupEntities();
        gameSpeed = 0;
        hud.transition(new RectangleFadeTransition(1, TweenMode.Linear, () -> gameSpeed = 1, Color.BLACK, 1, 0));
    }

    /**
     * Plays a sound at full volume using the internal audio player.
     * @param sound the sound to be played
     */
    public void playSound(Sound sound) {
        playSound(sound, 1f);
    }

    /**
     * Plays a sound at the specified volume using the internal audio player.
     * @param sound the sound to be played
     * @param volume the volume to play the sound at
     */
    public void playSound(Sound sound, float volume) {
        audioPlayer.playSound(sound.getSoundPath(), volume);
    }

    /**
     * Adds a new entity to the game, making it appear in the game world (by calling the draw() and update() methods)
     * @param entity the entity to be added
     */
    public void addEntity(Entity entity) {
        entityHandler.addEntity(entity);
    }

    /**
     * Adds a new tween to be handled by the game. <br>
     * The tween will be updated with the actual time that has passed and not the one adjusted by the games speed.
     * @param tween the tween to be added
     */
    public void addTween(Tween tween) {
        tweenHandler.addTween(tween);
    }

    /**
     * Adds trauma to the screen shake, making it shake more violently the higher the trauma gets.
     * @param amount the amount of trauma to be added
     */
    public void addScreenShake(double amount) {
        camera.addTrauma(amount);
    }

    /**
     * @return the width of this games canvas
     */
    public int getCanvasWidth() {
        return canvasWidth;
    }

    /**
     * @return the height of this games canvas
     */
    public int getCanvasHeight() {
        return canvasHeight;
    }


    /**
     * Will start the game loop internally, blocking the current Thread and should therefore only be automatically called by a new Thread. <br>
     * You should not call this manually and instead call {@link #startGameLoop()}.
     */
    @Override
    public void run() {
        //Initializes the gameloop variables
        long lastTime = System.nanoTime();
        double amountOfTicks = 50.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        double lastUpdate = lastTime;
        long timer = System.currentTimeMillis();
        int frames = 0;
        //The gameloop wich calls the tick() method every gameTick (60 times/sec) and the render() method each frame (dependent on System)
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                long time = System.nanoTime();
                double deltaTime = ((time - lastUpdate) / 1000000);
                lastUpdate = time;
                update(deltaTime / 1000);
                delta--;
            }
            if (running)
                render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }
    }

    /**
     * Will start the game by running its game loop in a new Thread.
     */
    public synchronized void startGameLoop() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    /**
     * Will stop the game safely, by stopping the game loop and calling {@link Thread#join()} on the games thread.
     */
    public synchronized void stopGameLoop() {
        try {
            running = false;
            thread.join(10000);
        } catch (Exception e) {
            System.err.println("Failed to stop the game thread: " + thread);
            e.printStackTrace();
        }
    }

    /**
     * Sets the internal game state of this game using its {@link GameStateManager}.
     * @param newState the state to change to.
     */
    public void setGameState(GameState newState) {
        gms.setState(newState);
    }

    /**
     * @return the first player
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * @return the second player (can be null in single-mode)
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * @return the current game speed
     */
    public double getGameSpeed() {
        return gameSpeed;
    }

    /**
     * Sets this games speed. <br>
     * The game speed will be used to calculate a new delta-time variable that will be used to update the games objects. <br>
     * Tweens will not be affected by this.
     * @param gameSpeed the new game speed
     */
    public void setGameSpeed(double gameSpeed) {
        this.gameSpeed = gameSpeed;
    }
}
