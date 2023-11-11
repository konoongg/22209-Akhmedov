#pragma once

#include <vector>
#include <iostream>
#include <cstdlib>
#include "../gameState/gameState.h"

class Viewer {
public:
	Viewer(const GameBoard& gameBoard, std::string mode);
	~Viewer();
	void printFiel(const GameBoard& gameBoard);
};