#include "censorship.h"
#include <iostream>
#include <sstream>

#include "../../exeption/wrongConfig/wrongConfig.h"

void Censorship::initParams(std::string params) {
	std::stringstream ss(params);
	std::string mode;
	std::string param;
	ss >> mode;
	ss >> param;
	if (param == "") {
		throw WrongConfig{ "wrong argument, so less params" };
	}
	startTime = static_cast<int>(std::stod(param));
	ss >> param;
	if (param == "") {
		throw WrongConfig{ "wrong argument, so less params" };
	}
	endTime = static_cast<int>(std::stod(param));
	if (startTime > endTime) {
		throw WrongConfig{ "startTime can't be more than endTime" };
	}
	if (startTime < 0 || endTime < 0) {
		throw WrongConfig{ "you need to startTime be > 0 and endTime be > 0" };
	}
}

void Censorship::change(short mainInputFile[44100], ReturnerSamples& returnerSamples, int curSec) {
	if (checkTime(curSec)) {
		for (int i = 0; i < 44100; ++i) {
			mainInputFile[i] *= 10000;
		}
	}
}

void Censorship::info() {
	std::cout << "censorship converter. [censorship {startTime} {endTime}]. The sound is replaced by noise from areas startTime(seconds) to endTime(seconds) " << std::endl;
}

bool Censorship::checkTime(int curSec) {
	if (curSec >= startTime && curSec <= endTime) {
		return true;
	}
	return false;
}
