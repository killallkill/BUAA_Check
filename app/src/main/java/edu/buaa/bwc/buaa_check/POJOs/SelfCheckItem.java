package edu.buaa.bwc.buaa_check.POJOs;

/**
 * Created by Gary on 2017/1/13.
 */

public class SelfCheckItem {
    public String content;
    public String id;
    public String rectifyState;
    public String name;
    public String userId;
    public String path;
    public String checkAdd;
    public String checkTime;
    public String passRate;
    public String rectifyStateVal;
    public String qualified;

    @Override
    public String toString() {
        return id + " " + name + " " + checkTime;
    }
}
