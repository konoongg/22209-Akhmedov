#include "CircularBuffer.h"
#include <vector>
#include <algorithm>
#include <stdexcept>

CircularBuffer::CircularBuffer() { 
	buffer = nullptr;
	frontIndex = 0;
	bufferSize = 0;
	bufferFreeSize = 0;
}

CircularBuffer::~CircularBuffer() {
	delete[] buffer;
}

CircularBuffer::CircularBuffer(const CircularBuffer& cb) {
	frontIndex = cb.startIndex();
	bufferSize = cb.capacity();
	bufferFreeSize = cb.bufferFreeSize;
	buffer = new value_type[bufferSize];
	for (int i = 0; i < cb.capacity(); ++i) {
		buffer[i] = cb[i];
	}
}

CircularBuffer::CircularBuffer(int capacity) {
	frontIndex = 0;
	bufferSize = capacity;
	bufferFreeSize = capacity;
	buffer = new value_type[bufferSize]{};
}

CircularBuffer::CircularBuffer(int capacity, const value_type& elem) {
	frontIndex = 0;
	bufferSize = capacity;
	bufferFreeSize = 0;
	buffer = new value_type[bufferSize];
	for (int i = 0; i < bufferSize; i++) {
		buffer[i] = elem;
	}
}

value_type& CircularBuffer::operator[](int i) {
	value_type& elem = buffer[(frontIndex + i) % bufferSize];
	return elem;
}

const value_type& CircularBuffer::operator[](int i) const {
	const value_type& elem = buffer[(frontIndex + i) % bufferSize];
	return elem;
}

value_type& CircularBuffer::at(int i) {
	if (i < 0 || i > bufferSize - 1) {
		throw std::out_of_range("out of range");
	}
	value_type& elem = buffer[(frontIndex + i) % bufferSize];
	return elem;
}

const value_type& CircularBuffer::at(int i) const {
	if (i < 0 || i > bufferSize - 1) {
		throw std::out_of_range("out of range");
	}
	const value_type& elem = buffer[(frontIndex + i) % bufferSize];
	return elem;
}

value_type& CircularBuffer::front() {
	value_type& elem = buffer[frontIndex];
	return elem;
}

value_type& CircularBuffer::back() {
	int bufferRealSize = bufferSize - bufferFreeSize;
	value_type& elem = buffer[(frontIndex + bufferRealSize - 1) % bufferSize];
	return elem;
}

const value_type& CircularBuffer::front() const {
	const value_type& elem = buffer[frontIndex];
	return elem;
}

const value_type& CircularBuffer::back() const {
	int bufferRealSize = bufferSize - bufferFreeSize;
	const value_type& elem = buffer[(frontIndex + bufferRealSize - 1) % bufferSize];
	return elem;
}

void CircularBuffer::linearize() {
	if (isLinearized()) {
		return;
	}
	std::rotate(buffer, buffer + frontIndex, buffer + bufferSize);
	frontIndex = 0;
}

bool CircularBuffer::isLinearized() const {
	return (frontIndex == 0);
}

void CircularBuffer::rotate(int new_begin) {
	if (!(full())) {
		throw std::runtime_error("not full buffer");
	}
	if (new_begin < 0 || new_begin >= bufferSize) {
		throw std::invalid_argument("new_begin > 0 and new_begin < bufferSize");
	}
	std::rotate(buffer, buffer + (frontIndex + new_begin) % bufferSize, buffer + bufferSize);
	frontIndex += new_begin;
	frontIndex %= bufferSize;
}

int CircularBuffer::size() const {
	return (bufferSize - bufferFreeSize);
}

bool CircularBuffer::empty() const {
	return (bufferSize == bufferFreeSize);
}

bool CircularBuffer::full() const {
	return (size() == bufferSize && capacity() != 0);
}

int CircularBuffer::reserve() const {
	return bufferFreeSize;
}

int CircularBuffer::capacity() const {
	return bufferSize;
}

int CircularBuffer::startIndex() const {
	return frontIndex;
}

void CircularBuffer::setCapacity(int newCapacity) {
	linearize();
	value_type* newBuffer = new value_type[newCapacity];
	int minCapacity = capacity();
	if (minCapacity > newCapacity) {
		minCapacity = newCapacity;
	}
	for (int i = 0; i < minCapacity; ++i) {
		newBuffer[i] = buffer[i];
	}
	if (newCapacity > bufferSize) {
		bufferFreeSize += (newCapacity - bufferSize);
		for (int i = bufferSize; i < newCapacity; ++i) {
			newBuffer[i] = 0;
		}
	}
	delete[] buffer;
	buffer = newBuffer;
	bufferSize = newCapacity;
}

