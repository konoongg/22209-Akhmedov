package org.example.enemy;

import org.example.Coords;

public interface IEnemy {
    Coords cords = null;
    float speed = 0;
    void Move();
}
