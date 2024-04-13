package org.example.characters;

import org.example.Coords;
import org.example.Sprite;
import org.example.map.Cell;

import java.io.IOException;

public interface ICharacter {
    void UseSkill();
    void Create(Cell startCell, CharactersParams params) throws IOException;
    void GetInfo();
    Coords GetStartCoords();
    Coords GetEndCoords();
    Sprite Sprite();
}
