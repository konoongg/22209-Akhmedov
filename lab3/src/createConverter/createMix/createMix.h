#pragma once

#include "../createConverter.h"
#include <memory>

class CreateMix : public CreateConverter {
public:
	std::shared_ptr<IConverter> create() override;
};