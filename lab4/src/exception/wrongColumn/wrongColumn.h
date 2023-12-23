#pragma once

#include <exception>
#include <string>

class WrongColumn : std::exception {
	std::string err;
public:
	WrongColumn(std::string err);
	const char* what() const noexcept override;
	const int returnCode() const noexcept;
};