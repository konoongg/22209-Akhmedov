#pragma once

#include "../createConverter.h"
#include <memory>

class CreateMute : public CreateConverter {
public:
	std::shared_ptr<IConverter> create() override;
};