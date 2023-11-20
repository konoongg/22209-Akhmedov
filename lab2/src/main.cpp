#include <iostream>
#include <vector>
#include <string>
#include "gameController/gameController.h"
#include "inputParser/inputParser.h"
#include "gameState/gameState.h"
#include "viewer/viewer.h"

int main(int argc, char* argv[]) {
	if (argc < 2) {
		std::cout << "not enough arguments\n";
		return 0;
	}
	std::vector<std::string> arguments(argv, argv + argc);
	InputParser inputParser{ arguments };
	Game gameState{ inputParser.inputFile() };
	Viewer viewer{ gameState.returnBoard(), inputParser.gameMode()};
	GameController controller{ inputParser, gameState, viewer };
	return 0;
}