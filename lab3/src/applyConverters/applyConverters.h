#pragma once

#include <string>
#include <map>

#include "../wawFileHeader.h"
#include "../editor/wawEditor.h"
#include "../returnerSemples/returnerSemples.h"

class ApplyConverters {
	ReturnerSamples returnerSamples;
	void writeHeader(std::string outFile, TWawFileHeader& header);
	void applyOperations(std::vector<std::string>& wawFiles, std::vector<Operation>& operations);
	void writeToOutFIle(short semplesOnOneSecByMainInputFile[44100], std::string outWawFile);
public:
	ApplyConverters(std::vector<std::string>& wawFiles, std::vector<Operation>& operations);
};