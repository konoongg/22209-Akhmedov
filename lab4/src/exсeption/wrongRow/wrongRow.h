#pragma once

#include <exception>
#include <string>

class WrongRow : std::exception {
	std::string err;
public:
	WrongRow(std::string err);
	const char* what() const noexcept override;
	const int returnCode() const noexcept;
};