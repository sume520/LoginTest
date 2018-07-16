package com.example.sun.httptest;

import java.io.Serializable;

public class UserData implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int ID;
    private int deviceID;
    private String username;
    private String nickname;
    private static UserData userData;

    public static UserData createInstance() {
        if(userData==null)
            userData=new UserData();
        return userData;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public static UserData getUserData() {
        return userData;
    }

    public static void setUserData(UserData userData) {
        UserData.userData = userData;
    }
}
