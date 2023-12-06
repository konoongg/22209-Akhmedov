#include "wrongRow.h"

WrongRow::WrongRow(std::string err) {
	this->err = err;
}

const char* WrongRow::what() const noexcept {
	return err.data();
}

const int WrongRow::returnCode() const noexcept {
	return 3;
}
