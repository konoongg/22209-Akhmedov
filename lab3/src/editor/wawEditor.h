#pragma once

#include <vector>
#include <string>
#include <map>
#include <memory>
#include "../operation/operation.h"
#include "../iConverter/IConverter.h"

class WawEditor {
	char mode;
	std::vector<std::string> wawFile;
	std::vector<Operation> operations;
	std::map<std::string, std::shared_ptr<IConverter>> converters;
	const char determineMode(std::string mode) const;
	void readConfig(std::string config);
	void addOperatin(std::string line);
	void initConverters();
public:
	WawEditor(std::vector<std::string> argc);
	std::vector<Operation>& convOperations();
	std::vector<std::string>& allFile();
	char returnMode();
};