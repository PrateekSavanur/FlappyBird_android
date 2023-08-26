package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Random;

public class FlappyBird extends Game implements Screen {
	SpriteBatch batch;
	Texture background;
	// ShapeRenderer shapeRenderer;
	Texture gameover;
	Texture Restart;
	Texture homePage;

	Texture[] birds;
	int flapState = 0;
	float birdY = 0;
	float velocity = 0;
	Circle birdCircle;
	int score = 0;
	int scoringTube = 0;
	BitmapFont font;


	int gameState = 0;
	float gravity = 1;
	Texture topTube;
	Texture bottomTube;
	float gap = 400;
	float maxTubeOffset;
	Random randomGenerator;

	float tubeVelocity = 4;

	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTheTube;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;






	@Override
	public void create() {

		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameover = new Texture("gameover.png");
		homePage = new Texture("Homepage.png");


		Restart = new Texture("start_button.png");
		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();

		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");

		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();
		distanceBetweenTheTube = Gdx.graphics.getWidth() * 3 / 4;
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];

		startGame();


	}

	public void startGame()
	{
		birdY = Gdx.graphics.getHeight() / 2 - birds[flapState].getHeight() / 2;
		for (int i = 0; i < numberOfTubes; i++) {
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTheTube;

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();

		}
	}

	@Override
	public void render() {

		batch.begin();
		batch.draw(homePage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {

			batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
				Gdx.app.log("Score", String.valueOf(score));
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

				if (tubeX[i] < -topTube.getWidth()) {

					tubeX[i] += numberOfTubes * distanceBetweenTheTube;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				} else {

					tubeX[i] = tubeX[i] - tubeVelocity;

				}
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			}


			if (birdY > 0) {
				velocity = velocity + gravity;
				birdY -= velocity;
			}else{

				gameState = 2; //2 is the game over state
			}
		} else if(gameState==0){

			if (Gdx.input.justTouched())
			{
				gameState = 1;

			}

		}else if(gameState==2)
		{
			batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.draw(gameover ,Gdx.graphics.getWidth()/2 - gameover.getWidth()/2,Gdx.graphics.getHeight()/2 -gameover.getHeight() + 500);
			batch.draw(Restart,Gdx.graphics.getWidth()/2 - Restart.getWidth()/2,Gdx.graphics.getHeight()/4 - Restart.getHeight()/2 + 200);
			font.draw(batch ,"Your Score is " + String.valueOf(score) , 50 , Gdx.graphics.getHeight() - birds[flapState].getHeight() / 2 - 300);
			font.draw(batch ,"Click to Restart",50 ,Gdx.graphics.getHeight()/5);


			if (Gdx.input.justTouched())
			{
				gameState = 1;
				startGame();
				score =0;
				scoringTube=0;
				velocity = 0;
			}

		}



		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}


		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
		font.draw(batch , String.valueOf(score) , 100 , 200);


		batch.end();

		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(birdCircle.x ,birdCircle.y , birdCircle.radius);

		for (int i = 0; i < numberOfTubes; i++) {

			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(),bottomTube.getHeight());

			if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {

				gameState = 2;
			}
		}


		//shapeRenderer.end();


	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

	}

	@Override
	public void hide() {

	}
}