package com.myyastr.run.box2d;

import com.myyastr.run.enums.UserDataType;

public class GroundUserData extends UserData {


    public GroundUserData(float width, float height){
        super(width, height);
        userDataType = UserDataType.GROUND;
    }
}
