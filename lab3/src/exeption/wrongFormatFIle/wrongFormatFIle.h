#include <exception>
#include <string>

class WrongFormatFile : std::exception {
	std::string err;
public:
	WrongFormatFile(std::string err);
	const char* what() const noexcept override;
	const int returnCode() const noexcept;
};