#include "mix.h"
#include <iostream>

#include "../../exeption/faildOpenFile/faildOpenFile.h"

void Mix::change(short mainInputFile[44100], ReturnerSamples& returnerSamples,  int curSec, int startTime, int endTime, unsigned char indexWawFile) {
	if (checkTime(curSec, startTime, endTime)) {
		short secondInputFile[44100];
		try {
			returnerSamples.returnSamples(secondInputFile, indexWawFile, curSec);
		}
		catch (FaildOpenFile& err) {
			throw err;
		}
		for (int i = 0; i < 44100; ++i) {
			mainInputFile[i] /= 2;
			secondInputFile[i] /= 2;
			mainInputFile[i] += secondInputFile[i];
		}
	}
}

void Mix::info() {
	std::cout << "mix converter. [mix ${number input file} {startTime}]. The sound from main input file mix with another file from areas startTime(seconds)  " << std::endl;
}

bool Mix::checkTime(int curSec, int startTime, int endTime) {
	if (curSec >= startTime) {
		return true;
	}
	return false;
}