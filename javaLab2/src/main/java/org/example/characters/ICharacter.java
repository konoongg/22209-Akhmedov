package org.example.characters;

import org.example.Coords;
import org.example.Sprite;
import org.example.enemy.EnemyAnimationStatus;
import org.example.enemy.IEnemy;
import org.example.map.Cell;

import java.io.IOException;
import java.util.ArrayList;

public interface ICharacter {
    void UseSkill(ArrayList<IEnemy> enemyList);
    void Create(Cell startCell, CharactersParams params) throws IOException;
    Coords GetStartCoords();
    Coords GetEndCoords();
    Sprite Sprite();
    void ChangeSpriteTime(int timeTosleep) throws IOException;
    void Wait(int waitTime);
    CharacterAnimationStatus Status();
}
