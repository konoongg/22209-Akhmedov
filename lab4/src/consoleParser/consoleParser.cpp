#include "consoleParser.h"
#include "../exñeption/wrongConsoleParam/wrongConsoleParam.h"

ConsoleParser::ConsoleParser(std::vector<std::string> arguments) {
	try {
		defineInputCSV(arguments);
	}
	catch (WrongConsoleParam& err) {
		throw err;
	}
	defineDelShi(arguments);

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