#define _CRT_SECURE_NO_WARNINGS 
#include <fstream>
#include <vector>

#include "parserInputWawFile.h"
#include "../exeption/faildOpenFile/faildOpenFile.h"

 TWawFileHeader ParserInputWawFile::returnHeader(std::string inputWawFile)  {
	TWawFileHeader header;
	fillConstantFields(header);
	try {
		fillFromInputFileFields(inputWawFile, header);
	}
	catch (FaildOpenFile& err) {
		throw err;
	}
	defineChunkSize(header);
	defineByteRate(header);
	return header;
}

void ParserInputWawFile::fillConstantFields(TWawFileHeader& header) {
	strcpy(header.chunkId, "RIFF");
	strcpy(header.format, "WAVE");
	strcpy(header.subchunk1Id, "fmt ");
	strcpy(header.subchunk2Id, "data");
	header.subchunk1Size = 16;
	header.numChannels = 1;
	header.blockAlign = 2;
	header.sampleRate = 44100;
	header.bitsPerSample = 16;
	header.audioFormat = 1;
}

void ParserInputWawFile::fillFromInputFileFields(std::string inputWawFile, TWawFileHeader& header) {
	std::ifstream file(inputWawFile, std::ios::binary);
	if (!file) {
		throw FaildOpenFile{ "can't to open the file " + inputWawFile };
	}
	int headerIndex = defineDate(file);
	char sizeData[4];
	file.read(sizeData, 4);
	defineSubchunk2Size(sizeData, header);
}

int ParserInputWawFile::defineDate(std::ifstream& file) {
	int DateIndex = 3;
	char subArray1[2];
	char subArray2[2];
	file.read(subArray1, 2);
	file.read(subArray2, 2);
	while (!(subArray1[0] == 'd' && subArray1[1] == 'a' && subArray2[0] == 't' && subArray2[1] == 'a')) {
		std::copy(subArray2, subArray2 + 2, subArray1);
		file.read(subArray2, 2);
		DateIndex += 2;
	}
	return DateIndex;
}

void ParserInputWawFile::defineSubchunk2Size(char* subchunk2Size, TWawFileHeader& header) {
	header.subchunk2Size = fromCharArrayToInt(subchunk2Size);
}

void ParserInputWawFile::defineChunkSize(TWawFileHeader& header) {
	header.chunkSize = header.subchunk2Size + 36;
}

void ParserInputWawFile::defineByteRate(TWawFileHeader& header) {
	header.byteRate = header.sampleRate * header.numChannels * header.bitsPerSample / 8;
}

unsigned long ParserInputWawFile::fromCharArrayToInt( char array[4]) {
	unsigned long result = 0;   
	for (int i = 3; i >= 0 ; --i) {
		result = (result << 8) | (array[i] & 0xFF);
	}
	return result;
}