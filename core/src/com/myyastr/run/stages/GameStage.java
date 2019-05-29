package com.myyastr.run.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.myyastr.run.actors.Background;
import com.myyastr.run.actors.Coin;
import com.myyastr.run.actors.Enemy;
import com.myyastr.run.actors.Ground;
import com.myyastr.run.actors.Runner;
import com.myyastr.run.actors.Score;
import com.myyastr.run.actors.StartButton;
import com.myyastr.run.box2d.UserData;
import com.myyastr.run.enums.Difficulty;
import com.myyastr.run.enums.GameState;
import com.myyastr.run.utils.BodyUtils;
import com.myyastr.run.utils.Constants;
import com.myyastr.run.utils.GameManager;
import com.myyastr.run.utils.WorldUtils;

public class GameStage extends Stage implements ContactListener {

    private static final int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    private static final int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;

    private World world;
    private Background background;
    private Ground ground;
    private Runner runner;


    private final float TIME_STEP = 1 / 600f;
    private float accumulator = 0f;
    private float totalTimePassed;

    private OrthographicCamera camera;
    private Score score;
    private static Preferences prefs;
    private StartButton startButton;


    private Music bgMusic;
    private Sound pickUpSound;

    private Rectangle screenRightSide;
    private Rectangle screenLeftSide;
    private Vector3 touchPoint;


    public GameStage() {
        super(new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT,
                new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)));
        //BODIES
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.5f);
        bgMusic.play();
        pickUpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.wav"));

        setupWorld();
        setupCamera();
        setupTouchControlAreas();

    }


    private void setupWorld() {
        world = WorldUtils.createWorld();
        world.setContactListener(this);

        setupBackground();

        setupRunner();
        setupGround();
        setupScore();
        createEnemy();
        createCoin();


        prefs = Gdx.app.getPreferences("Game");
        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
            prefs.flush();
        }


    }


    private void setupBackground() {
        background = new Background();
        addActor(background);
    }

    //Add enemy and set his speed.
    private void createEnemy() {
        Enemy enemy = new Enemy(WorldUtils.createEnemy(world));
        enemy.getUserData().setLinearVelocity(GameManager.getInstance().getDifficulty()
                .getEnemyLinearVelocity());
        addActor(enemy);

    }

    //add Coins
    private void createCoin() {
        Coin onecoin = new Coin(WorldUtils.createCoin(world));
        onecoin.getUserData().setLinearVelocity(GameManager.getInstance().getDifficulty()
                .getEnemyLinearVelocity());
        addActor(onecoin);
    }


    private void setupGround() {
        ground = new Ground(WorldUtils.createGround(world));
        addActor(ground);
    }


    private void setupRunner() {
        runner = new Runner(WorldUtils.createRunner(world));
        addActor(runner);
    }


    private void setupCamera() {

        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();

    }


    private void setupScore() {
        Rectangle boundaries = new Rectangle(125, getCamera().
                viewportHeight - 60, 100, 100);

        score = new Score(boundaries);

        addActor(score);
    }


    private void setupStartButton() {
        Rectangle boundaries = new Rectangle(camera.viewportWidth /
                2 - (151 / 2), 70, 151, 151);
        startButton = new StartButton(boundaries, this);
        addActor(startButton);
    }


    private void setupTouchControlAreas() {
        touchPoint = new Vector3();
        screenRightSide = new Rectangle(getCamera().viewportWidth /
                2, 0, getCamera().viewportWidth / 2,
                getCamera().viewportHeight);
        screenLeftSide = new Rectangle(0, 0, getCamera().viewportWidth /
                2, getCamera().viewportHeight);

        Gdx.input.setInputProcessor(this);
    }




    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        //translate to world coordinates
        translateScreenToWorldCoordinates(x, y);

        if (rightSideTouched(touchPoint.x, touchPoint.y)) {
            runner.jump();
        } else if (leftSideTouched(touchPoint.x, touchPoint.y)) {
            runner.dodge();
        }

        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (runner.isDodging()) {
            runner.stopDodge();
        }
        return super.touchUp(x, y, pointer, button);
    }


    private boolean rightSideTouched(float x, float y) {
        return screenRightSide.contains(x, y);
    }


    private boolean leftSideTouched(float x, float y) {
        return screenLeftSide.contains(x, y);
    }



    private void translateScreenToWorldCoordinates(int x, int y) {
        getCamera().unproject(touchPoint.set(x, y, 0));
    }

    @Override
    public void beginContact(Contact contact) {

        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsEnemy(b)) ||
                (BodyUtils.bodyIsEnemy(a) && BodyUtils.bodyIsRunner(b))) {
            runner.hit();
            onGameOver();
        } else if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsGround(b)) ||
                (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsRunner(b))) {
            runner.landed();
        } else if ((BodyUtils.bodyIsCoin(a) && BodyUtils.bodyIsRunner(b)) ||
                (BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsCoin(b))) {

            if (BodyUtils.bodyIsCoin(a)) killBody(a);
            else killBody(b);


            // Kaskartą kai suvalgom coins'ą gaunam 5 taškus
            score.addScore(5);
            pickUpSound.play();
        }

    }






    @Override
    public void act(float delta) {

        super.act(delta);


        if (GameManager.getInstance().getGameState() == GameState.RUNNING) {
            totalTimePassed += delta;
            updateDifficulty();
        }

        //Updating all bodies
        Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);
        for (Body body : bodies) {
            update(body);
        }

        accumulator += delta;

        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }

    }


    private void update(Body body) {

        //check if bodies needs to be destroyed
        if (!BodyUtils.bodyInBounds(body)) {
            if (BodyUtils.bodyIsEnemy(body) && (GameManager.getInstance().getGameState() == GameState.RUNNING)) {
                createEnemy();
            }
            if (BodyUtils.bodyIsCoin(body) && (GameManager.getInstance().getGameState() == GameState.RUNNING)) {
                createCoin();
            }
            if (!BodyUtils.bodyIsRunner(body)) world.destroyBody(body);
        }


        UserData bodyData = (UserData) body.getUserData();
        if (bodyData != null && bodyData.isFlaggedForDeletion() == true) {
            if (BodyUtils.bodyIsCoin(body) && GameManager.getInstance().getGameState()
                    == GameState.RUNNING)
                createCoin();
            world.destroyBody(body);

        }

    }

   //updatina difficulty priklausant nuo kiek laiko žaidėjas žaidia.
    private void updateDifficulty() {

        if (GameManager.getInstance().isMaxDifficulty()) {
            return;
        }

        Difficulty currentDifficulty = GameManager.getInstance().getDifficulty();


        if (totalTimePassed > GameManager.getInstance().getDifficulty().getLevel() * 8) {

            int nextDifficulty = currentDifficulty.getLevel() + 1;
            String difficultyName = "DIFFICULTY_" + nextDifficulty;
            GameManager.getInstance().setDifficulty(Difficulty.valueOf(difficultyName));

            runner.onDifficultyChange(GameManager.getInstance().getDifficulty());
        }

    }



    private void killBody(Body body) {
        UserData bodyData = (UserData) body.getUserData();
        bodyData.setFlaggedForDelete(true);
    }



    public void onGameOver() {
        GameManager.getInstance().setGameState(GameState.OVER);
        GameManager.getInstance().resetDifficulty();
        this.totalTimePassed = 0.0f;
        score.setHighScore();
        setupStartButton();
    }

    //restart game
    public void reset() {

        clear();
        setupWorld();
        setupCamera();
        GameManager.getInstance().setGameState(GameState.RUNNING);

    }

    @Override
    public void endContact(Contact contact) {


    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {


    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
