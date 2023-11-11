#include "gameController.h"


GameController::GameController(Game& game, Viewer& viewer) {
	if (game.gameMode() == "online") {
		isRead = false;
		std::cout << "game ready" << std::endl;
		reader(game, viewer);
	}
	else {
		tick(game.step(), game, viewer);
		dump(game.gameOutFile(), game.returnBoard());
	}
}

void GameController::reader(Game& game, Viewer& viewer) {
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
			tick(step, game, viewer);
			std::cout << "enter some command" << std::endl;
		}
		else if (input.find("t <n=") != std::string::npos) {
			int step = std::stoi(input.substr(5, input.size() - 5 - 1));
			tick(step, game, viewer);
			std::cout << "enter some command" << std::endl;
		}
		else if (input == "t" || input == "tick") {
			tick(1, game, viewer);
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
		int countCell = 0;
		for (auto cell: board.gameBoard()) {
			countCell++;
			file << cell.printValue() << ' ';
			if (countCell % board.gameSizeX() == 0) {
				file << std::endl;
			}
		}
		file.close();
		std::cout << "save the universe to " << outFile << std::endl;
	}
	else {
		std::cout << "failed to open file" << std::endl;
	}
}

int GameController::isNeighbour(const GameBoard& board, int x, int y) {
	if (board.gameBoard()[board.realIndex(x, y)].printValue() == 0) {
		return 0;
	}
	return 1;
}

void GameController::countNeighbourIsCountChange(Cell& cell , int countNeighbour, const std::vector<int>& countChange, int isSafe) {
	for (auto count : countChange) {
		if (countNeighbour == count) {
			if (isSafe == 1) {
				return;
			}
			cell.markChangeStatus();
			return;
		}
	}
	if (isSafe == 1) {
		cell.markChangeStatus();
	}
}

void GameController::needChange(Cell& cell, Game& game) {
	int countNeighbour = 0;
	const int sizeX = game.returnBoard().gameSizeX();
	const int sizeY = game.returnBoard().gameSizeY();
	countNeighbour += isNeighbour(game.returnBoard(), (cell.X() + 1) % sizeX, cell.Y());
	countNeighbour += isNeighbour(game.returnBoard(), cell.X(), (cell.Y() + 1) % sizeY);
	countNeighbour += isNeighbour(game.returnBoard(), (cell.X() + 1) % sizeX, (cell.Y() + 1) % sizeY);
	countNeighbour += isNeighbour(game.returnBoard(), (cell.X() + 1) % sizeX, (cell.Y() - 1 + sizeY) % sizeY);
	countNeighbour += isNeighbour(game.returnBoard(), cell.X(), (cell.Y() - 1 + sizeY) % sizeY);
	countNeighbour += isNeighbour(game.returnBoard(), (cell.X() - 1 + sizeX) % sizeX, (cell.Y() - 1 + sizeY) % sizeY);
	countNeighbour += isNeighbour(game.returnBoard(), (cell.X() - 1 + sizeX) % sizeX, cell.Y());
	countNeighbour += isNeighbour(game.returnBoard(), (cell.X() - 1 + sizeX) % sizeX, (cell.Y() + 1) % sizeY);
	if (cell.printValue() == 0 ) {
		countNeighbourIsCountChange(cell, countNeighbour, game.needForBorn(), 0);
	}
	else {
		countNeighbourIsCountChange(cell, countNeighbour, game.needForSafe(), 1);
	}
}

void GameController::tick(int step, Game& game, Viewer& viewer) {
	for (int i = 0; i < step; ++i) {
		for (int y = 0; y < game.returnBoard().gameSizeY(); ++y) {
			for (int x = 0; x < game.returnBoard().gameSizeX(); ++x) {
				needChange(game.returnBoard().gameBoard() [game.returnBoard().realIndex(x,y)], game);
			}
		}
		for (int y = 0; y < game.returnBoard().gameSizeY(); ++y) {
			for (int x = 0; x < game.returnBoard().gameSizeX(); ++x) {
				if (game.returnBoard().gameBoard()[game.returnBoard().realIndex(x, y)].needChangeStatus()) {
					game.returnBoard().gameBoard()[game.returnBoard().realIndex(x, y)].changeStatus();
				}
			}
		}
		if (game.gameMode() == "online") {
			Sleep(300);
			viewer.printFiel(game.returnBoard());
		}
	}

}

void GameController::exit() {
	isRead = true;
}