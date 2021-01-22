package net.trpfrog.medipro_game.mini_game.shonben_kozou.symbols;

import net.trpfrog.medipro_game.MainView;
import net.trpfrog.medipro_game.mini_game.shonben_kozou.ShonbenKozouModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CircleManager {

    private ShonbenKozouModel model;
    private List<Circle> circles = new ArrayList<>();
    private Circle c;
    private int score;
    private MainView mv;

    public CircleManager(ShonbenKozouModel model){
        this.model = model;
        mv = MainView.getInstance();
        score = 0;
    }

    public void addCircle(){
        if(model.getGameTimer().getCount() >= 300 && model.getGameTimer().getCount() % 5 == 0 && model.getGameTimer().getCount() < 1300) {
            c = new Circle(model);
            circles.add(c);
        }
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

    public void check(){
        for(int i = 0; i < circles.size(); i++){
            if(circles.get(i).touches(model.getCup())){
                circles.remove(i);
                score++;
            }
        }
    }

    public int getScore(){
        return score;
    }

}
