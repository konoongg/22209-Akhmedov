#include <fstream>

#include "writeOutFile.h"
#include "../exeption/faildOpenFile/faildOpenFile.h"

void WriteOutFile::writeToOutFile(short semplesOnOneSecByMainInputFile[44100], std::string outWawFile) {
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

void WriteOutFile::writeHeader(std::string outFile, TWawFileHeader& header) {
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