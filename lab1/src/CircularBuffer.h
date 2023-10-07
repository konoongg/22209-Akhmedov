#pragma once

typedef char value_type;

class CircularBuffer {
	value_type* buffer;
	int bufferSize;
	int bufferFreeSize;
	int frontIndex;
	int backIndex;

public:
	CircularBuffer();
	~CircularBuffer();
	CircularBuffer(const CircularBuffer& cb);
	explicit CircularBuffer(int capacity);
	CircularBuffer(int capacity, const value_type& elem);
	value_type& operator[](int i);
	const value_type& operator[](int i) const;
	value_type& at(int i);
	const value_type& at(int i) const;
	value_type& front();
	value_type& back();
	const value_type& front() const;
	const value_type& back() const;
	void linearize();
	bool isLinearized() const;
	void rotate(int new_begin);
	int size() const;
	bool empty() const;
	bool full() const;
	int reserve() const;
	int capacity() const;
	void setCapacity(int newCapacity);
	void resize(int newSize, const value_type& item = value_type());
	CircularBuffer& operator=(const CircularBuffer& cb);
	void swap(CircularBuffer& cb);
	void pushBack(const value_type& item = value_type());
	void pushFront(const value_type& item = value_type());
	void popBack();
	void popFront();
	void insert(int pos, const value_type& item = value_type());
	void erase(int first, int last);
	void clear();

private:
	int bufferIndex(int i) const;
	int moduloIncrease(int& increasing, int plus, int module);
};

bool operator==(const CircularBuffer& a, const CircularBuffer& b);
bool operator!=(const CircularBuffer& a, const CircularBuffer& b);
