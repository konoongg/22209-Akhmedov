#include <iostream>
#include <vector>
#include <string>
#include <exception>

#include "editor/wawEditor.h"
#include "applyConverters/applyConverters.h"
#include "exeption/wrongConfig/wrongConfig.h"
#include "exeption/wrongConsoleParam/wrongConsoleParam.h"
#include "exeption/faildOpenFile/faildOpenFile.h"
#include "exeption/wrongFormatFIle/wrongFormatFIle.h"

int main(int argc, char* argv[]) {
	if (argc < 2) {
		std::cout << "not enough arguments\n";
		return 0;
	}
	std::vector<std::string> arguments(argv, argv + argc);
	try {
		WawEditor editor{ arguments };
	}
	catch(WrongConsoleParam& err) {
		std::cout << err.what() << std::endl;
		return err.returnCode();
	}
	catch (FaildOpenFile& err) {
		std::cout << err.what() << std::endl;
		return err.returnCode();
	}
	catch (WrongConfig& err) {
		std::cout << err.what() << std::endl;
		return err.returnCode();
	}
	catch (WrongFormatFile& err) {
		std::cout << err.what() << std::endl;
		return err.returnCode();
	}
	WawEditor editor{ arguments };
	if (editor.returnMode() == 'c') {
		try {
			ApplyConverters{ editor.allFile(), editor.convOperations() };
		}
		catch (FaildOpenFile& err) {
			std::cout << err.what() << std::endl;
			return err.returnCode();
		}
	}
	return 0;
}