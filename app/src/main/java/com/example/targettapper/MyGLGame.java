package com.example.targettapper;

/**
 * Created by jfurtado on 3/9/2017.
 */

public interface MyGLGame {
    public void initialize();
    public void update(float dt);
    public void draw(MyGLRenderer renderer);
    public void shutdown();
    public void onTouch(float ndcX, float ndcY, float widthP);
}
