package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGdxGame;

public class MainMenuScreen implements Screen {

    private final MyGdxGame app;
    private Stage stage;
    private Skin skin;
    private Music bgm;

    private TextButton btnPlay, btnExit, btnNet;

    private ShapeRenderer shapeRenderer;

    public MainMenuScreen(final MyGdxGame app) {
        this.app = app;
        this.stage = new Stage(new StretchViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, app.camera));
        this.shapeRenderer = new ShapeRenderer();

    }


    @Override
    public void show() {
        System.out.println("MainMenu-Screen.show");
        Gdx.input.setInputProcessor(stage);//해당 스테이지에서 클릭등을 허용시킴. (초 중요 필수 라인)
        stage.clear();//화면이 전환될 때(화면 시작시) 전에있던 화면에 있던 개체들을 지움. 최고 중요! @@@@@@@@@@@@@@@@@@

        bgm = app.assets.get("sound/bgm.ogg");//assetmanager객체에서 로딩창 로드 할 때 담아놓은 음악 파일 가져옴.
        bgm.setLooping(true);//배경음악 반복 설정
        bgm.setVolume(1f);

        this.skin = new Skin();
        this.skin.addRegions(app.assets.get("ui/uiskin.atlas", TextureAtlas.class));//skin객체에 디자인 넣기. 매개변수 atlas 파일
        this.skin.add("default-font", app.font24);//skin 객체에 폰트 설정. uiskin.json에서 설정되어있는 default-font로 font24객체를 넣어줌.
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));//마지막에 설정된 값과 json파일 skin객체로 로드.

        initButton();
    }

    private void initButton() {//버튼 초기화 메서드.
        btnPlay = new TextButton("Play", skin, "default");//버튼 텍스트, 해당 버튼 스타일 객체, uiskin.json파일의 설정이름 불러오기
        btnPlay.setPosition(110,260);
        btnPlay.setSize(280, 60);
        btnPlay.addAction(Actions.sequence(Actions.alpha(0), Actions.parallel(
                Actions.fadeIn(.5f), Actions.moveBy(0, -20, .5f, Interpolation.pow5Out))
        ));
        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bgm.stop();//화면 전환 동시에 배경음악 종료.
                app.setScreen(app.playScreen);
            }
        });

        btnExit = new TextButton("Exit", skin, "default");
        btnExit.setPosition(110,190);
        btnExit.setSize(280, 60);
        btnExit.addAction(Actions.sequence(Actions.alpha(0), Actions.parallel(
                Actions.fadeIn(.5f), Actions.moveBy(0, -20, .5f, Interpolation.pow5Out))
        ));
        btnExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        btnNet = new TextButton("Go to Net", skin, "default");
        btnNet.setPosition(110,120);
        btnNet.setSize(280,60);
        btnNet.addAction(Actions.sequence(Actions.alpha(0), Actions.parallel(
                Actions.fadeIn(.5f), Actions.moveBy(0, -20, .5f, Interpolation.pow5Out))
        ));
        btnNet.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.httpScreen);
            }
        });

        stage.addActor(btnPlay);//스테이지에 해당 객체 추가.
        stage.addActor(btnExit);
        stage.addActor(btnNet);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

        bgm.play();
        //app.batch.begin();
        //app.font24.draw(app.batch, "Screen Main Menu", 20, 20);
        //app.batch.end();
    }

    private void update(float delta) {
        stage.act(delta);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }
}
