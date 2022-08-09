package com.thebookofcode.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture tubeUp;
    Texture tubeDown;
    Texture gameOver;
    int flapState = 0;
    Texture[] bird;
    int birdY = 0;
    int birdX = 0;
    float velocity = 0;
    float gap = 600f;
    float gravity = 2f;
    int gameState = 0;
    float maxTubeOffset;
    Random randomGenerator;
    float tubeVelocity = 6;
    int numberOfTubes = 5;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];
    float distanceBetweenTubes;
    Circle birdCircle;
    //ShapeRenderer shapeRenderer;
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;
    int score = 0;
    int scoringTube = 0;
    BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        tubeUp = new Texture("toptube3.png");
        tubeDown = new Texture("bottomtube3.png");
        gameOver = new Texture("gameover.png");
        bird = new Texture[4];
        bird[0] = new Texture("bird.png");
        bird[1] = new Texture("bird2.png");

        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;
        //shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
        startGame();
    }

    public void startGame() {
        birdX = Gdx.graphics.getWidth() / 2 - bird[flapState].getWidth() / 2;
        birdY = Gdx.graphics.getHeight() / 2;
        for (int i = 0; i < numberOfTubes; i++) {
            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - tubeUp.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {

            if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
                score++;
                if (scoringTube < numberOfTubes - 1) {
                    scoringTube++;
                } else {
                    scoringTube = 0;
                }
            }

            if (Gdx.input.justTouched()) {
                velocity = -30;

            }

            for (int i = 0; i < numberOfTubes; i++) {

                if (tubeX[i] < -tubeDown.getWidth()) {
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                } else {
                    tubeX[i] -= tubeVelocity;

                }
                batch.draw(tubeUp, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], tubeUp.getWidth(), tubeUp.getHeight());
                batch.draw(tubeDown, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tubeDown.getHeight() + tubeOffset[i], tubeUp.getWidth(), tubeUp.getHeight());

                //batch.draw(tubeUp, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], tubeUp.getWidth(), tubeUp.getHeight());
                //batch.draw(tubeDown, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tubeDown.getHeight() + tubeOffset[i], tubeUp.getWidth(), tubeUp.getHeight());


                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], tubeUp.getWidth(), tubeUp.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tubeDown.getHeight() + tubeOffset[i], tubeDown.getWidth(), tubeDown.getHeight());

            }

            velocity += gravity;
            birdY -= velocity;

            if (birdY <= 0) {
                birdY = 0;
                gameState = 2;
            }

            int check = Gdx.graphics.getHeight();
            int checkHeight = bird[flapState].getHeight();
            if (birdY >= Gdx.graphics.getHeight() - bird[flapState].getHeight()) {
                birdY = check - checkHeight;
                gameState = 2;
            }


        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 2) {
            batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
            if (Gdx.input.justTouched()) {
                gameState = 1;
                startGame();
                score = 0;
                scoringTube = 0;
                velocity = 0;
            }
        }
        if (flapState == 1) {
            flapState = 0;
        } else {
            flapState = 1;
        }

        batch.draw(bird[flapState], birdX, birdY);
        font.draw(batch, String.valueOf(score), 100, 200);
        batch.end();
        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + bird[flapState].getHeight() / 2, bird[flapState].getWidth() / 2);

        /*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

         */
        for (int i = 0; i < numberOfTubes; i++) {
            /*
            shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], tubeUp.getWidth(), tubeUp.getHeight());
            shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tubeDown.getHeight() + tubeOffset[i], tubeDown.getWidth(), tubeDown.getHeight());

             */

            if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
                gameState = 2;
            }

        }
        //shapeRenderer.end();
    }

    @Override
    public void dispose() {
        batch.dispose();

    }
}
