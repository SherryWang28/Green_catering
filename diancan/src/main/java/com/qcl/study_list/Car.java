package com.qcl.study_list;

public class Car {
    private String pinpai;
    private int jiage;

    public Car(String pinpai, int jiage) {
        this.pinpai = pinpai;
        this.jiage = jiage;
    }

    public String getPinpai() {
        return pinpai;
    }

    public void setPinpai(String pinpai) {
        this.pinpai = pinpai;
    }

    public int getJiage() {
        return jiage;
    }

    public void setJiage(int jiage) {
        this.jiage = jiage;
    }
}
