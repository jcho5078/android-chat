package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.MyGdxGame;

public class LoadingScreen implements Screen {

    private final MyGdxGame app;

    private ShapeRenderer shapeRenderer;
    private float progress = 0f;

    public LoadingScreen(MyGdxGame app){
        this.app = app;
        this.shapeRenderer = new ShapeRenderer();//shapeRenderer 초기화

    }

    private void queueAssets() {
        //사용자 지정함수인 queueAssets 에서 나중에 사용할 에셋 가져오기.
        app.assets.load("StartScreen/splash.png", Texture.class);//해당 에셋을 원하는 객체로 가져오기. TextureAtlas등을 불러내 사용가능.
        app.assets.load("ui/uiskin.atlas", TextureAtlas.class);
        app.assets.load("sound/bgm.ogg", Music.class);
        app.assets.load("sound/rd.ogg", Sound.class);
    }

    @Override
    public void show() {
        System.out.println("LODING");//로딩(에셋을 불러오는것이)이 너무 빨리 완료되서 확인용
        shapeRenderer.setProjectionMatrix(app.camera.combined);//게임세계에서 사물을 렌더링 하는 위치(카메라) 설정. 로딩화면의 크기문제 등 방지.
        // https://stackoverflow.com/questions/33703663/understanding-the-libgdx-projection-matrix 참조

        this.progress = 0f;
        queueAssets();//에셋 가져오는 메서드 호출.
    }

    public void update(float delta){//모든 에셋을 업데이트 한 다음에 에셋로드가 완료되었는지 계속 확인
        //progress 변수에 에셋 로딩 진척도 저장.
        progress = MathUtils.lerp(progress, app.assets.getProgress(), .1f);//lerp의 뜻은 우리나라의 선형보간법. 두점이 주어졌을 때 그 사이에 위치한 값을 추정하기 위하여 직선 거리에 따라 선형적으로 계산하는 방법이다.
        //progress(=0f)에서 app.assets.getProgress()까지 0.1f씩 커짐. ==>lerp 메서드 의미. 시각적인 로딩창 구현 완료.
        if (app.assets.update() && progress >= app.assets.getProgress() - .001f){//앱의 에셋이 업데이트되고, 로딩창 애니메이션도 다 끝났다음의 동작 설정(보편적으로 게임에서 로딩 끝난 후 로그인이나 게임화면으로)
            //app.setScreen(new SplashScreen(app));//로딩 다 끝났으니 화면 이동
            app.setScreen(app.splashScreen);//위 주석 라인 코드와 기능은 같지만, new 인스턴스를 만드는 것으로 메모리 낭비.
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1f);//배경화면 색
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        //update메서드가 실행되는 도중에도 render메서드가 계속해서 실행되기 때문에 update함수 실행 와중에도 비동기식? 처럼 동시에 나머지 render메서드의 내용도 실행이 됨.
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);//셰이프렌더러 클래스를 통하여 로딩 그래프 구현. 셰이프렌더러 클래스는 도형을 그리기 좋은 클래스임. batch처럼 사용하면 됨. 매개변수 확인하면서 사용. Cntrl + p
        //render()메서드는 계속해서 실행이 되므로 변수 progress 변수의 값만큼 그래프가 채워질 것임.
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(32, app.camera.viewportHeight / 2 - 8, app.camera.viewportWidth -64, 16);//rectangle의 약자. 사각형 만듦.
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(32, app.camera.viewportHeight / 2 - 8, progress * (app.camera.viewportWidth -64), 16);//너비 속성에서 progress 변수의 값에 따라서 파랑색이 넓어짐을 구현.

        shapeRenderer.end();

        //app.batch.begin();
        //app.font24.draw(app.batch, "Screen: Splash", 20, 20);
        //app.batch.end();
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
       System.out.println("loading dispose");
       shapeRenderer.dispose();
    }
}
