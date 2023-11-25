#pragma once
#include <vector>
#include "../cell/cell.h"
#include <string>

class GameBoard {
	std::string name;
	int sizeX;
	int sizeY;
	std::vector<Cell> board;
	const int realIndex(int x, int y) const;
public:
	GameBoard::GameBoard();
	GameBoard(int sizeX, int sizeY, std::string name);
	const std::string gameName() const;
	const Cell& returnCell(int x, int y) const;
	Cell& returnCell(int x, int y);
	const int gameSizeX() const;
	const int gameSizeY() const;
};
