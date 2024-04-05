package org.example.characters;

import org.example.Coords;

public interface ICharacter {
    void UseSkill();
    void Create(Coords characterStart);
    void GetInfo();
}
