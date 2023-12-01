#include <fstream>
#include <vector>

#include "applyConverters.h"
#include "../wawFileHeader.h"
#include "../parserInputWawFile/parserInputWawFile.h"
#include "../editor/wawEditor.h"
#include "../exeption/faildOpenFile/faildOpenFile.h"

ApplyConverters::ApplyConverters(std::vector<std::string>& wawFiles, std::vector<Operation>& operations) : returnerSamples{ wawFiles } {
    try {
        ParserInputWawFile parserInputWawFile{};
    }
    catch (FaildOpenFile& err) {
        throw err;
    }
    ParserInputWawFile parserInputWawFile{};
	TWawFileHeader header = parserInputWawFile.returnHeader(wawFiles[1]);
    try {
        writeHeader(wawFiles[0], header);
        applyOperations(wawFiles, operations);
    }
    catch (FaildOpenFile& err) {
        throw err;
    }
}

void ApplyConverters::writeHeader(std::string outFile, TWawFileHeader& header) {
    std::ofstream outputFile(outFile, std::ios::binary);
    if (outputFile.is_open()) {
        outputFile.write(reinterpret_cast<char*>(&header.chunkId), 4);
        outputFile.write(reinterpret_cast<char*>(&header.chunkSize), 4);
        outputFile.write(reinterpret_cast<char*>(&header.format), 4);
        outputFile.write(reinterpret_cast<char*>(&header.subchunk1Id), 4);
        outputFile.write(reinterpret_cast<char*>(&header.subchunk1Size), 4);
        outputFile.write(reinterpret_cast<char*>(&header.audioFormat), 2);
        outputFile.write(reinterpret_cast<char*>(&header.numChannels), 2);
        outputFile.write(reinterpret_cast<char*>(&header.sampleRate), 4);
        outputFile.write(reinterpret_cast<char*>(&header.byteRate), 4);
        outputFile.write(reinterpret_cast<char*>(&header.blockAlign), 2);
        outputFile.write(reinterpret_cast<char*>(&header.bitsPerSample), 2);
        outputFile.write(reinterpret_cast<char*>(&header.subchunk2Id), 4);
        outputFile.write(reinterpret_cast<char*>(&header.subchunk2Size), 4);
        outputFile.close();
    }
    else {
        throw FaildOpenFile{ "can't to open the file " + outFile };
    }
}

void ApplyConverters::applyOperations(std::vector<std::string>& wawFiles, std::vector<Operation>& operations) {
    int curSec = 0;
    std::ifstream mainInputFile(wawFiles[1], std::ios::binary);
    short semplesOnOneSecByMainInputFile[44100];
    while (!mainInputFile.eof()) {
        mainInputFile.read(reinterpret_cast<char*>(semplesOnOneSecByMainInputFile), sizeof(semplesOnOneSecByMainInputFile));
        for (auto operation : operations) {
            std::shared_ptr<IConverter> converter = operation.returnConverter();
            int startTime = operation.returnStartTime();
            int endTime = operation.returnEndTime();
            unsigned char indexWawFile = operation.returnIndexWawFile();
            try {
                converter->change(semplesOnOneSecByMainInputFile, returnerSamples, curSec, startTime, endTime, indexWawFile);
            }
            catch (FaildOpenFile& err) {
                throw err;
            }
        }
        writeToOutFIle(semplesOnOneSecByMainInputFile, wawFiles[0]);
        ++curSec;
    }
}

void ApplyConverters::writeToOutFIle(short semplesOnOneSecByMainInputFile[44100], std::string outWawFile) {
    std::ofstream file(outWawFile, std::ios::binary | std::ios::app);
    if (file.is_open()) {
        for (int i = 0; i < 44100; ++i) {
            file.write(reinterpret_cast<const char*>(&semplesOnOneSecByMainInputFile[i]), sizeof(short));
        }
        
    }
    else {
        throw FaildOpenFile{ "can't to open the file " + outWawFile };
    }
}
