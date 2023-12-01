#include "censorship.h"
#include <iostream>

void Censorship::change(short mainInputFile[44100], ReturnerSamples& returnerSamples, int curSec, int startTime, int endTime, unsigned char indexWawFile) {
	if (checkTime(curSec, startTime, endTime)) {
		for (int i = 0; i < 44100; ++i) {
			mainInputFile[i] *= 10000;
		}
	}
}

void Censorship::info() {
	std::cout << "censorship converter. [censorship {startTime} {endTime}]. The sound is replaced by noise from areas startTime(seconds) to endTime(seconds) " << std::endl;
}

bool Censorship::checkTime(int curSec, int startTime, int endTime) {
	if (curSec >= startTime && curSec <= endTime) {
		return true;
	}
	return false;
}