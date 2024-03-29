package net.trpfrog.medipro_game.space.symbols;

import net.trpfrog.medipro_game.SceneManager;
import net.trpfrog.medipro_game.mini_game.MiniGameScene;
import net.trpfrog.medipro_game.symbol.RelativeHitBox;

import java.awt.*;

public class EventStar extends Star {
    private RocketEvent event;

    public EventStar(Image starImage, int radius, RocketEvent event) {
        super(starImage, radius);
        this.event = event;
        setRelativeHitBox(RelativeHitBox.makeCircle(radius));
    }

    public EventStar(Image starImage, RocketEvent event) {
        this(starImage, 300, event);
    }

    public EventStar() {}

    public void setEvent(RocketEvent event) {
        this.event = event;
    }

    public RocketEvent getEvent() {
        return event;
    }



}
