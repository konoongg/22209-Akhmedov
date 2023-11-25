#pragma once

#include <iostream>
#include <vector>
#include <bitset>
#include <string>
#include "../cell/cell.h"
#include "../gameState/gameState.h"
#include "../inputParser/inputParser.h"
#include <fstream>
#include "../viewer/viewer.h"
#include <windows.h>

class GameController {
	bool isRead;
	void countNeighbourIsCountChange(Cell& cell, int countNeighbour, const std::bitset<8>& countChange, int isSafe);
	void needChange(Cell& cell, Game& game, int x, int y);
	int isNeighbour(const GameBoard& board, int x, int y);
public:
	GameController(InputParser& inputParser, Game& game, Viewer& viewer);
	void reader(InputParser& inputParser, Game& game, Viewer& viewer);
	void dump(std::string outFile, const GameBoard& board);
	void tick(int step, InputParser& inputParser, Game& game, Viewer& viewer);
	void help();
	void exit();
};