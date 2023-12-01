#pragma once

#include "../iConverter/IConverter.h"
#include <memory>

class Operation {
	std::shared_ptr<IConverter> converter;
	int startTime;
	int endTime;
	unsigned char indexWawFile;
public:
	Operation(std::shared_ptr<IConverter> converter, int startTime, int endTime);
	Operation(std::shared_ptr<IConverter> converter, int startTime, unsigned char indexWawFile);
	std::shared_ptr<IConverter> returnConverter();
	int returnStartTime();
	int returnEndTime();
	unsigned char returnIndexWawFile();
};