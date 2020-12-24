package net.trpfrog.medipro_game.mini_game.moons_work;

import net.trpfrog.medipro_game.MainView;
import net.trpfrog.medipro_game.SceneManager;
import net.trpfrog.medipro_game.dialog_background.DialogBackgroundScene;
import net.trpfrog.medipro_game.mini_game.moons_work.symbols.*;
import net.trpfrog.medipro_game.pause.PauseScene;
import net.trpfrog.medipro_game.pause.PauseWindow;
import net.trpfrog.medipro_game.scene.GameModel;
import net.trpfrog.medipro_game.space.symbols.Rocket;
import net.trpfrog.medipro_game.symbol.ImageAnimationSymbol;
import net.trpfrog.medipro_game.symbol.Symbol;
import net.trpfrog.medipro_game.util.GifConverter;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.Thread.sleep;

public class MoonsWorkModel extends GameModel {

    private Point centerPoint;
    private Background background;
    private Rectangle circleDrawArea;
    private Moon moon;
    private Earth earth;
    private DefenceCounter counter;
    private RocketLiveCount rocketLiveCount;
    private MeteoriteManager meteoriteManager;

    private boolean playing = true;

    public MoonsWorkModel() {
        MainView mv    = MainView.getInstance();
        centerPoint    = new Point(mv.getWidth()/2, mv.getHeight()/2);

        // 月の軌道を設定
        int r          = (int)(200);
        circleDrawArea = new Rectangle(centerPoint.x - r, centerPoint.y - r, r * 2, r * 2);

        background       = new Background();
        moon             = new Moon(this);
        earth            = new Earth(this);
        meteoriteManager = new MeteoriteManager(this);
        counter          = new DefenceCounter(earth.getHitJudgeRectangle());
        rocketLiveCount  = new RocketLiveCount(this);

        addSymbol(moon);
        addSymbol(earth);
        addSymbol(counter);
        addSymbol(rocketLiveCount);
    }

    public void endGame() {
        playing = false;
        var scene = new DialogBackgroundScene(new CompleteWindow(this), false);
        SceneManager.getInstance().push(scene);
    }

    public MeteoriteManager getMeteoriteManager() {
        return meteoriteManager;
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public Rectangle getCircleDrawArea() {
        return circleDrawArea;
    }

    public Moon getMoon() {
        return moon;
    }

    public Earth getEarth() {
        return earth;
    }

    public DefenceCounter getCounter() {
        return counter;
    }

    public Background getBackground() {
        return background;
    }

    public RocketLiveCount getRocketLiveCount() {
        return rocketLiveCount;
    }

    public boolean isPlaying() {
        return playing;
    }

    @Override
    public void suspend() {
        meteoriteManager.suspend();
        earth.suspend();
    }

    @Override
    public void resume() {
        meteoriteManager.resume();
        earth.resume();
    }
}
