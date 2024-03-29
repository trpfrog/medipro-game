package net.trpfrog.medipro_game.space.symbols;

import net.trpfrog.medipro_game.Drawable;
import net.trpfrog.medipro_game.MainView;
import net.trpfrog.medipro_game.Suspendable;
import net.trpfrog.medipro_game.space.SpaceModel;
import net.trpfrog.medipro_game.space.WarpSystem;
import net.trpfrog.medipro_game.symbol.MovableSymbol;
import net.trpfrog.medipro_game.symbol.RelativeHitBox;
import net.trpfrog.medipro_game.symbol.Symbol;
import net.trpfrog.medipro_game.util.MusicPlayer;
import net.trpfrog.medipro_game.util.ResourceLoader;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * 操作キャラクターであるロケットの情報を保持するクラス。
 * @author つまみ
 */
public class Rocket extends MovableSymbol implements Suspendable{

    private int depth;
    private RocketAnimation animation;
    private SpaceModel model;
    private Timer astronautTimer;
    private Image rocketImage = ResourceLoader.readImage(
            ".","resource","space_game","rocket.png"
    );
    private WarpSystem warpSystem;

    private int imageWidth = 120;
    private int imageHeight = 80;
    private long invincibleTimeUntil = 0;
    private final long invincibleTimeDuration = 5000;

    public Rocket(SpaceModel model) {
        this.model = model;
        this.animation = new RocketAnimation(this);
        this.setDrawer(animation);
        astronautTimer = new Timer(100, e -> warpToTouchingStar());
        this.setMaxSpeed(500.0);
        this.setMinSpeed(0.0);
        initHitBox();
        warpSystem = new WarpSystem(model, this);
    }

    public WarpSystem accessToWarpSystem() {
        return warpSystem;
    }

    private void initHitBox() {
        int[] xPoints = {-30,-20,-15, 0,30,60, 30,  0,-15,-20};
        int[] yPoints = {  0, 20, 40,25,20, 0,-20,-25,-40,-20};
        Polygon shape = new Polygon(xPoints, yPoints, xPoints.length);
        var hitBox = new RelativeHitBox(shape);
        setRelativeHitBox(hitBox);
    }

    public int getImageWidth(){ return this.imageWidth;}
    public int getImageHeight(){ return this.imageHeight; }

    private void warpToTouchingStar() {

        if(System.currentTimeMillis() < invincibleTimeUntil) {
            return;
        }

        int searchRange = 1000;
        var searchRect = new Rectangle(
                (int)getX() - searchRange, (int)getY() - searchRange,
                2 * searchRange, 2 * searchRange);

        var nearbyStars = model.getRocketFloorMap().rangeSymbolStream(searchRect);

        var touchingEventStar = searchNearestMiniGameStar(nearbyStars);
        if(touchingEventStar != null) {
            touchingEventStar.getEvent().run(this, touchingEventStar);
        } else {
            nearbyStars = model.getRocketFloorMap()
                    .rangeSymbolStream(searchRect)
                    .filter(star -> !(star instanceof MiniGameStar));
            fireStarEvent(nearbyStars);
        }
    }

    // 最も近い触れている星を返す
    private MiniGameStar searchNearestMiniGameStar(Stream<Symbol> starStream) {
        var eventStar = starStream
                .filter(e -> e instanceof MiniGameStar)
                .filter(this::touches)
                .min(Comparator.comparingDouble(e -> getPoint2D().distance(e.getPoint2D())));

        if(eventStar.isEmpty()) {
            return null;
        } else {
            return (MiniGameStar) eventStar.get();
        }
    }

    private void fireStarEvent(Stream<Symbol> starStream) {
        starStream.filter(e -> e instanceof EventStar)
                .filter(this::touches)
                .map(e -> ((EventStar) e))
                .forEach(e -> e.getEvent().run(this, e));
    }

    /**
     * ロケットが存在するマップ上の深さを返します。
     * @return ロケットのマップ上の深さ
     */
    public int getDepth() {
        return depth;
    }

    /**
     * ロケットが存在するマップ上の深さを変更します。
     * @param depth ロケットのマップ上の深さ
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    public RocketAnimation getAnimation(){ return animation;}

    @Override
    public void suspend() {
        astronautTimer.stop();
        MusicPlayer.ROCKET_SE.stop();
        MusicPlayer.ROCKET_SE.setFramePosition(0);
        stop();
    }

    @Override
    public void resume() {
        invincibleTimeUntil = System.currentTimeMillis() + invincibleTimeDuration;
        astronautTimer.start();
        MusicPlayer.ROCKET_SE.loop(Clip.LOOP_CONTINUOUSLY);
        start();
    }

    public boolean isInvincible() {
        return System.currentTimeMillis() < invincibleTimeUntil;
    }

    public float calcInvincibleAlpha() {
        if(!isInvincible()) return 0;
        double ratio = (invincibleTimeUntil - System.currentTimeMillis()) / (double) invincibleTimeDuration;
        // 傾きが徐々に大きくなるような関数を適当に選んだ
        float ret = (float)((1 - Math.pow(100, -ratio))/(1 - 1f/100));
        return Math.max(Math.min(1, ret), 0);
    }

    /**
     * ロケットのアニメーションに関するメソッドを定義します。
     */
    public class RocketAnimation implements Drawable{
        private final Rocket rocket;
        private Timer damageTimer;
        private int damageCounter;
        private Image rocketNow;

        private final Image rocketImage = ResourceLoader.readImage(
                ".","resource","space_game","rocket.png");
        private final Image invincibleImage = ResourceLoader.readImage(
                ".","resource","space_game","invincibleRocket.png");
        private final Image damagedImage = ResourceLoader.readImage(
                ".","resource","space_game","rocket_damaged.png");
        private final Image rocketStopImage = ResourceLoader.readImage(
                ".","resource","space_game","rocket_stop.png");
        private final Image invincibleStopImage = ResourceLoader.readImage(
                ".","resource","space_game","invincibleRocketStop.png");

        public RocketAnimation(Rocket rocket) {
            this.rocket = rocket;

            damageTimer = new Timer(10,e->{
                rocket.setAngleDegrees(rocket.getAngleDegrees()+8);
                damageCounter++;
                if(damageCounter >= 16){
                    damageTimer.stop();
                    damageCounter = 0;
                }
            });
        }

        public void damaged() {
            rocketNow = damagedImage;
            damageTimer.start();
            damageCounter = 0;
        }

        public void drawRocket(Graphics2D g, boolean invincible) {
            MainView mainView = MainView.getInstance();
            int drawX = mainView.getWidth()/2;
            int drawY = mainView.getHeight()/2;
            double angle = getAngleRadians();

            Image image;
            if(rocket.getSpeedPxPerSecond() < 1) {
                image = invincible ? invincibleStopImage : rocketStopImage;
            } else {
                image = invincible ? invincibleImage : rocketImage;
            }
            if(damageCounter != 0) image = damagedImage;

            g.rotate(angle,drawX,drawY);
            g.drawImage(image,drawX-imageWidth/2,drawY-imageHeight/2,
                    imageWidth, imageHeight, null);
            g.rotate(-angle,drawX,drawY);
        }

        @Override
        public void draw(Graphics2D g) {
            drawRocket(g, false);
            if(isInvincible()) {
                float alpha = calcInvincibleAlpha();
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                drawRocket(g, true);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            }
        }
    }
}
