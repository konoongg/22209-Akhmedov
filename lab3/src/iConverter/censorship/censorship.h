#pragma once

#include <sstream>

#include "../IConverter.h"

class Censorship : public IConverter {
	int startTime;
	int endTime;
	unsigned char indexWawFile;
	bool checkTime(int curSec);
public:
	void initParams(std::string params) override;
	void change(short mainInputFile[44100], ReturnerSamples& returnerSemples, int curSec) override;
	void info() override;
};
