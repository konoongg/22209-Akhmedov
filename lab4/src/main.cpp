#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <tuple>

#include "consoleParser/consoleParser.h"
#include "CSVParser/CSVParser.h"
#include "exception/wrongConsoleParam/wrongConsoleParam.h"
#include "exception/wrongColumn/wrongColumn.h"
#include "exception/wrongRow/wrongRow.h"


template <typename Ch, typename Tr, int counter, class ...args>
void printElemTuple(std::basic_ostream<Ch, Tr>& os, std::tuple<args...> const& dataTuple) {
	if constexpr (counter < sizeof...(args)) {
		os << std::get<counter>(dataTuple) << std::endl;
		printElemTuple<Ch, Tr, counter + 1, args...>(os, dataTuple);
	}
	else {
		return;
	}
}

template <typename Ch, typename Tr, class ...args>
auto& operator<<(std::basic_ostream<Ch, Tr>& os, std::tuple<args...> const& dataTuple) {
	printElemTuple<Ch, Tr, 0, args...>(os, dataTuple);
	return os;
}

int main(int argc, char* argv[]) {
	if (argc < 1) {
		std::cout << "not enough arguments\n";
		return 1;
	}
	std::vector<std::string> arguments(argv, argv + argc);
	try {
		ConsoleParser consoleParser{ arguments };
	}
	catch (WrongConsoleParam& err) {
		std::cout << err.what() << std::endl;
		return err.returnCode();
	}
	try {
		ConsoleParser consoleParser{ arguments };
	}
	catch (WrongColumn& err) {
		std::cout << err.what() << std::endl;
		return err.returnCode();
	}
	ConsoleParser consoleParser{ arguments };
	std::ifstream file(consoleParser.returnInputCSV());
	CSVParser<int, std::string> parser(file, 0, consoleParser.returnDelimiterRows(), consoleParser.returnDelimiterInLine(), consoleParser.returnShieldingSym());
	if (file.is_open()) {
		try {
			for (std::tuple<int, std::string> rs : parser) {
				std::cout << rs << std::endl;
			}
		}
		catch(WrongColumn& err) {
			std::cout << err.what() << std::endl;
			return err.returnCode();
		}
		catch (WrongRow& err) {
			std::cout << err.what() << std::endl;
			return err.returnCode();
		}
		
	}
	else {
		std::cout << "can't open file " + consoleParser.returnInputCSV() << std::endl;
		return 2;
	}
    return 0;
}