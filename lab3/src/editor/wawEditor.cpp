#include <fstream>
#include <iostream>
#include <sstream>
#include <fstream>

#include "wawEditor.h"
#include "../operation/operation.h"
#include "../iConverter/iConverter.h"
#include "../createConverter/createConverter.h"
#include "../createConverter/createMute/createMute.h"
#include "../createConverter/createMix/createMix.h"
#include "../createConverter/createCensorship/createCensorship.h"
#include "../exeption/wrongConfig/wrongConfig.h"
#include "../exeption/wrongConsoleParam/wrongConsoleParam.h"
#include "../exeption/faildOpenFile/faildOpenFile.h"
#include "../exeption/wrongFormatFIle/wrongFormatFIle.h"

WawEditor::WawEditor(std::vector<std::string> argc) {
	initConverters();
	try {
		mode = determineMode(argc[1]);
	}
	catch (FaildOpenFile& err) {
		throw err;
	}
	if (mode == 'c') {
		std::string config = argc[2];
		for (int i = 3; i < argc.size(); ++i) {
			if ( !(argc[i].find("wav") != std::string::npos)) {
				throw WrongFormatFile{"it's not wav file: " + argc[i]};
			}
			wawFile.push_back(argc[i]);
		}
		try {
			readConfig(config);
		}
		catch (FaildOpenFile& err) {
			throw err;
		}
		catch (WrongConfig& err) {
			throw err;
		}
	}
	else if (mode == 'h') {
		for (auto conv: converters) {
			conv.second->info();
		}
	}
	
}

const char WawEditor::determineMode(std::string mode) const {
	if (mode == "-c") {
		return 'c';
	}
	else if (mode == "-h") {
		return 'h';
	}
	else {
		throw WrongConsoleParam { "wrong mode" };
	}
	return 0;
}

void WawEditor::readConfig(std::string config) {
	std::ifstream file(config);
	if (file.is_open()) {
		std::string line;
		while (std::getline(file, line)) { 
			try {
				addOperatin(line);
			}
			catch(WrongConfig& err) {
				throw err;
			}
		}
		file.close(); 
	}
	else {
		throw FaildOpenFile {"can't to open the file " + config};
	}
}

void WawEditor::initConverters() {
	std::shared_ptr<CreateConverter> creater = std::make_shared<CreateMute>();
	converters["mute"] = creater->create();
	creater = std::make_shared<CreateMix>();
	converters["mix"] = creater->create();
	creater = std::make_shared<CreateCensorship>();
	converters["censorship"] = creater->create();
}

void WawEditor::addOperatin(std::string line) {
	std::stringstream ss(line);
	std::string mode;
	std::string param;
	ss >> mode;
	if (mode == "") {
		throw WrongConfig{ "no converter" };
	}
	if (mode == "mute") {
		ss >> param;
		if (param == "") {
			throw WrongConfig{ "wrong argument, so less params" };
		}
		int startTime = (int)std::stod(param);
		ss >> param;
		if (param == "") {
			throw WrongConfig{ "wrong argument, so less params" };
		}
		int endTime = (int)std::stod(param);
		if (startTime > endTime) {
			throw WrongConfig{ "startTime can't be more than endTime" };
		}
		if (startTime < 0 || endTime < 0) {
			throw WrongConfig{ "you nedd to startTime be > 0 and endTime be > 0" };
		}
		operations.push_back(Operation{ converters["mute"], startTime, endTime });
	}
	else if (mode == "mix") {
		ss >> param;
		if (param == "") {
			throw WrongConfig{ "wrong argument, so less params" };
		}
		if (!(param.find("$") != std::string::npos)) {
			throw WrongConfig{ "wrong argument, you need to use $ with index wav file " };
		}
		unsigned char index = (unsigned char)std::stod(param.substr(1));
		ss >> param;
		if (param == "") {
			throw WrongConfig{ "wrong argument, so less params" };
		}
		int startTime = (int)std::stod(param);
		if (startTime < 0 ) {
			throw WrongConfig{ "you need to startTime be > 0" };
		}
		operations.push_back(Operation{ converters["mix"], startTime, index });
	}
	
	else if (mode == "censorship") {
		ss >> param;
		if (param =="") {
			throw WrongConfig{ "wrong argument, so less params" };
		}
		int startTime = (int)std::stod(param);
		ss >> param;
		if (param == "") {
			throw WrongConfig{ "wrong argument, so less params" };
		}
		int endTime = (int)std::stod(param);
		if (startTime > endTime) {
			throw WrongConfig{ "startTime can't be more than endTime" };
		}
		if (startTime < 0 || endTime < 0) {
			throw WrongConfig{ "you need to startTime be > 0 and endTime be > 0" };
		}
		operations.push_back(Operation{ converters["censorship"], startTime, endTime });
	}
	else {
		throw WrongConfig{"wrong converter " + mode};
	}
}

std::vector<Operation>& WawEditor::convOperations()  {
	return operations;
}

std::vector<std::string>& WawEditor::allFile()  {
	return wawFile;
}

char WawEditor::returnMode() {
	return mode;
}