#include <iostream>
#include <sstream>

#include "mix.h"
#include "../../exeption/faildOpenFile/faildOpenFile.h"
#include "../../exeption/wrongConfig/wrongConfig.h"

void Mix::initParams(std::string params) {
	std::stringstream ss(params);
	std::string mode;
	std::string param;
	ss >> mode;
	ss >> param;
	if (param == "") {
		throw WrongConfig{ "wrong argument, so less params" };
	}
	if (!(param.find("$") != std::string::npos)) {
		throw WrongConfig{ "wrong argument, you need to use $ with index wav file " };
	}
	indexWawFile = static_cast<unsigned char>(std::stod(param.substr(1)));
	ss >> param;
	if (param == "") {
		throw WrongConfig{ "wrong argument, so less params" };
	}
	startTime = static_cast<int>(std::stod(param));
	if (startTime < 0) {
		throw WrongConfig{ "you need to startTime be > 0" };
	}
}

void Mix::change(short mainInputFile[44100], ReturnerSamples& returnerSamples,  int curSec) {
	if (checkTime(curSec)) {
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

bool Mix::checkTime(int curSec) {
	if (curSec >= startTime) {
		return true;
	}
	return false;
}
