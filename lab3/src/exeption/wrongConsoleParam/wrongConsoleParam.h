#pragma once

#include <exception>
#include <string>

class WrongConsoleParam : std::exception {
	std::string err;
public:
	WrongConsoleParam(std::string err);
	const char* what() const noexcept override;
	const int returnCode() const noexcept;
};