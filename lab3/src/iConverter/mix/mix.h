#pragma once

#include "../IConverter.h"

class Mix : public IConverter {
	bool checkTime(int curSec, int startTime, int endTime);
public:
	void change(short mainInputFile[44100], ReturnerSamples& returnerSamples, int curSec, int startTime, int endTime, unsigned char indexWawFile) override;
	void info() override;
};