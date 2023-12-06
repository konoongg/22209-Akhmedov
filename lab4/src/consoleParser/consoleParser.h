#pragma once

#include <string>
#include <vector>

class ConsoleParser {
	std::string inputCSV;
	char delimiterInLine;
	char delimiterRows;
	char shieldingSym;
	void defineInputCSV(std::vector<std::string> arguments);
	void defineDelShi(std::vector<std::string> arguments);
public:
	ConsoleParser(std::vector<std::string> arguments);
	std::string returnInputCSV();
	char returnDelimiterInLine();
	char returnDelimiterRows();
	char returnShieldingSym();
};