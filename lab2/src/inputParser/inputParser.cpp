#include "inputParser.h"

InputParser::InputParser(std::vector<std::string> argc) {
	outFile = "none";
	mode = "online";
	inFile = "standart.txt";
	if (argc.size() > 1) {
		defineInputFile(argc[1]);
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
}

void InputParser::defineInputFile(std::string argc) {
	if ( argc.find(".txt") != std::string::npos) {
		inFile = argc;
	}
}

const std::string InputParser::gameMode() const {
	return mode;
}

const int InputParser::step() const {
	return countStep;
}

const std::string InputParser::gameOutFile() const {
	return outFile;
}

const std::string InputParser::inputFile() const {
	return inFile;
}