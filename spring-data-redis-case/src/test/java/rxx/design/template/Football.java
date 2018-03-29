package com.rxx.design.template;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/3/21 11:18
 */
public class Football extends Game {

    @Override
    void endPlay() {
        System.out.println("Football Game Finished!");
    }

    @Override
    void initialize() {
        System.out.println("Football Game Initialized! Start playing.");
    }

    @Override
    void startPlay() {
        System.out.println("Football Game Started. Enjoy the game!");
    }
}