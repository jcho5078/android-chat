package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.actors.Person;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import sun.net.www.http.HttpClient;

public class HttpScreen implements Screen {

    //application reference
    private MyGdxGame app;

    //Stage var
    private Stage stage;
    private Skin skin;

    //Grid reference
    private String status;

    //Label reference
    private Label labelInfo;

    //Net refernce
    private Map parameter = new HashMap();
    Person person = new Person();

    private URLConnection urlConnection;
    private Net.HttpRequest httpGet = new Net.HttpRequest(Net.HttpMethods.GET);
    //private Net.HttpRequest httpPOST = ;
    private Net.HttpResponse httpResponse;

    public HttpScreen(final MyGdxGame app){
        this.app = app;
        this.stage = new Stage(new StretchViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, app.camera));

    }

    private void initInfoLabel(){
        labelInfo = new Label(status, skin, "default");
        labelInfo.setPosition(app.camera.viewportWidth / 4,(app.camera.viewportHeight / 5 * 4)-5);//라벨 위치 선정.
        labelInfo.setAlignment(Align.center);
        labelInfo.addAction(Actions.sequence(Actions.alpha(0f),Actions.delay(.5f),Actions.fadeIn(.5f)));
        stage.addActor(labelInfo);
    }

    private void httpRequestGetM(){
        parameter.put("user", "myuser");//접속자
        httpGet.setUrl("http://localhost:8080");
        httpGet.setContent(HttpParametersUtils.convertHttpParameters(parameter));
        Gdx.net.sendHttpRequest(httpGet, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                status = httpResponse.getResultAsString();
                System.out.println(status);
            }

            @Override
            public void failed(Throwable t) {
                //status = "connect failed";
                System.out.println("fail");
            }

            @Override
            public void cancelled() {
                //status = "connect failed";
                System.out.println("cancelled");
            }
        });
    }

    public void httpRequestPostM(Object requestObject, String method) {//JSON 받아오는 메서드.
        final Json json = new Json();
        final String requestJson = json.toJson(requestObject); // this is just an example
        Net.HttpRequest request = new Net.HttpRequest(method);
        final String url = "http://localhost:8080/data";
        request.setUrl(url);

        request.setContent(requestJson);

        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                if(statusCode != HttpStatus.SC_OK) {
                    System.out.println("Request Failed");
                    return;
                }
                String responseJson = httpResponse.getResultAsString();

                Gson gson = new Gson();//string convert to json sec try
                Person data = gson.fromJson(responseJson, Person.class);
                data.StoreData("냥냥맨", 155);
                //JsonArray jsonArray = jsonObject.getAsJsonArray();

                try {
                    //DO some stuff with the response string
                    //System.out.println(responseJson);
                    data.getInfo();
                }
                catch(Exception exception) {
                    exception.printStackTrace();
                }
            }
            public void failed(Throwable t) {
                System.out.println("Request Failed Completely");
            }
            @Override
            public void cancelled() {
                System.out.println("request cancelled");
            }
        });
    }

    private void httpRequestGson(){

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        this.stage.clear();

        this.skin = new Skin();
        this.skin.addRegions(app.assets.get("ui/uiskin.atlas", TextureAtlas.class));//skin객체에 디자인 넣기. 매개변수 atlas 파일
        this.skin.add("default-font", app.font24);//skin 객체에 폰트 설정. uiskin.json에서 설정되어있는 default-font로 font24객체를 넣어줌.
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));//마지막에 설정된 값과 json파일 skin객체로 로드.

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

        //httpRequestGetM();
        httpRequestPostM(person, "POST");
        initInfoLabel();
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

    }
}
