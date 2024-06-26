#include "CircularBuffer.h"
#include <vector>
#include <algorithm>
#include <stdexcept>

CircularBuffer::CircularBuffer() { 
	buffer = nullptr;
	frontIndex = 0;
	backIndex = -1;
	bufferSize = 0;
	bufferFreeSize = 0;
}

CircularBuffer::~CircularBuffer() {
	delete[] buffer;
}

CircularBuffer::CircularBuffer(const CircularBuffer& cb) {
	frontIndex = cb.frontIndex;
	backIndex = cb.backIndex;
	bufferSize = cb.bufferSize;
	bufferFreeSize = cb.bufferFreeSize;
	buffer = new value_type[bufferSize];
	for (int i = 0; i < cb.bufferSize; ++i) {
		buffer[i] = cb[i];
	}
}

CircularBuffer::CircularBuffer(int capacity) {
	frontIndex = 0;
	backIndex = -1;
	bufferSize = capacity;
	bufferFreeSize = capacity;
	buffer = new value_type[bufferSize]{};
}

CircularBuffer::CircularBuffer(int capacity, const value_type& elem) {
	frontIndex = 0;
	backIndex = capacity - 1 ;
	bufferSize = capacity;
	bufferFreeSize = 0;
	buffer = new value_type[bufferSize];
	for (int i = 0; i < bufferSize; i++) {
		buffer[i] = elem;
	}
}

int CircularBuffer::bufferIndex(int i) const{
	return (frontIndex + i) % bufferSize;
}

value_type& CircularBuffer::operator[](int i) {
	return buffer[bufferIndex(i)];
}

const value_type& CircularBuffer::operator[](int i) const {
	return buffer[bufferIndex(i)];
}

value_type& CircularBuffer::at(int i) {
	if (i < 0 || i > size() - 1) {
		throw std::out_of_range("out of range");
	}
	return buffer[bufferIndex(i)];
}

const value_type& CircularBuffer::at(int i) const {
	if (i < 0 || i > size() - 1) {
		throw std::out_of_range("out of range");
	}
	return buffer[bufferIndex(i)];
}

value_type& CircularBuffer::front() {
	return buffer[frontIndex];
}

value_type& CircularBuffer::back() {
	return buffer[backIndex];
}

const value_type& CircularBuffer::front() const {
	return buffer[frontIndex];
}

const value_type& CircularBuffer::back() const {
	return buffer[backIndex];
}

void CircularBuffer::linearize() {
	if (isLinearized()) {
		return;
	}
	std::rotate(buffer, buffer + frontIndex, buffer + bufferSize);
	frontIndex = 0;
	backIndex = size() - 1;
}

bool CircularBuffer::isLinearized() const {
	return (frontIndex == 0);
}

void CircularBuffer::rotate(int newBegin) {
	if (newBegin < 0 || newBegin >= bufferSize) {
		throw std::invalid_argument("new_begin > 0 and new_begin < bufferSize");
	}
	int realNewBegin = bufferIndex(newBegin);
	if (!full()) {
		if (realNewBegin < frontIndex || realNewBegin  > backIndex) {
			throw std::out_of_range("out of range");
		}
	}
	if (!full() && frontIndex != 0) {
		linearize();
	}
	std::rotate(buffer, buffer + newBegin, buffer + bufferSize - bufferFreeSize);
	frontIndex = 0;
	backIndex = size() - 1;
}

int CircularBuffer::size() const {
	return (bufferSize - bufferFreeSize);
}

bool CircularBuffer::empty() const {
	return (bufferSize == bufferFreeSize);
}

bool CircularBuffer::full() const {
	return (size() == bufferSize);
}

int CircularBuffer::reserve() const {
	return bufferFreeSize;
}

int CircularBuffer::capacity() const {
	return bufferSize;
}

