package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.screen.HttpScreen;
import com.mygdx.game.screen.LoadingScreen;
import com.mygdx.game.screen.MainMenuScreen;
import com.mygdx.game.screen.PlayScreen;
import com.mygdx.game.screen.SplashScreen;

public class MyGdxGame extends Game {

	public static final String Title = "Slide";
	public static final float VERSION = 0.1f;
	public static final int V_WIDTH = 480;
	public static final int V_HEIGHT = 420;

	public OrthographicCamera camera;
	public SpriteBatch batch;

	//public BitmapFont font;
	public BitmapFont font24;//사이즈 24로 설정할 폰트 객체. 이렇게 폰트 객체 여러개 만들고 사용하고 싶을 때 사용하는게 좋음.

	//AssetManager 사용하여 로딩창 구현
	public AssetManager assets;

	public LoadingScreen  loadingScreen;
	public SplashScreen splashScreen;
	public MainMenuScreen mainMenuScreen;
	public PlayScreen playScreen;
	public HttpScreen httpScreen;

	@Override
	public void create(){
		assets=new AssetManager();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);//카메라
		batch = new SpriteBatch();

		initFonts();//글꼴 생성 메서드.

		//font = new BitmapFont();//비트맵 함수로 글꼴 객체 생성하여 글씨 쓰기.
		//font.setColor(Color.BLACK);

		loadingScreen = new LoadingScreen(this);
		splashScreen = new SplashScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		playScreen = new PlayScreen(this);
		httpScreen = new HttpScreen(this);

		this.setScreen(loadingScreen);//게임 첫 화면 LoadingScreen으로 설정
	}

	private void initFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/RIDIBatang.otf"));//글꼴 생성 클래스 객체. 해당 글꼴파일 불러와 객체 저장
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();//파라미터 생성. 해당 파라미터로 글꼴에 사용자 지정 설정 가능.

		parameter.size = 24;//사이즈 24
		parameter.color = Color.BLACK;

		font24 = generator.generateFont(parameter);//사이즈 24설정 값 폰트에 대입 완료
	}

	@Override
	public void render(){
		super.render();
	}

	@Override
	public void dispose(){
		batch.dispose();
		//font.dispose();
		font24.dispose();
		assets.dispose();
		loadingScreen.dispose();
		splashScreen.dispose();
		mainMenuScreen.dispose();//불러들인 화면의 객체들의 사용한 값들을 처리.
		playScreen.dispose();
		httpScreen.dispose();
	}

}