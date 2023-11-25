#include "gameState.h"

#include <iostream>
#include <fstream>
#include <sstream>

Game::Game(std::string inFile) {
	std::ifstream inputFile;
	inputFile.open(inFile);
	if (inputFile.is_open()) {
		std::string line;
		std::vector<std::string> linesForFile;
		std::string name;
		while (std::getline(inputFile, line)) {
			linesForFile.push_back(line);
		}
		int index = 1;
		if (linesForFile[index].find("#N") != std::string::npos) {
			name = linesForFile[index].substr(3);
			index += 1;
		}
		else {
			name = "live";
		}
		bool isBorn = 0;
		if (linesForFile[index].find("#R") != std::string::npos) {
			for (char sym : linesForFile[index]) {
				if (isdigit(sym) && isBorn == false) {
					countForBorn.set(std::stoi(&sym));
				}
				else if (isdigit(sym) && isBorn == true) {
					countForSafe.set(std::stoi(&sym));
				}
				else if (sym == '/') {
					isBorn = true;
				}
			}
			index += 1;
		}
		else {
			countForBorn.set(1);
			countForBorn.set(2);
			countForSafe.set(3);
			countForSafe.set(4);
		}
		std::stringstream ss(linesForFile[index].substr(3));
		std::vector<std::string> size;
		while (std::getline(ss, line, '/')) {
			size.push_back(line);
		}
		inputFile.close();
		int sizeX = std::stoi(size[0]);
		int sizeY = std::stoi(size[1]);
		board = GameBoard{ sizeX, sizeY, name };
		index += 1;
		for (int i = index; i < linesForFile.size(); ++i) {
			std::string line;
			std::vector<std::string> cord;
			std::stringstream ss(linesForFile[i]);
			while (std::getline(ss, line, ' ')) {
				cord.push_back(line);
			}
			int x = std::stoi(cord[0]);
			int y = std::stoi(cord[1]);
			if (board.returnCell(x,y).printValue() == 0) {
				board.returnCell(x, y).changeStatus();
			}
		}
	}
	else {
		std::cout << "failed to open file \n";
	}
}

const std::bitset<8>& Game::needForSafe() const {
	return countForSafe;
}

const std::bitset<8>& Game::needForBorn() const {
	return countForBorn;
}

GameBoard& Game::returnBoard() {
	return board;
}
