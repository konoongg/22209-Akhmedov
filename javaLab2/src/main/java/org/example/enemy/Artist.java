package org.example.enemy;

import org.example.animation.Animation;
import org.example.animation.AnimationReader;
import org.example.Coords;
import org.example.Sprite;
import org.example.map.Cell;
import org.example.map.CellEffect;
import org.example.map.CellStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Artist  implements  IEnemy{
    private Coords coords;
    private int hp;
    private int damage;
    private float maxSpeed;
    private float curSpeed;
    private EnemyAnimationStatus status;
    private Sprite sprite;
    private int spriteTime;
    private Map<String, Animation> animations;

    private void DefineAnimation() throws IOException {
        animations = new HashMap<>();
        AnimationReader animationReader = new AnimationReader("/Images/Enemys/Artist", animations);
        status = EnemyAnimationStatus.Spawn;
        Animation animation = animations.get("Spawn");
        sprite = animation.GetNextSprite();
        spriteTime = animation.GetTime();
    }

    @Override
    public EnemyAnimationStatus Status(){
        return status;
    }
    @Override
    public void Create(Coords enemyStart) throws IOException {
        DefineAnimation();
        coords = new Coords(enemyStart.X(), enemyStart.Y());
        hp = 100;
        maxSpeed = 10;
        curSpeed = maxSpeed;
        damage = 10;
    }

    private void ChangeStatus() throws IOException {
        if(status == EnemyAnimationStatus.Spawn){
            status = EnemyAnimationStatus.Walk;
        }
        else if(hp <= 0){
            status = EnemyAnimationStatus.Die;
        }
        Animation animation = animations.get(status.toString());
        if(animation == null){
            throw new IOException("empty animation");
        }
        sprite  = animation.GetNextSprite();
    }

    @Override
    public void ChangeSpriteTime(int timeTosleep) throws IOException {
        if(spriteTime != 0){
            spriteTime -= timeTosleep;
        }
        else{
            Animation animation = animations.get(status.toString());
            if(animation == null){
                throw new IOException("empty animation");
            }
            Sprite newSprite = animation.GetNextSprite();
            if(newSprite == null){
                ChangeStatus();
            }
            else{
                sprite = newSprite;
                spriteTime = animation.GetTime();
            }
        }
    }

    private void CheckCellEffects(Cell cell){
        ArrayList<CellEffect> cellEffects = cell.GetCellEffects();
        if(cellEffects.contains(CellEffect.ACID)){
            ChangeHp(5);
        }
        if(cellEffects.contains(CellEffect.SLOW)){
            curSpeed = maxSpeed / 2;
        }
    }

    @Override
    public void Move(Cell cell){
        CheckCellEffects(cell);
        CellStatus cellStatus = cell.GetStatus();
        if(cellStatus == CellStatus.ROAD_UP){
            coords.ChangeY(coords.Y() + curSpeed);
        }
        else if(cellStatus == CellStatus.ROAD_DOWN){
            coords.ChangeY(coords.Y() - curSpeed);
        }
        else if(cellStatus == CellStatus.ROAD_LEFT){
            coords.ChangeX(coords.X() - curSpeed);
        }
        else if(cellStatus == CellStatus.ROAD_RIGHT){
            coords.ChangeX(coords.X() + curSpeed);
        }
    }

    @Override
    public double CoordsX(){
        return coords.X();
    }

    @Override
    public double CoordsY(){
        return  coords.Y();
    }

    @Override
    public Sprite Sprite(){
        return sprite;
    }

    @Override
    public int Damage() {
        return damage;
    }

    @Override
    public void ChangeHp(int damage) {
        hp -= damage;
    }
}
