#include "wrongFormatFIle.h"

WrongFormatFile::WrongFormatFile(std::string err) {
	this->err = err;
}

const char* WrongFormatFile::what() const noexcept {
	return err.data();
}

const int WrongFormatFile::returnCode() const noexcept {
	return 4;
}