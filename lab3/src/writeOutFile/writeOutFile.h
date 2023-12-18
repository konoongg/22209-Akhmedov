#pragma once

#include <string>

#include "../wawFileHeader.h"

class WriteOutFile {
public:
	void writeToOutFile(short semplesOnOneSecByMainInputFile[44100], std::string outWawFile);
	void writeHeader(std::string outFile, TWawFileHeader& header);
};