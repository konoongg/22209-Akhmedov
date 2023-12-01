#pragma once

#include <memory>
#include "../iConverter/IConverter.h"

class CreateConverter {
public:
	virtual std::shared_ptr<IConverter>  create() = 0;
};