void CircularBuffer::resize(int newSize, const value_type& item) {
	linearize();
	value_type* newBuffer = new value_type[newSize];
	int minCapacity = size();
	if (minCapacity > newSize) {
		minCapacity = newSize;
	}
	for (int i = 0; i < minCapacity; ++i) {
		newBuffer[i] = buffer[i];
	}
	if (newSize > capacity()) {
		for (int i = 0; i < (newSize - capacity()); ++i) {
			newBuffer[i + minCapacity] = item;
		}
		for (int i = (newSize - capacity() + minCapacity); i < newSize; ++i) {
			newBuffer[i] = 0;
		}
	}
	delete[] buffer;
	buffer = newBuffer;
	bufferSize = newSize;
}

CircularBuffer& CircularBuffer::operator=(const CircularBuffer& cb) {
	if (cb.capacity() != bufferSize) {
		setCapacity(cb.capacity());
	}
	bufferFreeSize = cb.reserve();
	frontIndex = cb.startIndex();
	for (int i = 0; i < cb.capacity(); ++i) {
		buffer[i] = cb[i];
	}
	return *(this);
}
void CircularBuffer::swap(CircularBuffer& cb) {
	if (cb.bufferSize != bufferSize) {
		throw std::out_of_range("size1 != size2");
	}
	for (int i = 0; i < bufferSize; ++i) {
		value_type swapValue = cb[i];
		cb[i] = buffer[i];
		buffer[i] = swapValue;
	}
}

void CircularBuffer::pushBack(const value_type& item) {
	if (full()) {
		buffer[frontIndex] = item;
		frontIndex++;
		frontIndex %= bufferSize;
	}
	else {
		int bufferRealSize = size();
		buffer[bufferRealSize] = item;
		bufferFreeSize -= 1;
	}
}

void CircularBuffer::swapInBuffer(int indexFirst, int indexSecond) {
	value_type swapValue = buffer[indexFirst];
	buffer[indexFirst] = buffer[indexSecond];
	buffer[indexSecond] = swapValue;
}

void CircularBuffer::pushFront(const value_type& item) {
	if (full()) {
		buffer[(frontIndex + bufferSize - 1) % bufferSize] = item;
		frontIndex = (frontIndex + bufferSize - 1) % bufferSize;
	}
	else {
		buffer[(frontIndex + bufferSize - 1) % bufferSize] = item;
		bufferFreeSize -= 1;
		int bufferRealSize = size();
		for (int i = 0; i < bufferRealSize; ++i) {
			swapInBuffer(i, frontIndex + bufferSize - 1);
		}
	}
}

void CircularBuffer::popBack() {
	if (!isLinearized()) {
		linearize();
	}
	buffer[frontIndex + bufferSize - 1] = 0;
	bufferFreeSize++;
}

void CircularBuffer::popFront() {
	buffer[frontIndex] = 0;
	bufferFreeSize++;
	frontIndex += 1;
	frontIndex %= bufferSize;
	if (!isLinearized()) {
		linearize();
	}
}


void CircularBuffer::insert(int pos, const value_type& item) {
	buffer[(frontIndex + pos) % bufferSize] = item;
}

void CircularBuffer::erase(int first, int last) {
	if (first < 0 || last > bufferSize) {
		throw std::out_of_range("out of range");
	}
	int bufferRealSize = size();
	if (!isLinearized()) {
		linearize();
	}
	int otherElem = bufferRealSize - 1 - last;
	for (int i = 0; i < last - first; ++i) {
		buffer[first + i] = 0;
	}
	if (last != bufferRealSize ) {
		int shiftSize = bufferRealSize - last;
		for (int i = 0; i < shiftSize; ++i) {
			buffer[first + i] = buffer[last + i];
			buffer[last + i] = 0;
		}
	}
	bufferFreeSize += last - first;
}

void CircularBuffer::clear() {
	for (int i = 0; i < bufferSize; ++i) {
		buffer[i] = 0;
	}
	bufferFreeSize = bufferSize;
	frontIndex = 0;
}


bool operator==(const CircularBuffer& a, const CircularBuffer& b) {
	if (a.size() != b.size()) {
		return false;
	}
	if (a.startIndex() != b.startIndex()) {
		return false;
	}
	if (a.capacity() != b.capacity()) {
		return false;
	}
	for (int i = 0; i < a.size(); ++i) {
		if (a[i] != b[i]) {
			return false;
		}
	}
	return true;
}

bool operator!=(const CircularBuffer& a, const CircularBuffer& b) {
	return !(a == b);
}