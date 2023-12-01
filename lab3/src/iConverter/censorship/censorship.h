#pragma once

#include "../IConverter.h"

class Censorship : public IConverter {
	bool checkTime(int curSec, int startTime, int endTime);
public:
	void change(short mainInputFile[44100], ReturnerSamples& returnerSemples, int curSec, int startTime, int endTime, unsigned char indexWawFile) override;
	void info() override;
};