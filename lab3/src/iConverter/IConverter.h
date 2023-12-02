#pragma once

#include "../returnerSemples/returnerSemples.h"

class IConverter {
	int startTime;
	int endTime;
	unsigned char indexWawFile;
	virtual bool checkTime(int curSec) = 0;
public:
	virtual void initParams(std::string params) = 0;
	virtual void change(short mainInputFile[44100], ReturnerSamples& returnerSamples,  int curSec) = 0;
	virtual void info() = 0;
};