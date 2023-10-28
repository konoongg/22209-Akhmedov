#pragma once

#include <string>
#include <vector>
#include <iostream>
#include <fstream>
#include <sstream>
#include "../cell/cell.h"

class Game {
	int countStep;
	int sizeX;
	int sizeY;
	std::vector<int> countForSafe;
	std::vector<int> countForBorn;
	std::string outFile;
	std::string inFile;
	std::string mode;
	std::string name;
	std::vector<std::vector<Cell>> stateGame;
public:
	Game(std::vector<std::string> argc);
	std::vector<std::vector<Cell>>& gameBoard();
	const std::vector<std::vector<Cell>>& gameBoard() const;
	const std::string gameMode() const;
	const std::string gameName() const;
	const int gameSizeX() const;
	const int gameSizeY() const;
	const int step() const;
	const std::vector<int>& needForSafe() const;
	const std::vector<int>& needForBorn() const;
	const std::string gameOutFile() const;
};