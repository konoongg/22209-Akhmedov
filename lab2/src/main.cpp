#include <iostream>
#include <vector>
#include <string>
#include "gameController/gameController.h"
#include "gameState/gameState.h"
#include "viewer/viewer.h"

int main(int argc, char* argv[]) {
	if (argc < 2) {
		std::cout << "not enough arguments\n";
		return 0;
	}
	std::vector<std::string> arguments(argv, argv + argc);
	Game gameState{ arguments };
	Viewer viewer{ gameState.returnBoard(), gameState.gameMode()};
	GameController controller{ gameState, viewer };
	return 0;
}