#pragma once

#include <exception>
#include <string>

class WrongConfig : std::exception  {
	std::string err;
public:
	WrongConfig(std::string err);
	const char* what() const noexcept override;
	const int returnCode() const noexcept;
};