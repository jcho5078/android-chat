package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GraphicsDemo implements ApplicationListener {
    private SpriteBatch batch;
        private Pixmap pixmap;
        private Texture texture;
        private Sprite sprite;

        @Override
        public void create() {
            batch = new SpriteBatch();
        //pixmap은 적은 이미지 용량을 픽셀로서 구성.
        // 256넓이, 128 높이를 8바이트로 사용
        pixmap = new Pixmap(256, 128, Pixmap.Format.RGBA8888);

        pixmap.setColor(Color.RED);//빨강으로 채우기
        pixmap.fill();

        pixmap.setColor(Color.BLACK);
        pixmap.drawLine(0,0,pixmap.getWidth()-1, pixmap.getHeight()-1);
        pixmap.drawLine(0,pixmap.getHeight()-1, pixmap.getWidth()-1,0);

        pixmap.setColor(Color.YELLOW);
        pixmap.drawCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2-1);

        texture = new Texture(pixmap);

        pixmap.dispose();

        sprite = new Sprite(texture);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        sprite.setPosition(0,0);
        sprite.draw(batch);
        sprite.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {//삭제? 처리 메서드
        batch.dispose();
        texture.dispose();
    }
}
