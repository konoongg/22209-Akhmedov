#pragma once

#include <string>
#include <vector>
#include <iostream>
#include <fstream>
#include <sstream>
#include "../cell/cell.h"
#include "../gameBoard/gameBoard.h"

class Game {
	int countStep;
	std::vector<int> countForSafe;
	std::vector<int> countForBorn;
	std::string outFile;
	std::string inFile;
	std::string mode;
	GameBoard board;
	
public:
	Game(std::vector<std::string> argc);
	const std::string gameMode() const;
	const int step() const;
	const std::vector<int>& needForSafe() const;
	const std::vector<int>& needForBorn() const;
	const std::string gameOutFile() const;
	GameBoard& returnBoard();
};