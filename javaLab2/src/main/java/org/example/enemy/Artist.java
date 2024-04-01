package org.example.enemy;

import org.example.Animation;
import org.example.Coords;
import org.example.Sprite;
import org.example.map.Cell;
import org.example.map.CellStatus;
import org.example.viewer.Viewer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Artist  implements  IEnemy{
    private Coords coords;
    private Cell curCell;
    private int hp;
    private float speed;
    private EnemyStatus status;
    private Sprite sprite;
    private int spriteTime;
    private Map<String, Animation> animations;

    private void DefineAnimation() throws IOException {
        animations = new HashMap<>();
        AnimationRead();
        status = EnemyStatus.Spawn;
        Animation animation = animations.get("Spawn");
        sprite = animation.GetNextSprite();
        spriteTime = animation.GetTime();
    }

    @Override
    public EnemyStatus Status(){
        return status;
    }
    @Override
    public void Create(Coords enemyStart, Cell cell) throws IOException {
        DefineAnimation();
        coords = new Coords(enemyStart.X(), enemyStart.Y());
        curCell = cell;
        hp = 100;
        speed = 10;
    }
    private void AnimationRead() throws IOException {
        String path = "/Images/Enemys/Artist";
        URL artistURL = Artist.class.getResource(path);
        if(artistURL == null){
            throw new IOException(path + " is null ");
        }
        File folder = new File(artistURL.getPath());
        if(folder.isDirectory()){
            File[] files = folder.listFiles();
            if(files.length == 0){
                throw new IOException(path + "is empty");
            }
            for(File file : files){
                String name = file.getName();
                Animation animation = new Animation(50,"/Images/Enemys/Artist/" + name, 50, 50);
                animations.put(name, animation);
            }
        }
        else{
            throw new IOException(path + " is not a folder");
        }
    }

    private void ChangeStatus(){
        if(status == EnemyStatus.Spawn){
            status = EnemyStatus.Walk;
        }
        else if(hp <= 0){
            status = EnemyStatus.Die;
        }
    }

    @Override
    public void ChangeSpriteTime(int timeTosleep){
        if(spriteTime != 0){
            spriteTime -= timeTosleep;
        }
        else{
            Animation animation = animations.get(status.toString());
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

    @Override
    public void Move(){
        CellStatus cellStatus = curCell.GetStatus();
        if(cellStatus == CellStatus.ROAD_UP){
            coords.ChangeY(coords.Y() + speed);
        }
        else if(cellStatus == CellStatus.ROAD_DOWN){
            coords.ChangeY(coords.Y() - speed);
        }
        else if(cellStatus == CellStatus.ROAD_LEFT){
            coords.ChangeY(coords.X() - speed);
        }
        else if(cellStatus == CellStatus.ROAD_DOWN){
            coords.ChangeY(coords.X() + speed);
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
}
