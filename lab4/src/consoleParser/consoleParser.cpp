#include <iostream>

#include "consoleParser.h"
#include "../exception/wrongConsoleParam/wrongConsoleParam.h"

ConsoleParser::ConsoleParser(std::vector<std::string> arguments) {
	if (itIsHelp(arguments)) {
		mode = 'h';
	}
	else {
		try {
			defineInputCSV(arguments);
		}
		catch (WrongConsoleParam& err) {
			throw err;
		}
		defineDelShi(arguments);
		mode = 'p';
	}
}

bool ConsoleParser::itIsHelp(std::vector<std::string> arguments) {
	if (arguments[1] == "--help" || arguments[1] == "-h") {
		std::cout << "-R [delimiter for Rows]" << std::endl;
		std::cout << "-C [delimiter for line]" << std::endl;
		std::cout << "-S [shielding symbol]" << std::endl;
		std::cout << "example:"  << std::endl;
		std::cout << "file.exe -R [,] -C [.]  -S [a]"  << std::endl;
		return true;
	}
	return false;
}

void ConsoleParser::defineInputCSV(std::vector<std::string> arguments) {
	if ((arguments[1].find("csv") != std::string::npos)) {
		inputCSV = arguments[1];
	}
	else {
		throw WrongConsoleParam{ "wrong file" };
	}
}

void ConsoleParser::defineDelShi(std::vector<std::string> arguments) {
	delimiterInLine = ',';
	delimiterRows = '\n';
	shieldingSym = '\"';
	for (int i = 2; i < arguments.size(); i++) {
		if (arguments[i] == "-R") {
			delimiterRows = arguments[i + 1][1];
			++i;
		}
		else if (arguments[i] == "-C") {
			delimiterInLine = arguments[i + 1][1];
			++i;
		}
		else if(arguments[i] == "-S") {
			shieldingSym = arguments[i + 1][1];
			++i;
		}
	}
}

std::string ConsoleParser::returnInputCSV() {
	return inputCSV;
}


char ConsoleParser::returnDelimiterInLine() {
	return delimiterInLine;
}

char ConsoleParser::returnDelimiterRows() {
	return delimiterRows;
}

char ConsoleParser::returnShieldingSym() {
	return shieldingSym;
}

char ConsoleParser::returnMode() {
	return mode;
}