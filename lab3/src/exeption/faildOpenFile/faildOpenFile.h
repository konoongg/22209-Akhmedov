#include <exception>
#include <string>

class FaildOpenFile : std::exception {
	std::string err;
public:
	FaildOpenFile(std::string err);
	const char* what() const noexcept override;
	const int returnCode() const noexcept;
};