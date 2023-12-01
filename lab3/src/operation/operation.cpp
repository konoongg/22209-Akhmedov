#include "operation.h"

Operation::Operation(std::shared_ptr<IConverter> converter, int startTime, int endTime) : converter { converter } {
	this->startTime = startTime;
	this->endTime = endTime;
	indexWawFile = 0;
}

Operation::Operation(std::shared_ptr<IConverter> converter, int startTime, unsigned char indexWawFile) : converter { converter } {
	this->startTime = startTime;
	this->indexWawFile = indexWawFile;
	endTime = 0;
}

std::shared_ptr<IConverter> Operation::returnConverter() {
	return converter;
}

int Operation::returnStartTime() {
	return startTime;
}

int Operation::returnEndTime() {
	return endTime;
}

unsigned char Operation::returnIndexWawFile() {
	return indexWawFile;
}