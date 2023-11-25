#include "gameController.h"

GameController::GameController(InputParser& inputParser, Game& game, Viewer& viewer) {
	if (inputParser.gameMode() == "online") {
		isRead = false;
		std::cout << "game ready" << std::endl;
		reader(inputParser,game, viewer);
	}
	else {
		tick(inputParser.step(), inputParser, game, viewer);
		dump(inputParser.gameOutFile(), game.returnBoard());
	}
}

void GameController::reader(InputParser& inputParser, Game& game, Viewer& viewer) {
	std::cout << "enter some command" << std::endl;
	std::string input;
	while (isRead == false) {
		std::getline(std::cin, input);
		if (input == "help") {
			help();
			std::cout << "enter some command" << std::endl;
		}
		else if (input == "exit") {
			exit();
		}
		else if (input.find("tick <n=") != std::string::npos) {
			int step = std::stoi(input.substr(8, input.size() - 8 - 1));
			tick(step, inputParser, game, viewer);
			std::cout << "enter some command" << std::endl;
		}
		else if (input.find("t <n=") != std::string::npos) {
			int step = std::stoi(input.substr(5, input.size() - 5 - 1));
			tick(step, inputParser, game, viewer);
			std::cout << "enter some command" << std::endl;
		}
		else if (input == "t" || input == "tick") {
			tick(1, inputParser, game, viewer);
			std::cout << "enter some command" << std::endl;
		}
		else if (input.find("dump <") != std::string::npos) {
			std::string outFile = input.substr(6, input.size() - 6 - 1);
			dump(outFile, game.returnBoard());
			std::cout << "enter some command" << std::endl;
		}
		else {
			std::cout << "you entered an incorrect value. Type help for commands" << std::endl;
			help();
		}
	}
}

void GameController::help() {
	std::cout << "dump <filename> - save the universe to a file." << std::endl;
	std::cout << "tick <n=1> (and abbreviated t <n=1>) - calculate n (default 1) iterations and print the result." << std::endl;
	std::cout << "exit - end the game." << std::endl;
}

void GameController::dump(std::string outFile, const GameBoard& board) {
	std::ofstream file(outFile);
	if (file.is_open()) {
		for (int y = 0; y < board.gameSizeY(); ++y) {
			for (int x = 0; x < board.gameSizeX(); ++x) {
				file << board.returnCell(x,y).printValue() << ' ';
			}
			file << std::endl;
		}
		file.close();
		std::cout << "save the universe to " << outFile << std::endl;
	}
	else {
		std::cout << "failed to open file" << std::endl;
	}
}

int GameController::isNeighbour(const GameBoard& board, int x, int y) {
	if (board.returnCell(x,y).printValue() == 0) {
		return 0;
	}
	return 1;
}

void GameController::countNeighbourIsCountChange(Cell& cell, int countNeighbour, const std::bitset<8>& countChange, int isSafe) {
	bool isNeedMark = countChange.test(countNeighbour);
	if (isSafe == 1) {
		if (!isNeedMark) {
			cell.markChangeStatus();
		}
		return;
	}
	else {
		if (isNeedMark) {
			cell.markChangeStatus();
		}
	}
}
	
void GameController::needChange(Cell& cell, Game& game, int x, int y) {
	int countNeighbour = 0;
	const int sizeX = game.returnBoard().gameSizeX();
	const int sizeY = game.returnBoard().gameSizeY();
	countNeighbour += isNeighbour(game.returnBoard(), x + 1 , y);
	countNeighbour += isNeighbour(game.returnBoard(), x, y + 1);
	countNeighbour += isNeighbour(game.returnBoard(), x + 1, y + 1);
	countNeighbour += isNeighbour(game.returnBoard(), x + 1, y - 1);
	countNeighbour += isNeighbour(game.returnBoard(), x, y - 1);
	countNeighbour += isNeighbour(game.returnBoard(), x - 1, y - 1);
	countNeighbour += isNeighbour(game.returnBoard(), x - 1, y);
	countNeighbour += isNeighbour(game.returnBoard(), x - 1, y + 1);
	if (cell.printValue() == 0) {
		countNeighbourIsCountChange(cell, countNeighbour, game.needForBorn(), 0);
	}
	else {
		countNeighbourIsCountChange(cell, countNeighbour, game.needForSafe(), 1);
	}
}

void GameController::tick(int step,InputParser& inputParser, Game& game, Viewer& viewer) {
	for (int i = 0; i < step; ++i) {
		for (int y = 0; y < game.returnBoard().gameSizeY(); ++y) {
			for (int x = 0; x < game.returnBoard().gameSizeX(); ++x) {
				needChange(game.returnBoard().returnCell(x,y), game, x, y);
			}
		}
		for (int y = 0; y < game.returnBoard().gameSizeY(); ++y) {
			for (int x = 0; x < game.returnBoard().gameSizeX(); ++x) {
				if (game.returnBoard().returnCell(x, y).needChangeStatus()) {
					game.returnBoard().returnCell(x, y).changeStatus();
				}
			}
		}
		if (inputParser.gameMode() == "online") {
			Sleep(300);
			viewer.printFiel(game.returnBoard());
		}
	}

}

void GameController::exit() {
	isRead = true;
}