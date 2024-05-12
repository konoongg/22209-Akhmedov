package org.example.characters;

import org.example.Coords;
import org.example.Sprite;
import org.example.animation.Animation;
import org.example.animation.AnimationReader;
import org.example.enemy.EnemyAnimationStatus;
import org.example.enemy.IEnemy;
import org.example.map.Cell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Scientist  implements ICharacter{
    private Cell characterStartCell;
    private CharactersParams params;
    private CharacterAnimationStatus status;
    private Sprite sprite;
    private int spriteTime;
    private Map<String, Animation> animations;
    private int curDelay;
    private IEnemy myEnemy;
    private boolean atacked;

    @Override
    public void Create(Cell startCell, CharactersParams params) throws IOException {
        DefineAnimation();
        this.characterStartCell = startCell;
        this.params = params;
        myEnemy = null;
        curDelay = params.GetDelay();
    }

    private void  FindEnemy(ArrayList<IEnemy> enemyList){
        Coords coords = characterStartCell.GetStartCoords();
        double norma = 0;
        for(IEnemy enemy : enemyList){
            double x = enemy.CoordsX();
            double y = enemy.CoordsY();
            double enemyNorma = x*x + y*y;
            if(enemyNorma > norma){
                myEnemy = enemy;
            }
        }
    }
    @Override
    public void UseSkill(ArrayList<IEnemy> enemyList, Cell[] cells) {
        if(enemyList.isEmpty()){
            return;
        }
        if(atacked){
            return;
        }
        if(myEnemy == null || myEnemy.Status() == EnemyAnimationStatus.Died){
            FindEnemy(enemyList);
        }
        myEnemy.ChangeHp(params.GetDamage());
        atacked = true;
    }

    @Override
    public void Wait(int waitTime) {
        curDelay -= waitTime;
    }

    private void DefineAnimation() throws IOException {
        animations = new HashMap<>();
        AnimationReader animationReader = new AnimationReader("/Images/Characters/Scientist", animations);
        status = CharacterAnimationStatus.Wait;
        Animation animation = animations.get("Wait");
        sprite = animation.GetNextSprite();
        spriteTime = animation.GetTime();
    }

    private void ChangeStatus() throws IOException {
        if(curDelay <= 0 && status == CharacterAnimationStatus.Wait){
            status = CharacterAnimationStatus.Atack;
            atacked = false;
        }
        else if(status == CharacterAnimationStatus.Atack){
            curDelay = params.GetDelay();
            status = CharacterAnimationStatus.Wait;
        }
        Animation animation = animations.get(status.toString());
        if(animation == null){
            throw new IOException("empty animation");
        }
        animation.UpdateItetaror();
        sprite  = animation.GetNextSprite();
        spriteTime = animation.GetTime();
    }

    @Override
    public void ChangeSpriteTime(int timeTosleep) throws IOException {
        if(spriteTime > 0){
            spriteTime -= timeTosleep;
        }
        else{
            Animation animation = animations.get(status.toString());
            if(animation == null){
                throw new IOException("empty animation");
            }
            Sprite newSprite = animation.GetNextSprite();
            if(newSprite == null || (curDelay <= 0 && status == CharacterAnimationStatus.Wait)){
                ChangeStatus();
            }
            else{
                sprite = newSprite;
                spriteTime = animation.GetTime();
            }
        }
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
    public CharacterAnimationStatus Status() {
        return status;
    }
}
