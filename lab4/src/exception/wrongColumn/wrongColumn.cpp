#include "wrongColumn.h"

WrongColumn::WrongColumn(std::string err) {
	this->err = err;
}

const char* WrongColumn::what() const noexcept {
	return err.data();
}

const int WrongColumn::returnCode() const noexcept {
	return 3;
}
