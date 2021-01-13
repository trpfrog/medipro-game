package net.trpfrog.medipro_game.mini_game.shooting_star.symbols;

import net.trpfrog.medipro_game.Drawable;
import net.trpfrog.medipro_game.MainView;
import net.trpfrog.medipro_game.mini_game.shooting_star.ShootingStarModel;
import net.trpfrog.medipro_game.symbol.Symbol;

import javax.swing.*;
import java.awt.*;

public class ShootingStar extends Symbol implements Drawable {

    private static int centerX = MainView.getInstance().getWidth() / 2;
    private static int centerY = MainView.getInstance().getWidth() * 2;

    public static final int START_ANGLE = 180;
    public static final int END_ANGLE   = 270;

    private Rectangle drawRange;
    private boolean lookedByCouple = false;
    private SubtractCount subtractCount;
    private ShootingStarModel model;

    private Timer timer = new Timer(10, e -> turnStarAutomatically());

    public ShootingStar(ShootingStarModel model) {
        setAngleDegrees(180);
        setDrawer(this);
        this.model = model;

        int radius = (int)(MainView.getInstance().getWidth() * 1.5
                + Math.random() * MainView.getInstance().getWidth() * 0.5);

        MainView mv = MainView.getInstance();
        drawRange = new Rectangle(centerX, centerY, 0, 0);
        drawRange.grow(radius, radius);
        setX(drawRange.getCenterX());
        setY(drawRange.getCenterY());

        timer.start();
    }

    public boolean isOutdated() {
        return START_ANGLE < getAngleDegrees() && getAngleDegrees() <= END_ANGLE;
    }

    private SubtractCount createSubtractCount() {
        int r = drawRange.width / 2;
        return new SubtractCount(
                getX() + r * calcSightLineX(),
                getY() - r * calcSightLineY()
        );
    }

    public boolean isLooked(Couple couple) {
        if(lookedByCouple) return true;
        lookedByCouple =  couple.looksAtTheSky() &&
                180 - 45 <= getAngleDegrees() && getAngleDegrees() <= 45;

        if(lookedByCouple) {
            subtractCount = createSubtractCount();
            model.setScore(Math.max(0, model.getScore() - 10));
        }

        return lookedByCouple;
    }

    private void turnStarAutomatically() {
        if(isOutdated()) timer.stop();
        this.turnClockwiseDegrees(2);
    }

    public SubtractCount getSubtractCount() {
        return subtractCount;
    }

    @Override
    public void draw(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g;

        if(subtractCount != null) subtractCount.getDrawer().draw(g2);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(5));
        g2.drawArc(drawRange.x, drawRange.y, drawRange.width, drawRange.height,
                (int)getAngleDegrees(), 5);
    }
}
