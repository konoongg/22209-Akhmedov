package org.example.enemy;

import org.example.Coords;

public interface IEnemy {
    Coords coords = null;
    float speed = 10;
    int hp = 100;
    void Create(Coords enemyStart);
    void Move(double x, double y);
}
