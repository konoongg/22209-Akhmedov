# CircularBuffer
____

#### CircularBuffer()
____
Does not accept arguments.constructor that is used in cases where the size is not specified

#### ~CircularBuffer()
____
Destructor, fires when an object is deleted

#### CircularBuffer(const CircularBuffer& cb)
____
A constructor that takes a reference to another buffer as an argument and creates a buffer in its image

#### CircularBuffer(int capacity)
____
A constructor that takes the buffer size as an argument. Fires when an object is created. creates buffers of the specified size

#### CircularBuffer(int capacity, const value_type& elem)
____
A constructor that takes as an argument the size of the buffer and the element with which the buffer will be filled. Fires when an object is created. creates a buffer 
of the specified size and fills it with the specified element

#### operator[](int i)
____
returns an element from a buffer according to the first index of the buffer

#### operator[](int i) const
____
returns an element from a buffer according to the first index of the buffer. used for constant objects

#### at(int i)
____
takes index as argument. A safe version of the function that returns the buffer element from the beginning of the buffer index.

#### at(int i) const
____
takes index as argument. A safe version of the function that returns the buffer element from the beginning of the buffer index. used for constant objects

#### front()
____
returns the first element of the buffer

#### back()
____
returns the last element of the buffer

#### front() const
____
returns the first element of the buffer used for constant objects

#### back() const
____
returns the last element of the buffer used for constant objects

#### linearize()
____
shifts the elements of the buffer so that the starting index of the buffer is 0

#### isLinearized()
____
returns true if the buffer's starting index is , false otherwise

#### rotate(int new_begin)
____
takes an index. Shifts the buffer so that the starting index of the buffer is equal to this index

#### size()
____
returns the number of elements in the buffer

#### empty()
____
returns true if the buffer is empty, false otherwise

#### full()
____
returns true if the buffer is full, false otherwise

#### reserve()
____
returns the number of free spaces in the buffer

#### capacity()
____
returns the buffer size

#### startIndex()
____
returns the initial index of the buffer

#### setCapacity(int newCapacity)
____
accepts the new buffer size. shifts it so that the initial buffer index is 0. changes the buffer size, if the new size is larger, fills new cells with 0, copies old ones

#### resize(int newSize, const value_type& item)
____
accepts the new buffer size and element. shifts it so that the initial buffer index is 0. changes the buffer size, if the new size is larger, fills new cells with the specified element, copies old ones

#### operator=
____
changes all buffer parameters to the parameters of the buffer to which it is equated

#### swap(CircularBuffer& cb)
____
accepts a reference to a buffer. if the sizes of the source and transmitted buffers are different, it returns an error, otherwise it swaps their elements.

#### pushBack(const value_type& item)
____
accepts an argument, an element to be added .if the buffer is incomplete, then adds an element to the end of the buffer, if full, then overwrites the element at the starting index of the buffer

#### swapInBuffer(int indexFirst, int indexSecond)
____
takes two indices, changes elements in the buffer according to these indices with each other

#### pushFront(const value_type& item)
____
accepts an argument, an element to be added.if the buffer is incomplete, then writes this value to the first cell of the buffer, and shifts the rest, 
if full, then replaces the last element and changes the index of the beginning of the buffer

#### popBack()
____
deletes the last element of the buffer, shifts the starting index of the array to zero

#### popFront()
____
deletes the first element of the buffer, shifts the starting index of the array to zero

#### insert(int pos, const value_type& item)
____
takes two arguments, position and element, inserts the passed element into the buffer at the index of the arguments

#### insert(int pos, const value_type& item)
____

#### erase(int first, int last)
____
takes two arguments, which are buffer indices, removes buffer elements from index first, inclusive, to last, inclusive.
Shifts the remaining elements, shifts the buffer start index to 0. If the arguments are larger than the buffer size or less than 0, then it returns an error

#### clear()
____
clears the buffer, replaces all elements with zero, shifts the buffer start index to 0

#### operator==
____
returns false if the buffers have different sizes or the elements contained in the buffer or the starting index of the buffer or the number of free elements, otherwise true

#### operator!=
____
returns true if operator== returns false, false otherwise