void CircularBuffer::setCapacity(int newCapacity) {
	if (newCapacity == bufferSize) {
		return;
	}
	value_type* newBuffer = new value_type[newCapacity];
	int minCapacity = std::min(capacity(), newCapacity);
	for (int i = 0; i < minCapacity; ++i) {
		newBuffer[i] = buffer[bufferIndex(i)];
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
	frontIndex = 0;
	backIndex = size() - 1;;
}

void CircularBuffer::resize(int newSize, const value_type& item) {
	if (newSize == bufferSize) {
		return;
	}
	else if (newSize < bufferSize) {
		linearize();
		linearize();
		linearize();
		for (int i = size() - 1; i > newSize - 1; --i) {
			buffer[i] =  0 ;
		}
		backIndex = newSize - 1;
		bufferFreeSize += size() - newSize;
	}
	else {
		value_type* newBuffer = new value_type[newSize];
		int minCapacity = std::min(size(), newSize);
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
		backIndex = size() - 1;
	}
}

CircularBuffer& CircularBuffer::operator=(const CircularBuffer& cb) {
	if (*(this) == cb) {
		return *(this);
	}
	if (cb.bufferSize != bufferSize){
		value_type* newBuffer = new value_type[cb.bufferSize];
		delete[] buffer;
		buffer = newBuffer;
		bufferSize = cb.bufferSize;
	}
	bufferFreeSize = cb.bufferFreeSize;
	frontIndex = 0;
	backIndex = cb.size() - 1;
	for (int i = 0; i < cb.bufferSize; ++i) {
		buffer[i] = cb[i];
	}
	return *(this);
}

void CircularBuffer::swap(CircularBuffer& cb) {
	std::swap(buffer, cb.buffer);
	std::swap(bufferSize, cb.bufferSize);
	std::swap(bufferFreeSize, cb.bufferFreeSize);
	std::swap(frontIndex, cb.frontIndex);
	std::swap(backIndex, cb.backIndex);
}

int CircularBuffer::moduloIncrease(int&	increasing, int plusNum, int module) {
	return (increasing + plusNum) % module;
}

void CircularBuffer::pushBack(const value_type& item) { 
	if (full()) {
		buffer[frontIndex] = item;
		backIndex = frontIndex;
		frontIndex = moduloIncrease(frontIndex, 1, bufferSize);
	}
	else {
		buffer[(frontIndex + size()) % bufferSize] = item;
		bufferFreeSize -= 1;
		backIndex = moduloIncrease(backIndex, 1, bufferSize);
	}
}

void CircularBuffer::pushFront(const value_type& item) {
	if (full()) {
		buffer[backIndex] = item;
		frontIndex = backIndex;
		backIndex = moduloIncrease(backIndex, bufferSize - 1, bufferSize);
	}
	else {
		if (backIndex == -1) {
			buffer[0] = item;
			backIndex = 0;
			frontIndex = 0;
		}
		else{
			buffer[bufferIndex(bufferSize - 1)] = item;
			frontIndex = moduloIncrease(frontIndex, bufferSize - 1, bufferSize);
		}
		bufferFreeSize -= 1;
	}
}

void CircularBuffer::popBack() {
	buffer[backIndex] = 0;
	backIndex = moduloIncrease(backIndex, bufferSize - 1, bufferSize);
	bufferFreeSize++;
	if (empty()) {
		backIndex = -1;
		frontIndex = 0;
	}
}

void CircularBuffer::popFront() {
	buffer[frontIndex] = 0;
	bufferFreeSize++;
	frontIndex += 1;
	frontIndex %= bufferSize;
	if (empty()) {
		backIndex = -1;
		frontIndex = 0;
	}
 }


void CircularBuffer::insert(int pos, const value_type& item) {
	if (size() == 0) {
		throw std::out_of_range("not have elem");
	}
	if (pos < 0 || pos > size() - 1) {
		throw std::out_of_range("wrong index");
	}
	buffer[bufferIndex(pos)] = item;
}

void CircularBuffer::erase(int first, int last) {
	if (first < 0 || last > bufferSize) {
		throw std::out_of_range("out of range");
	}
	for (int i = first; i < last; ++i) {
		buffer[(i + frontIndex) % bufferSize] = 0;
	}
	if (first == 0) {
		frontIndex += last;
		frontIndex %= bufferSize;
	}
	else {
		int otherElem = size() - last;
		for (int i = 0; i < otherElem; ++i) {
			buffer[(first + i + frontIndex) % bufferSize] = buffer[(last + i + frontIndex) % bufferSize];
			buffer[(last + i + frontIndex) % bufferSize] = 0;
		}
		backIndex = moduloIncrease(backIndex, bufferSize - (last - first), bufferSize);
	}
	bufferFreeSize += last - first;
}

void CircularBuffer::clear() {
	for (int i = 0; i < bufferSize; ++i) {
		buffer[i] = 0;
	}
	bufferFreeSize = bufferSize;
	frontIndex = 0;
	backIndex = -1;
}


bool operator==(const CircularBuffer& a, const CircularBuffer& b) {
	if (a.size() != b.size()||
		a.capacity() != b.capacity()){
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