#pragma once

#include <tuple>
#include <sstream>
#include <string>

#include "../exception/wrongColumn/wrongColumn.h"
#include "../exception/wrongRow/wrongRow.h"

template <class... args>
class CSVParser {
    std::string CSVLine;
    std::ifstream& CSVFile;
	std::tuple<args...> CSVTuple;
	char delimiterRows;
	char delimiterInLine;
	char shieldingSym;
	int curColumn;
	int curRow;
    void readLine(std::ifstream& file, std::string& line);

	template <typename T>
	T createDataCell(std::istringstream& iss);

	template <size_t... numberArgc>
	void defineTypesInLine(std::index_sequence<numberArgc...>);

public:
    class Iterator {
		CSVParser& parser;
		bool itIsEnd;
    public:
		Iterator(CSVParser& p, bool endFlag);
        std::tuple<args...> operator*();
        Iterator& operator++();
        bool operator!=(Iterator const&  other);
		bool operator==(Iterator const&  other);
    };
    Iterator begin();
    Iterator end();
    CSVParser(std::ifstream& file, int countSkipRows, char delimiterRows, char delimiterInLine, char shieldingSym);
};

namespace {
	template <typename T>
	T castToType(std::string substr) {
		std::stringstream ss{ substr };
		T data;
		ss >> data;
		return data;
	}
}


template <class... args>
CSVParser<args...>::Iterator::Iterator(CSVParser& p, bool endFlag) : parser(p), itIsEnd{endFlag} {};

template <class... args>
CSVParser<args...>::CSVParser(std::ifstream& file, int countSkipRows, char delimiterRows, char delimiterInLine, char shieldingSym)  : CSVFile(file) {
	this->delimiterInLine = delimiterInLine;
	this->delimiterRows = delimiterRows;
	this->shieldingSym = shieldingSym;
	curColumn = 0;
	curRow = 0;
	for (int i = 0; i < countSkipRows; ++i) {
		std::string line;
		while (!file.eof()) {
			try {
				readLine(file, line);
			}
			catch (WrongColumn& err) {
				throw err;
			}
		}
	}
}

template <class... args>
void CSVParser<args...>::readLine(std::ifstream& file, std::string& line) {
	curRow = 0;
	char sym = delimiterRows + 1;
	bool shielding = false;
	while (file.get(sym) && (sym != delimiterRows || shielding == true)) {
		if (sym == shieldingSym) {
			line.append(1, sym);
			if (shielding == false) {
				shielding = true;
			}
			else {
				shielding = false;
			}
		}
		else {
			line.append(1, sym);
		}
	}
	if (line == "" && !file.eof()) {
		throw WrongColumn{ "incorrect column  with number " + std::to_string(curColumn)};
	}
	++curColumn;
}

template <typename... args>
template <typename T>
T CSVParser<args...>::createDataCell(std::istringstream& iss) {
	std::string cell;
	bool shielding = false;
	char sym;
	while (iss.get(sym) && (sym != delimiterInLine || shielding == true)) {
		if (sym == shieldingSym) {
			if (shielding == false) {
				shielding = true;
			}
			else {
				shielding = false;
			}
		}
		else {
			cell.push_back(sym);
		}
	}
	if (cell == "") {
		throw WrongColumn{ "incorrect row with column number: " + std::to_string(curColumn) + " , row number: " + std::to_string(curRow) };
	}
	std::stringstream ss(cell);
	T cellTuple;
	ss >> cellTuple;
	++curRow;
	return cellTuple;
}

template <class... args>
template <size_t... numberArgc>
void CSVParser<args...>::defineTypesInLine(std::index_sequence<numberArgc...>) {
	std::istringstream iss(CSVLine);
	try {
		((std::get<numberArgc>(CSVTuple) = createDataCell<std::tuple_element_t<numberArgc, decltype(CSVTuple)>>(iss)), ...);
	}
	catch (WrongRow& err) {
		throw err;
	}
}

template <class... args>
std::tuple<args...> CSVParser<args...>::Iterator::operator*() {
	try {
		parser.defineTypesInLine(std::index_sequence_for<args...>{});
	}
	catch (WrongRow& err) {
		throw err;
	}
	parser.CSVLine = "";
	return parser.CSVTuple;
}

template <class... args>
typename CSVParser<args...>::Iterator& CSVParser<args...>::Iterator::operator++() {
	try {
		parser.readLine(parser.CSVFile, parser.CSVLine);
		if(parser.CSVFile.eof()){
			itIsEnd = true;
		}
	}
	catch (WrongColumn& err) {
		throw err;
	}
	return *this;
}

template <class... args>
typename CSVParser<args...>::Iterator CSVParser<args...>::begin() {
	readLine(CSVFile, CSVLine);
	return CSVParser<args...>::Iterator(*this, false);
}

template <class... args>
typename CSVParser<args...>::Iterator CSVParser<args...>::end() {
	return CSVParser<args...>::Iterator(*this, true);
}

template <class... args>
bool CSVParser<args...>::Iterator::operator!=(Iterator const& other) {
	if (!(*this == other)) {
		return true;
	}
	return false;
}

template <class... args>
bool CSVParser<args...>::Iterator::operator==(Iterator const& other) {
	if (parser.CSVLine == other.parser.CSVLine && itIsEnd == other.itIsEnd ) {
		return true;
	}
	return false;
}