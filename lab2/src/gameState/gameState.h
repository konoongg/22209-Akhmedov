#pragma once

#include <string>
#include <vector>
#include <bitset>
#include "../cell/cell.h"
#include "../gameBoard/gameBoard.h"
#include "../inputParser/inputParser.h"
class Game {
	int countStep;
	std::bitset<8> countForSafe;
	std::bitset<8> countForBorn;
	GameBoard board;
	
public:
	Game(std::string inFile);
	const std::bitset<8>& needForSafe() const;
	const std::bitset<8>& needForBorn() const;
	const std::string gameOutFile() const;
	GameBoard& returnBoard();
};