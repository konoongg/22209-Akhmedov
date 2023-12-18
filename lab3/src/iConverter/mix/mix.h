#pragma once

#include "../IConverter.h"

class Mix : public IConverter {
	int startTime;
	int endTime;
	unsigned char indexWawFile;
	bool checkTime(int curSec);
public:
	void initParams(std::string params) override;
	void change(short mainInputFile[44100], ReturnerSamples& returnerSamples, int curSec) override;
	void info() override;
};