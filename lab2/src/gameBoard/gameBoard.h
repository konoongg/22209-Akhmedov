#pragma once
#include <vector>
#include "../cell/cell.h"
#include <string>

class GameBoard {
	std::string name;
	int sizeX;
	int sizeY;
	std::vector<Cell> board;
public:
	GameBoard::GameBoard();
	GameBoard(int sizeX, int sizeY, std::string name);
	const int realIndex(int x, int y) const;
	std::vector<Cell>& gameBoard();
	const std::vector<Cell>& gameBoard() const;
	const std::string gameName() const;
	const int gameSizeX() const;
	const int gameSizeY() const;
};
