package org.example.characters;

import org.example.Coords;

public interface ICharacter {
    Coords coord = null;
    int sizeX = 2;
    int sizeY = 2;
    void UseSkill();

    void Create(Coords characterStart);
    void GetInfo();
}
