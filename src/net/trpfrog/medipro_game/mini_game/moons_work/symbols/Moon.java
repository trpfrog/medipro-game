package net.trpfrog.medipro_game.mini_game.moons_work.symbols;

import net.trpfrog.medipro_game.MainView;
import net.trpfrog.medipro_game.mini_game.moons_work.MoonsWorkModel;
import net.trpfrog.medipro_game.symbol.Symbol;

import javax.swing.*;
import java.awt.*;

public class Moon extends Symbol {

    private MoonsWorkModel model;

    public Moon(MoonsWorkModel model) {
        this.model = model;
        int size = 30;
        setDrawer(g -> {
            g.setColor(Color.YELLOW);
            g.fillOval((int) getX() - size / 2, (int) getY() - size / 2, size, size);
        });
        createHitJudgementRectangle(20, 20);
    }

    @Override
    public void setLocation(double x, double y) {
        Point p = (Point) model.getCenterPoint().clone();

        double angle = Math.atan2(y - p.y, x - p.x);
        double r = model.getCircleDrawArea().getWidth() / 2;
        p.translate((int)(r * Math.cos(angle)), (int)(r * Math.sin(angle)));

        super.setLocation(p.x, p.y);
    }

}