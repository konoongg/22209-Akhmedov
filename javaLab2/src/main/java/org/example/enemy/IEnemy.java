package org.example.enemy;

import org.example.Coords;
import org.example.Sprite;
import org.example.map.Cell;

import java.io.IOException;

public interface IEnemy {
    void Create(Coords enemyStart, Cell cell) throws IOException;
    double CoordsX();
    double CoordsY();
    Sprite Sprite();

    EnemyStatus Status();
    void Move();
    void ChangeSpriteTime(int timeTosleep);
}
