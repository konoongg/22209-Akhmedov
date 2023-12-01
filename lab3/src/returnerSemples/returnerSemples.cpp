#include "returnerSemples.h"
#include "../parserInputWawFile/parserInputWawFile.h"
#include "../exeption/faildOpenFile/faildOpenFile.h"

ReturnerSamples::ReturnerSamples(std::vector<std::string>& wawFiles) {
	sampleRate = 44100;
	blockAlign = 2;
	for (int i = 0; i < wawFiles.size(); ++i) {
		indexWawFiles[i] = wawFiles[i];
	}
}

void ReturnerSamples::returnSamples(short semplesOnOneSecSecondInputFile[44100], unsigned char indexWawFile, int curSec) {
	try {
		checkOpening(indexWawFile);
	}
	catch (FaildOpenFile& err) {
		throw err;
	}
	openFiles[indexWawFile].seekg(dataIndex[indexWawFile] + curSec * sampleRate * blockAlign);
	openFiles[indexWawFile].read(reinterpret_cast<char*>(semplesOnOneSecSecondInputFile), 44100 * sizeof(short));
}


void ReturnerSamples::checkOpening(unsigned char indexWawFile) {
	if (!(openFiles.find(indexWawFile) != openFiles.end())) {
		try {
			openFile(indexWawFile);
		}
		catch (FaildOpenFile& err) {
			throw err;
		}
	}
}


void ReturnerSamples::openFile(unsigned char indexWawFile) {
	ParserInputWawFile parserInputWawFile{};
	std::ifstream file(indexWawFiles[indexWawFile], std::ios::binary);
	if (file.is_open()) {
		dataIndex[indexWawFile] = defineFirstSample(parserInputWawFile.defineDate(file)) ;
		openFiles.emplace(indexWawFile, std::move(file));
	}
	else {
		throw FaildOpenFile{ "can't to open the file " + indexWawFiles[indexWawFile] };
	}
}

int ReturnerSamples::defineFirstSample(int lastDataByte) {
	return lastDataByte + 5;
}