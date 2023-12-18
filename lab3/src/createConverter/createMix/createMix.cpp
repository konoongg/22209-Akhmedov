#include "createMix.h"
#include "../../iConverter/mix/mix.h"
#include <memory>

std::shared_ptr<IConverter> CreateMix::create() {
	return std::make_shared<Mix>();
}