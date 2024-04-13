package org.example.characters;

import org.example.Coords;
import org.example.Sprite;
import org.example.animation.Animation;
import org.example.animation.AnimationReader;
import org.example.enemy.EnemyAnimationStatus;
import org.example.map.Cell;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Scientist  implements ICharacter{
    private Cell characterStartCell;
    private CharactersParams params;

    private CharacterAnimationStatus status;
    private Sprite sprite;
    private int spriteTime;
    private Map<String, Animation> animations;

    @Override
    public void UseSkill() {

    }

    private void DefineAnimation() throws IOException {
        animations = new HashMap<>();
        AnimationReader animationReader = new AnimationReader("/Images/Characters/Scientist", animations);
        status = CharacterAnimationStatus.ATACK;
        Animation animation = animations.get("Atack");
        sprite = animation.GetNextSprite();
        spriteTime = animation.GetTime();
    }
    @Override
    public void Create(Cell characterStart, CharactersParams params) throws IOException {
        DefineAnimation();
        this.characterStartCell = characterStart;
        this.params = params;
    }

    @Override
    public void GetInfo() {

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
}
