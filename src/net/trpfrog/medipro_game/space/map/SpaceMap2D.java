package net.trpfrog.medipro_game.space.map;

import net.trpfrog.medipro_game.fieldmap.FieldMap;
import net.trpfrog.medipro_game.space.symbols.EventStar;
import net.trpfrog.medipro_game.space.symbols.Star;
import net.trpfrog.medipro_game.symbol.Symbol;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 宇宙の地図(平面)を管理するクラス。
 * @author つまみ
 */
public class SpaceMap2D extends FieldMap {

    private boolean[][] visited;
    private List<EventStar> eventStars = new LinkedList<>();
    private Image backgroundImage;

    public SpaceMap2D(int numberOfVerticalChunks,
                      int numberOfHorizontalChunks,
                      int chunkSquareLength) {

        super(numberOfHorizontalChunks, numberOfVerticalChunks, chunkSquareLength);
        visited = new boolean[numberOfHorizontalChunks][numberOfVerticalChunks];

        for (boolean[] booleans : visited) {
            Arrays.fill(booleans, false);
        }
    }

    /**
     * 指定した座標が未踏のチャンクであればチャンク内に星オブジェクトを自動生成します。
     * @param x x座標
     * @param y x座標
     */
    public void generateStars(int x, int y) {
        if(!isWithin(x, y)) return;
        int chunkSize = getChunkSquareLength();
        int cx = x/chunkSize, cy = y/chunkSize;
        if(visited[cx][cy]) return;

        double AUTO_GENERATED_STAR_DENSITY = 0.000005;

        for(int i = cx * chunkSize; i < (cx + 1) * chunkSize; i++) {
            for(int j = cy * chunkSize; j < (cy + 1) * chunkSize; j++) {
                if(Math.random() > AUTO_GENERATED_STAR_DENSITY) continue;

                Star star = Star.getRandomStar();
                star.setLocation(i, j);
                addSymbol(i, j, star);
            }
        }

        visited[cx][cy] = true;
    }

    @Override
    public void addSymbol(int x, int y, Symbol symbol) {
        if(symbol instanceof EventStar) eventStars.add((EventStar) symbol);
        super.addSymbol(x, y, symbol);
    }

    public List<EventStar> getEventStars() {
        return eventStars;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    /**
     * 指定した範囲に未踏のチャンクがあればそのチャンク内に星オブジェクトを自動生成します。
     * @param range 範囲
     */
    public void generateStars(final Rectangle range) {
        for(int i = range.x; i < range.x + range.width; i += getChunkSquareLength()) {
            for(int j = range.y; j < range.y + range.height; j += getChunkSquareLength()) {
                generateStars(i, j);
            }
        }
    }

    @Override
    public Stream<Symbol> rangeSymbolStream(Rectangle range) {
        generateStars(range);
        return super.rangeSymbolStream(range);
    }
}

