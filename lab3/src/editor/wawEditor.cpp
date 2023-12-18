#include <fstream>
#include <iostream>
#include <sstream>
#include <fstream>

#include "wawEditor.h"
#include "../iConverter/iConverter.h"
#include "../createConverter/createConverter.h"
#include "../createConverter/createMute/createMute.h"
#include "../createConverter/createMix/createMix.h"
#include "../createConverter/createCensorship/createCensorship.h"
#include "../exeption/wrongConfig/wrongConfig.h"
#include "../exeption/wrongConsoleParam/wrongConsoleParam.h"
#include "../exeption/faildOpenFile/faildOpenFile.h"
#include "../exeption/wrongFormatFIle/wrongFormatFIle.h"
#include "../modeEnum.h"

WawEditor::WawEditor(std::vector<std::string> argc) {
	initConverters();
	try {
		mode = determineMode(argc[1]);
	}
	catch (FaildOpenFile& err) {
		throw err;
	}
	if (mode == EMode::apllyConverter) {
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
	else if (mode == EMode::help) {
		printInfoConv();
	}
	
}
void WawEditor::printInfoConv() {
	for (auto conv : createrConverters) {
		conv.second->create()->info();
	}
}

EMode WawEditor::determineMode(std::string mode) const {
	if (mode == "-c") {
		return EMode::apllyConverter;
	}
	else if (mode == "-h") {
		return EMode::help;
	}
	else {
		throw WrongConsoleParam { "wrong mode" };
	}
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
	createrConverters["mute"] = std::make_shared<CreateMute>();
	createrConverters["mix"] = std::make_shared<CreateMix>();
	createrConverters["censorship"] = std::make_shared<CreateCensorship>();
}

void WawEditor::addOperatin(std::string line) {
	std::stringstream ss(line);
	std::string mode;
	std::string param;
	ss >> mode;
	if (mode == "") {
		throw WrongConfig{ "no converter" };
	}
	auto it = createrConverters.find(mode);
	if (it != createrConverters.end()) {
		try {
			std::shared_ptr<IConverter> converter = it->second->create();
			converter->initParams(line);
			converters.push_back(converter);
		}
		catch (WrongConfig& err) {
			throw err;
		}
	}
	else {
		throw WrongConfig{ "wrong converter " + mode };
	}
}

std::vector<std::shared_ptr<IConverter>>& WawEditor::convOperations()  {
	return converters;
}

std::vector<std::string>& WawEditor::allFile()  {
	return wawFile;
}

EMode WawEditor::returnMode() {
	return mode;
}