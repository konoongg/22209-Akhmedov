#pragma once

#include <string>
#include <map>

#include "../wawFileHeader.h"
#include "../editor/wawEditor.h"
#include "../returnerSemples/returnerSemples.h"
#include "../writeOutFile/writeOutFile.h"

class ApplyConverters {
	WriteOutFile writeOutFile;
	ReturnerSamples returnerSamples;
	void applyOperations(std::vector<std::string>& wawFiles, std::vector<std::shared_ptr<IConverter>>& converters);
public:
	ApplyConverters(std::vector<std::string>& wawFiles, std::vector<std::shared_ptr<IConverter>>& converters);
};