package com.mygdx.game.actors;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class Person {
    private String name;
    private int age;

    public void StoreData(String name, int age){
        this.name = name;
        this.age = age;
    }

    public void getInfo() {
        System.out.println("이름은 : " + this.name +"이고, 나이는 " + this.age + "입니다.");
    }
}