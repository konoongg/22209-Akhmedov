#include "mute.h"
#include <iostream>

void Mute::change(short mainInputFile[44100], ReturnerSamples& returnerSamples, int curSec, int startTime, int endTime, unsigned char indexWawFile)  {
	if (checkTime(curSec, startTime, endTime)) {
		for (int i = 0; i < 44100; ++i) {
			mainInputFile[i] = 0;
		}
	}
}

void Mute::info() {
	std::cout << "mute converter. [mute {startTime} {endTime}]. The sound is removed from areas startTime(seconds) to endTime(seconds) " << std::endl;
}

bool Mute::checkTime(int curSec, int startTime, int endTime) {
	if (curSec >= startTime && curSec <= endTime) {
		return true;
	}
	return false;
}