#include "viewer.h"
#include <iostream>

Viewer::Viewer(const GameBoard& gameBoard, std::string mode) {
	if (mode == "online") {
		printFiel(gameBoard);
	}
}

Viewer::~Viewer() {
	std::cout << "the end" << std::endl;
}

void Viewer::printFiel(const GameBoard& gameBoard) {
	system("cls");
	std::cout << gameBoard.gameName() << std::endl;
	for (int i = 0; i < gameBoard.gameSizeY() + 2; ++i) {
		std::cout << "-";
	}
	std::cout << std::endl;
	for (int x = 0; x < gameBoard.gameSizeX(); ++x) {
		std::cout << "|";
		for (int y = 0; y < gameBoard.gameSizeY(); ++y) {
			std::cout << gameBoard.returnCell(x,y).printValue();
		}
		std::cout << "|" << std::endl;
	}
	for (int i = 0; i < gameBoard.gameSizeY() + 2; ++i) {
		std::cout << "-";
	}
	std::cout << std::endl;
}