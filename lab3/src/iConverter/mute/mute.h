#pragma once

#include <string>

#include "../IConverter.h"

class Mute : public IConverter {
	int startTime;
	int endTime;
	unsigned char indexWawFile;
	bool checkTime(int curSec);
public:
	void initParams(std::string params) override;
	void change(short semplesOnOneSecByMainInputFile[44100], ReturnerSamples& returnerSamples, int curSec) override;
	void info() override;
};