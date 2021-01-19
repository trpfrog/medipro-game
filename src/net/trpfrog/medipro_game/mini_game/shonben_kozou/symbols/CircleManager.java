package net.trpfrog.medipro_game.mini_game.shonben_kozou.symbols;

import net.trpfrog.medipro_game.mini_game.shonben_kozou.ShonbenKozouModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CircleManager {

    private ShonbenKozouModel model;
    private List<Circle> circles = new ArrayList<Circle>();
    private Circle c;

    public CircleManager(ShonbenKozouModel model){
        this.model = model;
    }

    public void addCircle(){
        c = new Circle(model);
        circles.add(c);
    }

    public void move(){
        for(int i = 0; i < circles.size(); i++){
            circles.get(i).move();
        }
    }

    public void draw(Graphics2D g2){
        for(int i = 0; i < circles.size(); i++){
            circles.get(i).getDrawer().draw(g2);
        }
    }

}
