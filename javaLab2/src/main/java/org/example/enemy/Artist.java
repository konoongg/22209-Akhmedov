package org.example.enemy;

import org.example.Coords;
import org.example.Sprite;

public class Artist  implements  IEnemy{
    private Coords coords;
    private int hp;
    private float speed;

    private Sprite sprite;
    @Override
    public void Create(Coords enemyStart){
        sprite = new Sprite("/Images/Enemys/artist.png", 50, 50);
        coords = new Coords(enemyStart.X(), enemyStart.Y());
        hp = 100;
        speed = 10;
    }

    @Override
    public void Move(double x, double y){
        coords.ChangeX(x);
        coords.ChangeY(y);
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
