#include "viewer.h"

Viewer:: Viewer(const Game& game) {
	if (game.gameMode() == "online") {
		printFiel(game);
	}
}

Viewer::~Viewer() {
	std::cout << "the end" << std::endl;
}

void Viewer::printFiel(const Game& game) {
	system("cls");
	std::cout << game.gameName() << std::endl;
	for (int i = 0; i < game.gameSizeY() + 2; ++i) {
		std::cout << "-";
	}
	std::cout << std::endl;
	const std::vector<std::vector<Cell>> gameBoard = game.gameBoard();
	for (int x = 0; x < game.gameSizeX(); ++x) {
		std::cout << "|";
		for (int y = 0; y < game.gameSizeY(); ++y) {
			std::cout << gameBoard[x][y].printValue();
		}
		std::cout << "|" << std::endl;
	}
	for (int i = 0; i < game.gameSizeY() + 2; ++i) {
		std::cout << "-";
	}
	std::cout << std::endl;
}