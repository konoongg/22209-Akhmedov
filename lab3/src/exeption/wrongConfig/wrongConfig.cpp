#include "wrongConfig.h"

WrongConfig::WrongConfig(std::string err) {
	this->err = err;
}

const char* WrongConfig::what() const noexcept{
	return err.data();
}

const int WrongConfig::returnCode() const noexcept {
	return 1;
}