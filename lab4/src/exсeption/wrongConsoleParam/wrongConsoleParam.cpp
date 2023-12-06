#include "wrongConsoleParam.h"

WrongConsoleParam::WrongConsoleParam(std::string err) {
	this->err = err;
}

const char* WrongConsoleParam::what() const noexcept {
	return err.data();
}

const int WrongConsoleParam::returnCode() const noexcept {
	return 3;
}