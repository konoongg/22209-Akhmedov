package org.example.enemy;

import org.example.Coords;
import org.example.Sprite;

public interface IEnemy {
    Coords coords = null;
    float speed = 10;
    int hp = 100;
    
    Sprite sprite = null;
    void Create(Coords enemyStart);
    double CoordsX();
    double CoordsY();
    Sprite Sprite();
    void Move(double x, double y);
}
