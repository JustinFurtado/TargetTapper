package com.example.targettapper;

/**
 * Created by jfurtado on 3/9/2017.
 */

public class TimeManager {
    private static long lastTime;

    public static void initialize(){
        lastTime = System.currentTimeMillis();
    }
    public static float getDeltaTimeOnlyCallOncePerFrame(){
        long currentTime = System.currentTimeMillis();
        float deltaTime = ((float)(currentTime - lastTime))/1000.0f;
        lastTime = currentTime;
        return deltaTime;
    }
}
