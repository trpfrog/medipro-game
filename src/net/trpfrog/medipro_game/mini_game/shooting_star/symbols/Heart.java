package net.trpfrog.medipro_game.mini_game.shooting_star.symbols;

import net.trpfrog.medipro_game.symbol.Symbol;
import net.trpfrog.medipro_game.util.ResourceLoader;

import java.awt.*;

public class Heart extends Symbol {

    public static final Image HEART = ResourceLoader.readImage(
            ".", "resource", "mini_game", "shooting_star", "heart.png"
    );

    private float alpha = 1f;

    public Heart(double x, double y) {
        super(x, y);
        setDrawer(g -> {
            translate(0, -1);
            alpha -= 0.01f;
            if(alpha < 0) return;
            int size = 50;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g.drawImage(HEART, (int)getX() - size/2, (int)getY() - size/2, size, size, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        });
    }

    public float getAlpha() {
        return alpha;
    }
}
