#pragma once

#include <fstream>
#include <vector>
#include <string>
#include <map>

class ReturnerSamples {
	int sampleRate;
	int blockAlign;
	std::map<int, std::ifstream> openFiles;
	std::map<int, size_t> dataIndex;
	std::map<int, std::string> indexWawFiles;
	void checkOpening(unsigned char indexWawFile);
	void openFile(unsigned char indexWawFile);
	int defineFirstSample(int lastDataByte);
public:
	ReturnerSamples(std::vector<std::string>& wawFiles);
	void returnSamples(short semplesOnOneSecSecondInputFile[44100], unsigned char indexWawFile, int curSec);
};