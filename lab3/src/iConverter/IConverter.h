#pragma once

#include "../returnerSemples/returnerSemples.h"

class IConverter {
	virtual bool checkTime(int curSec, int startTime, int endTime) = 0;
public:
	virtual void change(short mainInputFile[44100], ReturnerSamples& returnerSamples,  int curSec, int startTime, int endTIme, unsigned char indexWawFile) = 0;
	virtual void info() = 0;
};