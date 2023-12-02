#pragma once

#include <vector>
#include <string>
#include <map>
#include <memory>
#include "../iConverter/IConverter.h"
#include "../createConverter/createConverter.h"
#include "../modeEnum.h"

class WawEditor {
	EMode mode;
	std::vector<std::string> wawFile;
	//std::vector<Operation> operations;
	std::vector<std::shared_ptr<IConverter>> converters;
	std::map<std::string, std::shared_ptr<CreateConverter>> createrConverters;
	EMode determineMode(std::string mode) const;
	void readConfig(std::string config);
	void addOperatin(std::string line);
	void initConverters();
	void printInfoConv();
public:
	WawEditor(std::vector<std::string> argc);
	std::vector<std::shared_ptr<IConverter>>& convOperations();
	std::vector<std::string>& allFile();
	EMode returnMode();
};