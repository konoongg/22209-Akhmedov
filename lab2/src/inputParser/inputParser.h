#pragma once

#include <string>
#include <vector>
#include "../cell/cell.h"
#include "../gameBoard/gameBoard.h"

class InputParser {
	int countStep;
	std::string outFile;
	std::string inFile;
	std::string mode;
	void defineInputFile(std::string argc);
public:
	InputParser(std::vector<std::string> argc);
	const std::string inputFile() const;
	const std::string gameMode() const;
	const int step() const;
	const std::string gameOutFile() const;
};