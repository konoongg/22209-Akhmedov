#include "faildOpenFile.h"

FaildOpenFile::FaildOpenFile(std::string err) {
	this->err = err;
}

const char* FaildOpenFile::what() const noexcept {
	return err.data();
}

const int FaildOpenFile::returnCode() const noexcept {
	return 3;
}