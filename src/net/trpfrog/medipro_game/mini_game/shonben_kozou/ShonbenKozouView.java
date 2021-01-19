package net.trpfrog.medipro_game.mini_game.shonben_kozou;

import net.trpfrog.medipro_game.mini_game.moons_work.symbols.ExplosionAnimation;
import net.trpfrog.medipro_game.scene.GameView;

import javax.swing.*;
import java.awt.*;

public class ShonbenKozouView extends GameView {

    private ShonbenKozouModel model;
    private final Timer drawTimer = new Timer(10, e->{
        if(model.isPlaying()) repaint();
        model.getKozou().move();
    });

    public ShonbenKozouView(ShonbenKozouModel model){
        super(model);
        this.model = model;
        setBackground(new Color(0, 34, 82));

        new ExplosionAnimation();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        model.getCup().getDrawer().draw(g2);
        model.getKozou().getDrawer().draw(g2);
    }

    @Override
    public void suspend() {
        drawTimer.stop();
    }

    @Override
    public void resume() {
        drawTimer.start();
    }
}