package net.trpfrog.medipro_game.mini_game.moons_work.symbols;

import net.trpfrog.medipro_game.Drawable;
import net.trpfrog.medipro_game.MainView;
import net.trpfrog.medipro_game.mini_game.moons_work.MoonsWorkModel;
import net.trpfrog.medipro_game.symbol.Symbol;
import net.trpfrog.medipro_game.util.ResourceLoader;

import java.awt.*;

public class RocketLiveCount extends Symbol implements Drawable {
    private final int MAX_COUNT = 3;
    private int count = MAX_COUNT;
    private int imageSize = 50;
    private MoonsWorkModel model;
    private Rectangle mainViewRect = MainView.getInstance().getBounds();

    private static final Image
            LIVE_ROCKET = ResourceLoader.readImage(
                    ".", "resource", "mini_game", "moons_work", "tilt_rocket.png"
            ),
            DIED_ROCKET = ResourceLoader.readImage(
                    ".", "resource", "mini_game", "moons_work", "tilt_rocket_shadow.png"
            );

    public RocketLiveCount(MoonsWorkModel model) {
        this.model = model;
        setDrawer(this);
    }

    public void decrement() {
        count--;
        if(count == 0) {
            model.getEarth().explode(true);
        }
    }

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public void increment(Rocket rocket) {
        if(rocket.isReturnedToEarth()) return;
        increment();
        rocket.setReturnedToEarth(true);
    }

    @Override
    public void draw(Graphics2D g) {
        int margin = 10;
        for(int i = 0; i < Math.max(MAX_COUNT, count); i++) {
            Image image = (i < count) ? LIVE_ROCKET : DIED_ROCKET;
            int x = mainViewRect.width - (i + 1) * (imageSize + margin);
            g.drawImage(image, x, margin, imageSize, imageSize, null);
        }
    }
}
