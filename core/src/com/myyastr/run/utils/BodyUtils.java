package com.myyastr.run.utils;

import com.badlogic.gdx.physics.box2d.Body;
import com.myyastr.run.box2d.UserData;
import com.myyastr.run.enums.UserDataType;

public class BodyUtils {
    // return true if body is on ground
    public static boolean bodyIsRunner(Body body){
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.RUNNER;
    }

   // return true if body is on ground
    public static boolean bodyIsGround(Body body){
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.GROUND;
    }

    //return true if body is enemy
    public static boolean bodyIsEnemy(Body body){
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.ENEMY;
    }

   //return true if body is coin
    public static boolean bodyIsCoin(Body body){
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.COIN;
    }

    //return true if body is in screen borders
    public static boolean bodyInBounds(Body body){
        UserData userData = (UserData) body.getUserData();

        switch(userData.getUserDataType()){
            case RUNNER:
            case ENEMY:
            case COIN:
                return body.getPosition().x + userData.getWidth() / 2 > 0;
            default:
                return true;
        }
    }
}
