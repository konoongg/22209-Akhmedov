#pragma once

#include <vector>
#include <iostream>
#include <cstdlib>
#include "../gameState/gameState.h"

class Viewer {
public:
	Viewer(const Game& game);
	~Viewer();
	void printFiel(const Game& game);
};