#include <fstream>
#include <vector>

#include "applyConverters.h"
#include "../wawFileHeader.h"
#include "../parserInputWawFile/parserInputWawFile.h"
#include "../editor/wawEditor.h"
#include "../exeption/faildOpenFile/faildOpenFile.h"

ApplyConverters::ApplyConverters(std::vector<std::string>& wawFiles, std::vector<std::shared_ptr<IConverter>>& converters) : returnerSamples{ wawFiles } {
    writeOutFile = WriteOutFile{};
    try {
        ParserInputWawFile parserInputWawFile{};
    }
    catch (FaildOpenFile& err) {
        throw err;
    }
    ParserInputWawFile parserInputWawFile{};
	TWawFileHeader header = parserInputWawFile.returnHeader(wawFiles[1]);
    try {
        writeOutFile.writeHeader(wawFiles[0], header);
        applyOperations(wawFiles, converters);
    }
    catch (FaildOpenFile& err) {
        throw err;
    }
}

void ApplyConverters::applyOperations(std::vector<std::string>& wawFiles, std::vector<std::shared_ptr<IConverter>>& converters) {
    int curSec = 0;
    std::ifstream mainInputFile(wawFiles[1], std::ios::binary);
    short secMainInputFile[44100];
    while (!mainInputFile.eof()) {
        mainInputFile.read(reinterpret_cast<char*>(secMainInputFile), sizeof(secMainInputFile));
        for (auto converter : converters) {
            try {
                converter->change(secMainInputFile, returnerSamples, curSec);
            }
            catch (FaildOpenFile& err) {
                throw err;
            }
        }
        writeOutFile.writeToOutFile(secMainInputFile, wawFiles[0]);
        ++curSec;
    }
}

