package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.actors.SlideButton;
import com.badlogic.gdx.Net.*;

import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;


public class PlayScreen implements Screen {
    //App reference
    private MyGdxGame app;

    //Sound reference
    private Sound sound;
    //private Sound sound = Gdx.audio.newSound(Gdx.files.internal("sound/rd.ogg"));//이 방법은 메모리 낭비가 심할 수도, 반응이 늦어질수도 있음.
    // 로딩창에서 assetmanager객체를 이용해 담아놓은 ogg파일을 이용해 메모리 낭비, 반응 저하 방지.

    //Stage var
    private Stage stage;
    private Skin skin;

    //Game Grid
    private int boardSize = 4;
    private int holX, holY;
    private SlideButton[][] buttonGrid;

    //TextButton
    private TextButton buttonBack;

    //Info label
    private Label labelInfo;//클리어 알리기 위한 라벨

    public PlayScreen(final MyGdxGame app){
        this.app = app;
        this.stage = new Stage(new StretchViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, app.camera));
    }

    //Initialize the game grid
    private void initGrid() {
        Array<Integer> nums = new Array<>();//배열생성
        for(int i=1; i < boardSize * boardSize; i++){
            nums.add(i);//최댓값을 배열에 추가.
        }
        nums.shuffle();//libgdx가 제공하는 배열에 random값 적용 시키는 메서드.

        //holX = 3;
        //holY = 3;//처음에 버튼의 배열에서 null값인 위치는 끝부분인 [3][3]이니까 3,3으로 초기화.
        holX = MathUtils.random(0, boardSize - 1);
        holY = MathUtils.random(0, boardSize - 1);//b oardSize=4 까지 값에서 랜덤값 적용.

        buttonGrid = new SlideButton[boardSize][boardSize];
        for(int i=0; i < boardSize; i++){
            for(int j=0; j < boardSize; j++){
                if(i != holY || j != holX) {
                    int id = nums.removeIndex(0);//지정된 인덱스에서 항목을 제거하고 반환하는 메서드.
                    System.out.println(id);//확인용.
                    //int id = j + 1 + (i * boardSize);
                    buttonGrid[i][j] = new SlideButton(id + "", skin, "default", id);
                    buttonGrid[i][j].setPosition((app.camera.viewportWidth / 7) * 2 + 51 * j,
                            (app.camera.viewportHeight / 5) * 3 - 51 * i);
                    buttonGrid[i][j].setSize(50, 50);

                    buttonGrid[i][j].addAction(Actions.sequence(Actions.alpha(0), Actions.delay((j + 1 + (i * boardSize)) / 15f) ,
                            Actions.parallel(
                                    Actions.fadeIn(.5f), Actions.moveBy(0, 10, .25f, Interpolation.pow5Out)
                            )));
                    // Button for the Click Listener(to slide function)
                    buttonGrid[i][j].addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            int buttonX = 0, buttonY = 0;
                            boolean buttonFound = false;
                            SlideButton selectedButton = (SlideButton) event.getListenerActor(); //눌려진 버튼에 대한 객체 생성.@@@@@@@@

                            for(int i =0; i<boardSize && !buttonFound; i++){//버튼 클릭하면 해당 버튼 값
                                for(int j = 0; j< boardSize && !buttonFound; j++){
                                    if(buttonGrid[i][j] != null && selectedButton == buttonGrid[i][j]){//눌려진 버튼이 null값이 아니라면 실행.
                                        buttonX = j;
                                        buttonY = i;
                                        buttonFound = true;
                                        System.out.println("clicked: "+buttonY+", "+buttonX);//클릭 이벤트 발생 확인용.
                                    }
                                }
                            }

                            if(holX == buttonX || holY == buttonY){//빈공간의 위치를 나타내는 값이 버튼의 위치값과 같다면 함수실행.
                                moveButtons(buttonX,buttonY);

                                if(solutionFound()){
                                    labelInfo.clearActions();
                                    labelInfo.setText("Congratulation!");
                                    labelInfo.addAction(Actions.sequence(Actions.alpha(1f),Actions.delay(1f),Actions.fadeOut(2f, Interpolation.pow5Out)));
                                }
                            }else {
                                labelInfo.clearActions();
                                labelInfo.setText("You can't to this move");
                                labelInfo.addAction(Actions.sequence(Actions.alpha(1f),Actions.delay(1f),Actions.fadeOut(2f, Interpolation.pow5Out)));
                            }
                        }
                    });

                    stage.addActor(buttonGrid[i][j]);
                }
            }
        }
    }

    private void moveButtons(int x, int y) {//매개변수 x, y는 버튼위치.
        SlideButton button;

        if(x < holX){
            for(; holX > x; holX--){//빈공간의 위치와 버튼 위치 비교 후 이동 실행.
                button = buttonGrid[holY][holX - 1];
                button.addAction(Actions.moveBy(51,0,.5f, Interpolation.pow5Out));
                buttonGrid[holY][holX] = button;
                buttonGrid[holY][holX - 1] = null;//위치 움직여진 후, 원래 있던자리 null값 넣기.
             }
        }else {
            for(; holX < x; holX++){
                button = buttonGrid[holY][holX + 1];
                button.addAction(Actions.moveBy(-51,0,.5f, Interpolation.pow5Out));
                buttonGrid[holY][holX] = button;
                buttonGrid[holY][holX + 1] = null;
            }
        }

        if(y < holY){
            for(; holY > y; holY--){
                button = buttonGrid[holY - 1][holX];
                button.addAction(Actions.moveBy(0,-51,.5f, Interpolation.pow5Out));
                buttonGrid[holY][holX] = button;
                buttonGrid[holY - 1][holX] = null;
            }
        }else {
            for(; holY < y; holY++){
                button = buttonGrid[holY + 1][holX];
                button.addAction(Actions.moveBy(0,51,.5f, Interpolation.pow5Out));
                buttonGrid[holY][holX] = button;
                buttonGrid[holY + 1][holX] = null;
            }
        }
        sound.play(1f);
        System.out.println(y+", "+x);//빈공간 위치 출력.
    }

    private boolean solutionFound(){//게임 클리어 조건 정함.
        int idCheck = 1;
        for(int i =0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(buttonGrid[i][j] != null){
                    if(buttonGrid[i][j].getId() == idCheck++){
                        if(idCheck== 16){
                            return true;
                        }
                    } else {
                        return false;
                    }
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    //Initialize the go to back button
    private void initNavigationButton() {
        buttonBack = new TextButton("Back", skin, "default");
        buttonBack.setPosition(20, app.camera.viewportHeight - 60);
        buttonBack.setSize(100,50);
        buttonBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play(1f);
                app.setScreen(app.mainMenuScreen);
            }
        });

        stage.addActor(buttonBack);
    }

    private void initInfoLabel(){
        labelInfo = new Label("Welcome to puzzle game.", skin, "default");
        labelInfo.setPosition(app.camera.viewportWidth / 4,(app.camera.viewportHeight / 5 * 4)-5);//라벨 위치 선정.
        labelInfo.setAlignment(Align.center);
        labelInfo.addAction(Actions.sequence(Actions.alpha(0f),Actions.delay(.5f),Actions.fadeIn(.5f)));
        stage.addActor(labelInfo);
    }

    @Override
    public void show() {
        System.out.println("PlayScreen");
        Gdx.input.setInputProcessor(stage);
        this.stage.clear();

        sound = app.assets.get("sound/rd.ogg", Sound.class);//

        this.skin = new Skin();
        this.skin.addRegions(app.assets.get("ui/uiskin.atlas", TextureAtlas.class));//skin객체에 디자인 넣기. 매개변수 atlas 파일
        this.skin.add("default-font", app.font24);//skin 객체에 폰트 설정. uiskin.json에서 설정되어있는 default-font로 font24객체를 넣어줌.
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));//마지막에 설정된 값과 json파일 skin객체로 로드.

        initNavigationButton();
        initInfoLabel();
        initGrid();

    }

    private void update(float delta){
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);//update 메서드에서 stage객체에 요소 추가
        stage.draw();//stage객체를 사용자에게 보여주는 역할 수행. 초중요 @@@@@@@
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
        sound.dispose();
    }
}
