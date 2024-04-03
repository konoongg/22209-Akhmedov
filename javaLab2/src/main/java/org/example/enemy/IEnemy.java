package org.example.enemy;

import org.example.Coords;
import org.example.Sprite;
import org.example.map.Cell;

import java.io.IOException;

public interface IEnemy {
    void Create(Coords enemyStart) throws IOException;
    double CoordsX();
    double CoordsY();
    Sprite Sprite();

    int Damage();

    void ChangeHp(int damage);

    EnemyAnimationStatus Status();
    void Move(Cell cell);
    void ChangeSpriteTime(int timeTosleep) throws IOException;
}
