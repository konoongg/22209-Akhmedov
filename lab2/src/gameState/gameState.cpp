#include "gameState.h"


Game::Game(std::vector<std::string> argc) {
	outFile = "none";
	mode = "online";
	if (argc.size() > 1 && argc[1].find(".txt") != std::string::npos) {
		inFile = argc[1];
	}
	else {
		inFile = "standart.txt";
	}
	for (int i = 1; i < argc.size(); ++i) {
		if (argc[i] == "-o") {
			mode = "offline";
			outFile = argc[i + 1];
			i++;
		}
		else if (argc[i].find("--output=") != std::string::npos) {
			mode = "offline";
			outFile = argc[i].substr(9);
		}
		else if (argc[i] == "-i") {
			countStep = std::stoi(argc[i + 1]);
			i++;
		}
		else if (argc[i].find("--iterations=") != std::string::npos) {
			countStep = std::stoi(argc[i].substr(13));
		}
	}
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
					countForBorn.push_back(std::stoi(&sym));
				}
				else if (isdigit(sym) && isBorn == true) {
					countForSafe.push_back(std::stoi(&sym));
				}
				else if (sym == '/') {
					isBorn = true;
				}
			}
			index += 1;
		}
		else {
			countForBorn.push_back(1);
			countForBorn.push_back(2);
			countForSafe.push_back(3);
			countForSafe.push_back(4);
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
			if (board.gameBoard()[board.realIndex(x, y)].printValue() == 0) {
				board.gameBoard()[board.realIndex(x, y)].changeStatus();
			}
		}
	}
	else {
		std::cout << "failed to open file \n";
	}
}

const std::vector<int>& Game::needForSafe() const {
	return countForSafe;
}

const std::vector<int>& Game::needForBorn() const {
	return countForBorn;
}

const std::string Game::gameMode() const {
	return mode;
}

const int Game::step() const {
	return countStep;
}

const std::string Game::gameOutFile() const {
	return outFile;
}

GameBoard& Game::returnBoard() {
	return board;
}
