#pragma once

#include <iostream>
#include <vector>
#include <string>
#include "../cell/cell.h"
#include "../gameState/gameState.h"
#include <fstream>
#include "../viewer/viewer.h"
#include <windows.h>

class GameController {
	bool isRead;
	void countNeighbourIsCountChange(Cell& cell, int countNeighbour, const std::vector<int>& countChange, int isSafe);
	void needChange(Cell& cell, Game& game);
	int isNeighbour(const GameBoard& board, int x, int y);
public:
	GameController(Game& game, Viewer& viewer);
	void reader(Game& game, Viewer& viewer);
	void dump(std::string outFile, const GameBoard& board);
	void tick(int step, Game& game, Viewer& viewer);
	void help();
	void exit();
};