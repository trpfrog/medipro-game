package net.trpfrog.medipro_game.mini_game.shonben_kozou;

import net.trpfrog.medipro_game.MainView;
import net.trpfrog.medipro_game.mini_game.shonben_kozou.symbols.Cup;
import net.trpfrog.medipro_game.mini_game.shonben_kozou.symbols.Kozou;
import net.trpfrog.medipro_game.scene.GameModel;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ShonbenKozouModel extends GameModel {
    private Cup cup;
    private Kozou kozou;

    private boolean playing = true;

    public ShonbenKozouModel(){
        MainView mv = MainView.getInstance();

        cup = new Cup(this);
        kozou = new Kozou(this);
    }

    public Cup getCup() {
        return cup;
    }

    public Kozou getKozou(){
        return kozou;
    }

    public boolean isPlaying(){
        return playing;
    }

    @Override
    public void suspend() {

    }

    @Override
    public void resume() {

    }
}