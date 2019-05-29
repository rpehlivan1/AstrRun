package com.myyastr.run.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.myyastr.run.box2d.RunnerUserData;
import com.myyastr.run.enums.Difficulty;
import com.myyastr.run.utils.Constants;

public class Runner extends GameActor {

    private boolean isJumping;
    private boolean isDodging;
    private boolean isHit;

    //Animations
    private Animation runningAnimation;
    private TextureRegion jumpingTexture;
    private TextureRegion dodgingTexture;
    private TextureRegion hitTexture;
    private float stateTime;
    private Sound dieSound;
    private Sound jumpSound;


    //Sukuriam character objektą kuris
    public Runner(Body body) {
        super(body);
        //creating animations and textures
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal
                (Constants.CHARACTERS_ATLAS_PATH));
        TextureRegion[] runningFrames = new TextureRegion[Constants.
                RUNNER_RUNNING_REGION_NAMES.length];
        for(int i=0; i<Constants.RUNNER_RUNNING_REGION_NAMES.length; i++){
            String path = Constants.RUNNER_RUNNING_REGION_NAMES[i];
            runningFrames[i] = textureAtlas.findRegion(path);
        }
        runningAnimation = new Animation(0.1f, runningFrames);
        stateTime = 0f;
        jumpingTexture = textureAtlas.findRegion(Constants.RUNNER_JUMPING_REGION_NAME);
        dodgingTexture = textureAtlas.findRegion(Constants.RUNNER_DODGING_REGION_NAME);
        hitTexture = textureAtlas.findRegion(Constants.RUNNER_HIT_REGION_NAME);
        dieSound = Gdx.audio.newSound(Gdx.files.internal("sounds/death.mp3"));
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.mp3"));
        jumpSound.setVolume(0, 0.5f);
    }


    @Override
    public void draw(Batch batch, float parentAlpha){

        super.draw(batch, parentAlpha);

        float x = screenRectangle.x - (screenRectangle.width * 0.1f);
        float y = screenRectangle.y;
        float width = screenRectangle.width * 1.2f;

        if(isDodging){

            batch.draw(dodgingTexture, x, y + screenRectangle.height / 4, width,
                    screenRectangle.height * 3 / 4);
        }
        else if(isHit){
            batch.draw(hitTexture, x, y, width *0.5f,
                    screenRectangle.height*0.5f, width,  screenRectangle.height,
                    1f, 1f, (float) Math.toDegrees(body.getAngle()));
        }
        else if(isJumping){
            batch.draw(jumpingTexture, x, y, width, screenRectangle.height);
        }
        else{
            // else he is running !
            stateTime += Gdx.graphics.getDeltaTime();
            batch.draw((TextureRegion) runningAnimation.getKeyFrame(stateTime,
                    true), x, y, width, screenRectangle.height);
        }
    }

    @Override
    public RunnerUserData getUserData() {
        return (RunnerUserData) userData;
    }


    public void jump(){
        if(!(isJumping || isHit)){
            body.applyLinearImpulse(getUserData().getJumpingLinearImpulse()
                    ,body.getWorldCenter(), true);
            jumpSound.play();
            isJumping = true;
        }
    }


    public void landed(){
        isJumping = false;
    }


    public void dodge(){
        if(!(isJumping || isHit)){
            body.setTransform(getUserData().getDodgePosition(),getUserData().getDodgeAngle());
            isDodging = true;
        }
    }


    public void stopDodge(){
        isDodging = false;
        if(!isHit){
            body.setTransform(getUserData().getRunningPosition(), 0f);
        }
    }


    public boolean isDodging(){
        return isDodging;
    }


    public void hit(){
        body.applyAngularImpulse(getUserData().getHitAngularImpulse(), true);
        if(!isHit)dieSound.play();
        isHit = true;

    }


    public boolean isHit(){
        return this.isHit;
    }



    public void reset(){
        isJumping = false;
        isDodging = false;
        isHit = false;

        body.setAngularVelocity(0f);
        body.setLinearVelocity(0f, 0f);
        body.setTransform(getUserData().getRunningPosition(), 0);
    }


    public void onDifficultyChange(Difficulty newDifficulty) {
        setGravityScale(newDifficulty.getRunnerGravityScale());
        getUserData().setJumpingLinearImpulse(newDifficulty.getRunnerJumpingLinearImpulse());
    }


    public void setGravityScale(float gravityScale) {
        body.setGravityScale(gravityScale);
        body.resetMassData();
    }



}
