package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGdxGame;
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
//2d에서 일어나는 action 관련 메서드들 사용 가능 패키지.


public class SplashScreen implements Screen {

    private final MyGdxGame app;
    private Stage stage;//새로운 무대 만들기.

    private Image splashImg;

    //생성자 생성.
    //Screen 클래스의 생성자는 변수와 모든 값들을 초기화 하는 역할 수행.
    public SplashScreen(final MyGdxGame app){
        this.app = app;
        this.stage = new Stage(new StretchViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, app.camera));//새로운 무대를 만들기위한 stage객체 생성.
        // 여기서는 stage객체를 생성하기 위한 여러가지 viewport중 StretchViewport를 통하여 구성.
        //Viewport 종류마다 다른 효과를 지님. FitViewport에서는 요소들이 정해진 크기를 유지함.
        //StretchViewport 에서는 화면의 크기 조절에 따라서 요소들의 크기도 달라짐.
        //FillViewport에서는 너비를 유지하긴 하지만 비효율적. 요소들이 화면 밖에 나갈수도 있음.
    }

    //Screen 인터페이스의 메서드들.
    @Override
    public void show() {//게임 설정 초기화 재설정 역할. 값 불러오기. (다음 레벨로 이동 등)
        System.out.println("SPLASH show...");

        Gdx.input.setInputProcessor(stage);//무대에 액터를 추가하기 위한것. 플레이어 상호작용 및 입력 이벤트를 허용하는 것 등.
        Texture splashTex =  app.assets.get("StartScreen/splash.png",Texture.class);//에셋을 Texture객체로서 불러들이기. LodingScreen의 update 메서드에서 부른 에셋 사용.
        // new Texture(Gdx.files.internal("StartScreen/splash.png"));// Texture객체에 파일 넣기.
        splashImg = new Image(splashTex);
        //splashImg.setPosition(stage.getWidth() /2 -16, stage.getHeight() / 2 -16); // 32x32f
        //show 메서드로 이동.
        splashImg.setOrigin(splashImg.getWidth() /2, splashImg.getHeight()/2);
        splashImg.setPosition(stage.getWidth() /2 -32, stage.getHeight() / 2 +32); // 32x32f

        Runnable transeformationRunnable = new Runnable() {//스플래쉬 이미지 완료된후 Runnable 객체 실행.
            @Override
            public void run() {
                app.setScreen(app.mainMenuScreen);//스크린 이동.
            }
        };

        //splashImg.addAction(Actions.alpha(0.5f));
        //Actions클래스: 객체의 동적인 움직임 등을 취할 수 있게 도와주는 역할 부여.
        //Actuons클래스의 alpha 메서드는 지정한 해당 객체를 약간 반투명하게 만들어주는 역할을 수행함.
        //splashImg.addAction(Actions.alpha(0f));
        //splashImg.addAction(Actions.fadeIn(1f));//css의 fadeIn마냥 투명한 상태에서 점점 드러나는 효과 연출 가능. fadeOut은 반대로 투명한상태로 변하는 효과.
        //splashImg.addAction(Actions.sequence(Actions.alpha(0f),Actions.fadeIn(1f)));//sequence 메서드를 통하여 효과를 한번에 지정 가능.
        // 위의 alpha 효과와 fadeIn 메서드의 효과를 동시에 순차적으로 연출가능.
        splashImg.addAction(Actions.sequence(Actions.alpha(0), Actions.scaleBy(.1f,.1f),
                Actions.parallel(Actions.fadeIn(2f,Interpolation.pow2),
                        Actions.scaleTo(2f,2f,2.5f,Interpolation.pow5),
                        Actions.moveTo(stage.getWidth() /2 -32, stage.getHeight() /2 - 32 ,2f,Interpolation.swing)),
                        Actions.delay(0.3f), Actions.fadeOut(1.25f), Actions.run(transeformationRunnable)));//스플래쉬 이미지 완료되면 runnable 객체 오버라이딩 메서드 호출함.
        //parallel을 이용하면 병렬적으로 작업 수행 가능. 한꺼번에 실행.sequence에서는 순차적으로 실행이 되므로 효과를 복합적으로 넣기 위해서는 parallel 메서드를 사용해야함.

        stage.addActor(splashImg);//액터를 추가 함으로서, 이미지 화면에 나타냄.
    }

    @Override
    public void render(float delta) {
        System.out.println("render...");
        Gdx.gl.glClearColor(1f,1f,1f,1f);//배경화면 색
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);//배경화면 변화 버퍼 삭제. 이거 설정 안하면 배경화면 변경사항대로 겹쳐보임. 더러워 보이게됨.
        
        update(delta);

        stage.draw();//해당 무대 객체에서

        //app.batch.begin();
        //app.font24.draw(app.batch, "SplashScreen!",20,20);//화면에 텍스트 출력
        //app.batch.end();//batch는 게임의 효율성을 결정! GPU 사용 등.
    }

    public void update(float delta){//무대 행위 호출. 배우 물건 추가, 위치에 영향 끼치기 위한 함수
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {//스테이지 크기 조절
        System.out.println("resize...");
        stage.getViewport().update(width, height, false);//스테이지 크기 설정.
        //resize메서드의 매개변수가 화면의 크기값 가지고 있음.
    }

    @Override
    public void pause() {//게임 멈춤
        System.out.println("pause...");
    }

    @Override
    public void resume() {//게임 다시 시작
        System.out.println("resume...");
    }

    @Override
    public void hide() {//숨겨져있고, 화면이 바뀔 때 마다 로드되는 메서드
        System.out.println("hide...");
    }//게임중 저장 기능?

    @Override
    public void dispose() {//메모리 관리를 위함
        System.out.println("dispose...");
        stage.dispose();
    }
}
