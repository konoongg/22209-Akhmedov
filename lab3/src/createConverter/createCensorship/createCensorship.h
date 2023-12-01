#pragma once

#include "../createConverter.h"
#include <memory>

class CreateCensorship : public CreateConverter {
public:
	std::shared_ptr<IConverter> create() override;
};