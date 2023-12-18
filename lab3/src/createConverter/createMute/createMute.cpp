#include "createMute.h"
#include "../../iConverter/mute/mute.h"
#include <memory>

std::shared_ptr<IConverter> CreateMute::create() {
	return std::make_shared<Mute>();
}