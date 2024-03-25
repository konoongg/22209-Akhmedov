package org.example.enemy;

import org.example.Coords;

public class Artist  implements  IEnemy{
    private Coords coords;
    private int hp;
    private float speed;

    String sprite;
    @Override
    public void Create(Coords enemyStart){
        sprite = "/images/artist.png";
        hp = 100;
        speed = 10;
        coords = new Coords(enemyStart.X(), enemyStart.Y());
    }

    @Override
    public void Move(double x, double y){
        coords.ChangeX(x);
        coords.ChangeY(y);
    }
}
