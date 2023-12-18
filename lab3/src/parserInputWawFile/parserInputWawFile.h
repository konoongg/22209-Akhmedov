#pragma once

#include <string>

#include "../wawFileHeader.h"

class ParserInputWawFile {
	void fillConstantFields(TWawFileHeader& header);
	void fillFromInputFileFields(std::string inputWawFile, TWawFileHeader& header);
	void defineSubchunk2Size(char subchunk2Size[4], TWawFileHeader& header);
	unsigned long fromCharArrayToInt(char array[4]);
	void defineChunkSize(TWawFileHeader& header);
	void defineByteRate(TWawFileHeader& header);
public:
	TWawFileHeader returnHeader(std::string inputWawFile);
	int defineDate(std::ifstream& file);
};