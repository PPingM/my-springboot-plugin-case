package com.rxx.design.template;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/3/21 11:17
 */
public abstract class Game {
    abstract void initialize();
    abstract void startPlay();
    abstract void endPlay();

    //模板
    public final void play(){

        //初始化游戏
        initialize();

        //开始游戏
        startPlay();

        //结束游戏
        endPlay();
    }
}
