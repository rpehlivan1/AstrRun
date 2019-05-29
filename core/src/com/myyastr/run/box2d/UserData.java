package com.myyastr.run.box2d;

import com.myyastr.run.enums.UserDataType;

public class UserData {
    protected UserDataType userDataType;
    protected float width;
    protected float height;
    protected boolean isFlaggedForDelete;

    public UserData(){

        //Work in progress

    }


    public UserData(float width, float height){
        this.width = width;
        this.height = height;
        this.isFlaggedForDelete = false;
    }


    public UserDataType getUserDataType(){
        return userDataType;
    }


    public float getWidth() {
        return width;
    }


    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }


    public void setHeight(float height) {
        this.height = height;
    }


    public boolean isFlaggedForDeletion() {
        return isFlaggedForDelete;
    }


    public void setFlaggedForDelete(boolean isFlaggedForDelete) {
        this.isFlaggedForDelete = isFlaggedForDelete;
    }
}
