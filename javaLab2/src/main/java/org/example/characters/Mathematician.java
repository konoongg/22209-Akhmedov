package org.example.characters;

import org.example.Coords;
import org.example.Sprite;
import org.example.animation.Animation;
import org.example.animation.AnimationReader;
import org.example.enemy.IEnemy;
import org.example.map.Cell;
import org.example.map.CellEffect;
import org.example.map.CellStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Mathematician implements ICharacter{
    private Cell characterStartCell;
    private CharactersParams params;
    private CharacterAnimationStatus status;
    private Sprite sprite;
    private int spriteTime;
    private Map<String, Animation> animations;
    private int range;

    @Override
    public void UseSkill(ArrayList<IEnemy> enemyList, Cell[] cells) {
        Coords myStartCoords = characterStartCell.GetStartCoords();
        double myX = myStartCoords.X();
        double myY = myStartCoords.Y();
        for(Cell cell : cells){
            Coords cellStartCoords = cell.GetStartCoords();
            double cellX = cellStartCoords.X();
            double cellY = cellStartCoords.Y();
            double distanceX = Math.abs(cellX - myX);
            double distanceY = Math.abs(cellY - myY);
            if(distanceX <= range && distanceY <= range){
                CellStatus cellStatus = cell.GetStatus();
                if(cellStatus == CellStatus.ROAD_DOWN || cellStatus == CellStatus.ROAD_UP ||  cellStatus == CellStatus.ROAD_LEFT || cellStatus == CellStatus.ROAD_RIGHT){
                    if(cell.HaveEffect(CellEffect.SLOW)){
                        continue;
                    }
                    cell.AddEffect(CellEffect.SLOW);
                }
            }
        }
    }

    @Override
    public void Create(Cell startCell, CharactersParams params) throws IOException {
        DefineAnimation();
        range = startCell.GetSize();
        this.characterStartCell = startCell;
        this.params = params;
    }

    private void DefineAnimation() throws IOException {
        animations = new HashMap<>();
        AnimationReader animationReader = new AnimationReader("/Images/Characters/Mathematician", animations);
        status = CharacterAnimationStatus.Atack;
        Animation animation = animations.get("Atack");
        sprite = animation.GetNextSprite();
        spriteTime = animation.GetTime();
    }

    @Override
    public Coords GetStartCoords() {
        Coords startCoords = characterStartCell.GetStartCoords();
        return startCoords;
    }

    @Override
    public Coords GetEndCoords() {
        int countCellX = params.GetSizeCellX();
        int countCellY = params.GetSizeCellY();
        int cellSize = characterStartCell.GetSize();
        double x = characterStartCell.GetStartCoords().X() + countCellX * cellSize;
        double y = characterStartCell.GetStartCoords().Y() + countCellY * cellSize;
        Coords endCoords = new Coords(x, y);
        return endCoords;
    }

    @Override
    public Sprite Sprite() {
        return sprite;
    }

    @Override
    public void ChangeSpriteTime(int timeTosleep) throws IOException {
        if(spriteTime > 0){
            spriteTime -= timeTosleep;
        }
        else {
            Animation animation = animations.get(status.toString());
            if (animation == null) {
                throw new IOException("empty animation");
            }
            sprite = animation.GetNextSprite();
            spriteTime = animation.GetTime();
        }
    }

    @Override
    public void Wait(int waitTime) {
        //no wait
    }

    @Override
    public CharacterAnimationStatus Status() {
        return status;
    }
}
