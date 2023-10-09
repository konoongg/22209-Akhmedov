#include <gtest/gtest.h>
#include <CircularBuffer.h> 

TEST(CircularBufferInitTest, InitEmptyTest) {
    CircularBuffer cb{};

    EXPECT_EQ(cb.capacity(), 0);
    EXPECT_EQ(cb.reserve(), 0);
    EXPECT_TRUE(cb.empty());
}

TEST(CircularBufferInitTest, InitEmptyGivenSizeTest) {
    CircularBuffer cb{ 10 };

    EXPECT_EQ(cb.capacity(), 10);
    EXPECT_EQ(cb.reserve(), 10);
    EXPECT_TRUE(cb.empty());
    EXPECT_FALSE(cb.full());
}

TEST(CircularBufferInitTest, InitTest) {
    CircularBuffer cb{ 10, 'a' };

    EXPECT_EQ(cb.capacity(), 10);
    EXPECT_EQ(cb.reserve(), 0);
    EXPECT_FALSE(cb.empty());
    EXPECT_TRUE(cb.full());
    EXPECT_EQ(cb.back(), 'a');
    EXPECT_EQ(cb.front(),'a');
}

TEST(CircularBufferInitTest, InitCopyTest) {
    CircularBuffer cb{ 10, 'a' };
    CircularBuffer cbCopy{ 10, 'a' };
    EXPECT_EQ(cbCopy.capacity(), cb.capacity());
    EXPECT_EQ(cbCopy.reserve(), cb.reserve());
    EXPECT_EQ(cb.back(), cbCopy.back());
    EXPECT_EQ(cb.front(),cbCopy.front() );
    for (int i = 0; i < cb.capacity(); ++i) {
        EXPECT_EQ(cbCopy[i], cb[i]);
    }
}

TEST(CircularBufferAdditionTest, pushBackNotFullStartIndexNullTest) {
    CircularBuffer cb{10};
    cb.pushBack('b');
    cb.pushBack('a');
    EXPECT_EQ(cb[0], 'b');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb.front(), 'b');
    EXPECT_EQ(cb.back(), 'a');
    for (int i = 2; i < cb.capacity(); ++i) {
        EXPECT_EQ(cb[i], 0);
    }
}

TEST(CircularBufferAdditionTest, pushBackNotFullStartIndexNotNullTest) {
    CircularBuffer cb{ 5, 'a'};
    cb.erase(0, 3);
    cb.pushBack('b');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 'b');
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb[4], 0);
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'b');
    EXPECT_EQ(cb.reserve(), 2);
    EXPECT_EQ(cb.size(), 3);
}

TEST(CircularBufferAdditionTest, pushBackFullTest) {
    CircularBuffer cb{ 3, 'a' };
    cb.pushBack('b');
    cb.pushBack('c');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'c');
}

TEST(CircularBufferAdditionTest, pushBackNotItemTest) {
    CircularBuffer cb{ 3, 'a' };
    cb.pushBack();
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 0);
}

TEST(CircularBufferAdditionTest, insertFullNotStartIndexShiftTest) {
    CircularBuffer cb{ 3, 'a' };
    cb.insert(1, 'b');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'a');
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'a');
    EXPECT_EQ(cb.reserve(), 0);
    EXPECT_EQ(cb.size(), 3);
}

TEST(CircularBufferAdditionTest, insertNotItemTest) {
    CircularBuffer cb{ 3, 'a' };
    cb.insert(1);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 0);
    EXPECT_EQ(cb[2], 'a');
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'a');
    EXPECT_EQ(cb.reserve(), 0);
    EXPECT_EQ(cb.size(), 3);
}

TEST(CircularBufferAdditionTest, insertFullStartIndexShiftTest) {
    CircularBuffer cb{ 3, 'a' };
    cb.pushBack('b');
    cb.insert(1, 'c');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'c');
    EXPECT_EQ(cb[2], 'b');
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'b');
    EXPECT_EQ(cb.reserve(), 0);
    EXPECT_EQ(cb.size(), 3);
}

TEST(CircularBufferAdditionTest, insertNotFullStartIndexNotShiftTest) {
    CircularBuffer cb{ 3, };
    cb.pushBack('b');
    cb.pushBack('b');
    cb.insert(1, 'c');
    EXPECT_EQ(cb[0], 'b');
    EXPECT_EQ(cb[1], 'c');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb.front(), 'b');
    EXPECT_EQ(cb.back(), 'c');
    EXPECT_EQ(cb.reserve(), 1);
    EXPECT_EQ(cb.size(), 2);
}

TEST(CircularBufferAdditionTest, insertNotFullStartIndexShiftTest) {
    CircularBuffer cb{ 5, 'a' };
    cb.erase(0, 3);
    EXPECT_EQ(cb.reserve(), 3);
    EXPECT_EQ(cb.size(), 2);
    cb.insert(1, 'c');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'c');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb[4], 0);
}

TEST(CircularBufferAdditionTest, pushFrontNotFullTest) {
    CircularBuffer cb{ 3 };
    cb.pushFront('b');
    cb.pushFront('a');
    cb.pushFront('c');
    EXPECT_EQ(cb[0], 'c');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 'b');
    EXPECT_EQ(cb.front(), 'c');
    EXPECT_EQ(cb.back(), 'b');
    EXPECT_EQ(cb.reserve(), 0);
    EXPECT_EQ(cb.size(), 3);
}

TEST(CircularBufferAdditionTest, pushFrontNotItemTest) {
    CircularBuffer cb{ 3, 'a'};
    cb.pushFront();
    EXPECT_EQ(cb[0], 0);
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 'a');
    EXPECT_EQ(cb.front(), 0);
    EXPECT_EQ(cb.back(), 'a');
    EXPECT_EQ(cb.reserve(), 0);
    EXPECT_EQ(cb.size(), 3);
}

TEST(CircularBufferAdditionTest, pushFrontFullTest) {
    CircularBuffer cb{ 3 };
    cb.pushFront('a');
    cb.pushFront('b');
    cb.pushFront('c');
    cb.pushFront('d');
    EXPECT_EQ(cb[0], 'd');
    EXPECT_EQ(cb[1], 'c');
    EXPECT_EQ(cb[2], 'b');
    cb.pushFront('e');
    EXPECT_EQ(cb[0], 'e');
    EXPECT_EQ(cb[1], 'd');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb.front(), 'e');
    EXPECT_EQ(cb.back(), 'c');
    EXPECT_EQ(cb.reserve(), 0);
    EXPECT_EQ(cb.size(), 3);
}

TEST(CircularBufferAdditionTest, pushFrontNotFullStartIndexNotNullTest) {
    CircularBuffer cb{ 5, 'a' };
    cb.erase(0, 3);
    cb.pushFront('b');
    EXPECT_EQ(cb[0], 'b');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 'a');
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb[4], 0);
    EXPECT_EQ(cb.front(), 'b');
    EXPECT_EQ(cb.back(), 'a');
    EXPECT_EQ(cb.reserve(), 2);
    EXPECT_EQ(cb.size(), 3);
}

TEST(CircularBufferDeletionTest, popBackNotStartIndexShiftTest) {
    CircularBuffer cb{ 3 };
    cb.pushBack('b');
    cb.pushBack('a');
    cb.pushBack('c');
    cb.popBack();
    EXPECT_EQ(cb[0], 'b');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb.reserve(), 1);
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.front(), 'b');
    EXPECT_EQ(cb.back(), 'a');
}

TEST(CircularBufferDeletionTest, popBackStartIndexShiftTest) {
    CircularBuffer cb{ 3, 'a' };
    cb.pushBack('b');
    cb.pushBack('b');
    cb.popBack();
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.reserve(), 1);
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'b');
}

TEST(CircularBufferDeletionTest, popBackNotfullNotStartIndexShift) {
    CircularBuffer cb{ 3 };
    cb.pushBack('b');
    cb.pushBack('a');
    cb.popBack();
    EXPECT_EQ(cb[0], 'b');
    EXPECT_EQ(cb[1], 0);
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb.size(), 1);
    EXPECT_EQ(cb.reserve(), 2);
    EXPECT_EQ(cb.front(), 'b');
    EXPECT_EQ(cb.back(), 'b');
}

TEST(CircularBufferDeletionTest, popBackNotfullStartIndexShift) {
    CircularBuffer cb{ 3, 'a' };
    cb.pushBack('b');
    cb.pushBack('b');
    cb.popBack();
    cb.popBack();
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 0);
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb.size(), 1);
    EXPECT_EQ(cb.reserve(), 2);
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'a');
}

TEST(CircularBufferDeletionTest, popBackNowNullL) {
    CircularBuffer cb{ 3, 'a' };
    cb.popBack();
    cb.popBack();
    cb.popBack();
    EXPECT_EQ(cb[0], 0);
    EXPECT_EQ(cb[1], 0);
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb.size(), 0);
    EXPECT_EQ(cb.reserve(), 3);
}

TEST(CircularBufferDeletionTest, popFrontNotStartIndexShiftTest) {
    CircularBuffer cb{ 3 };
    cb.pushFront('a');
    cb.pushFront('b');
    cb.pushFront('c');
    cb.popFront();
    EXPECT_EQ(cb[0], 'b');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.reserve(), 1);
    EXPECT_EQ(cb.front(), 'b');
    EXPECT_EQ(cb.back(), 'a');
}

TEST(CircularBufferDeletionTest, popFrontStartIndexShiftTest) {
    CircularBuffer cb{ 3 };
    cb.pushFront('a');
    cb.pushFront('b');
    cb.pushFront('c');
    cb.pushFront('d');
    cb.pushFront('e');
    cb.popFront();
    EXPECT_EQ(cb[0], 'd');
    EXPECT_EQ(cb[1], 'c');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.reserve(), 1);
    EXPECT_EQ(cb.front(), 'd');
    EXPECT_EQ(cb.back(), 'c');
}

TEST(CircularBufferDeletionTest, popFrontNowNull) {
    CircularBuffer cb{ 3 };
    cb.pushFront('a');
    cb.pushFront('b');
    cb.pushFront('c');
    cb.popFront();
    cb.popFront();
    cb.popFront();
    EXPECT_EQ(cb[0], 0);
    EXPECT_EQ(cb[1], 0);
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb.size(), 0);
    EXPECT_EQ(cb.reserve(), 3);
}

TEST(CircularBufferDeletionTest, clearTest) {
    CircularBuffer cb{ 3, 'a'};
    cb.clear();
    for (int i = 0; i < cb.capacity(); ++i) {
        EXPECT_EQ(cb[i], 0);
    }
    EXPECT_EQ(cb.size(), 0);
    EXPECT_EQ(cb.reserve(), 3);
}

TEST(CircularBufferDeletionTest, addingAfterClearTest) {
    CircularBuffer cb{ 3, 'a' };
    cb.clear();
    cb.pushBack('a');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 0);
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb.size(), 1);
    EXPECT_EQ(cb.reserve(), 2);
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'a');
}

TEST(CircularBufferDeletionTest, generlyEraseTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.erase(1, 3);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb.back(), 'a');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.reserve(), 2);
}

TEST(CircularBufferDeletionTest, toEndEraseNotShiftIndexTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.erase(1, 3);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb.back(), 'a');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.reserve(), 2);
}

TEST(CircularBufferDeletionTest, eraseShiftIndexTest) {
    CircularBuffer cb{ 5, 'a' };
    cb.pushBack('b');
    cb.pushBack('c');
    cb.erase(1, 3);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb.back(), 'c');
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb[4], 0);
    EXPECT_EQ(cb.size(), 3);
    EXPECT_EQ(cb.reserve(), 2);
}

TEST(CircularBufferDeletionTest, fromStartEraseTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.erase(0, 2);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb.back(), 'a');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.reserve(), 2);
}

TEST(CircularBufferDeletionTest, fromStartToEndEraseTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.erase(0, 3);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb.back(), 'a');
    EXPECT_EQ(cb[1], 0);
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb.size(), 1);
    EXPECT_EQ(cb.reserve(), 3);
}

TEST(CircularBufferPermutationsTest, linearizeFullTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.pushBack('d');
    cb.pushBack('e');
    cb.linearize();
    EXPECT_EQ(cb.back(), 'e');
    EXPECT_EQ(cb.front(), 'b');
    EXPECT_EQ(cb[0], 'b');
    EXPECT_EQ(cb[1], 'c');
    EXPECT_EQ(cb[2], 'd');
    EXPECT_EQ(cb[3], 'e');
    EXPECT_EQ(cb.size(), 4);
    EXPECT_EQ(cb.reserve(), 0);
}

TEST(CircularBufferPermutationsTest, linearizeNotFullTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.pushBack('d');
    cb.erase(0,2);
    cb.linearize();
    EXPECT_EQ(cb.back(), 'd');
    EXPECT_EQ(cb.front(), 'c');
    EXPECT_EQ(cb[0], 'c');
    EXPECT_EQ(cb[1], 'd');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.reserve(), 2);
}

TEST(CircularBufferPermutationsTest, linearizeNotFullWithWindowTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.pushBack('d');
    cb.pushBack('e');
    cb.erase(0, 1);
    cb.linearize();
    EXPECT_EQ(cb.back(), 'e');
    EXPECT_EQ(cb.front(), 'c');
    EXPECT_EQ(cb[0], 'c');
    EXPECT_EQ(cb[1], 'd');
    EXPECT_EQ(cb[2], 'e');
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb.size(), 3);
    EXPECT_EQ(cb.reserve(), 1);
}

TEST(CircularBufferPermutationsTest, linearizeYetTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.pushBack('d');
    cb.linearize();
    EXPECT_EQ(cb.back(), 'd');
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb[3], 'd');
    EXPECT_EQ(cb.size(), 4);
    EXPECT_EQ(cb.reserve(), 0);

}

TEST(CircularBufferPermutationsTest, isLinearizedTrueTest) {
    CircularBuffer cb{ 4, 'a' };
    EXPECT_TRUE(cb.isLinearized());
 }

TEST(CircularBufferPermutationsTest, isLinearizedFalseTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.pushFront('b');
    EXPECT_FALSE(cb.isLinearized());
}

TEST(CircularBufferPermutationsTest, rotateSizeTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.pushBack('d');
    cb.rotate(2);
    EXPECT_EQ(cb.front(), 'c');
    EXPECT_EQ(cb.back(), 'b');
    EXPECT_EQ(cb[0], 'c');
    EXPECT_EQ(cb[1], 'd');
    EXPECT_EQ(cb[2], 'a');
    EXPECT_EQ(cb[3], 'b');
    EXPECT_EQ(cb.size(), 4);
    EXPECT_EQ(cb.reserve(), 0);
}

TEST(CircularBufferPermutationsTest, rotateNotChangeSizeTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.pushBack('d');
    cb.rotate(0);
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'd');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb[3], 'd');
    EXPECT_EQ(cb.size(), 4);
    EXPECT_EQ(cb.reserve(), 0);
}

TEST(CircularBufferPermutationsTest, rotateNotFullSizeTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.rotate(1);
    EXPECT_EQ(cb.front(), 'b');
    EXPECT_EQ(cb.back(), 'a');
    EXPECT_EQ(cb[0], 'b');
    EXPECT_EQ(cb[1], 'c');
    EXPECT_EQ(cb[2], 'a');
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb.size(), 3);
    EXPECT_EQ(cb.reserve(), 1);
}

TEST(CircularBufferPermutationsTest, rotateNotFullSizeWithShiftTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.pushBack('d');
    cb.erase(0, 2);
    cb.rotate(1);
    EXPECT_EQ(cb.back(), 'c');
    EXPECT_EQ(cb.front(), 'd');
    EXPECT_EQ(cb[0], 'd');
    EXPECT_EQ(cb[1], 'c');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.reserve(), 2);
}

TEST(CircularBufferChangeOfSizeTest, setMoreCapacityTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.pushBack('d');
    cb.setCapacity(6);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb[3], 'd');
    EXPECT_EQ(cb[4], 0);
    EXPECT_EQ(cb[5], 0);
    EXPECT_EQ(cb.capacity(), 6);
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'd');
    EXPECT_EQ(cb.reserve(), 2);
}

TEST(CircularBufferChangeOfSizeTest, setLessCapacityTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.pushBack('d');
    cb.setCapacity(2);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb.capacity(), 2);
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'b');
    EXPECT_EQ(cb.reserve(), 0);
}

TEST(CircularBufferChangeOfSizeTest, setEqCapacityTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.pushBack('d');
    cb.setCapacity(4);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb[3], 'd');
    EXPECT_EQ(cb.capacity(), 4);
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'd');
    EXPECT_EQ(cb.reserve(), 0);
}

TEST(CircularBufferChangeOfSizeTest, notFullSetCapacityTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.setCapacity(6);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb[4], 0);
    EXPECT_EQ(cb[5], 0);
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'c');
    EXPECT_EQ(cb.capacity(), 6);
    EXPECT_EQ(cb.reserve(), 3);
}

TEST(CircularBufferChangeOfSizeTest, moreResizeTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.pushBack('d');
    cb.resize(6,'e');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb[3], 'd');
    EXPECT_EQ(cb[4], 'e');
    EXPECT_EQ(cb[5], 'e');
    EXPECT_EQ(cb.capacity(), 6);
    EXPECT_EQ(cb.size(), 6);
    EXPECT_EQ(cb.reserve(), 0);
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'e');
}

TEST(CircularBufferChangeOfSizeTest, notItemResizeTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.pushBack('d');
    cb.resize(6);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb[3], 'd');
    EXPECT_EQ(cb[4], 0);
    EXPECT_EQ(cb[5], 0);
    EXPECT_EQ(cb.capacity(), 6);
    EXPECT_EQ(cb.size(), 6);
    EXPECT_EQ(cb.reserve(), 0);
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 0);
}

TEST(CircularBufferChangeOfSizeTest, lessResizeTest) {
    CircularBuffer cb{ 4};
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.pushBack('d');
    cb.resize(2,'b');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb.capacity(), 4);
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.reserve(), 2);
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'b');
}

TEST(CircularBufferChangeOfSizeTest, eqResizeTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.pushBack('d');
    cb.resize(4,'a');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb[3], 'd');
    EXPECT_EQ(cb.capacity(), 4);
    EXPECT_EQ(cb.size(), 4);
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'd');
    EXPECT_EQ(cb.reserve(), 0);
}

TEST(CircularBufferChangeOfSizeTest, notFullResizeTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.resize(6, 'e');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb[3], 'e');
    EXPECT_EQ(cb[4], 'e');
    EXPECT_EQ(cb[5], 0);
    EXPECT_EQ(cb.front(), 'a');
    EXPECT_EQ(cb.back(), 'e');
    EXPECT_EQ(cb.capacity(), 6);
    EXPECT_EQ(cb.size(), 5);
    EXPECT_EQ(cb.reserve(), 1);
}

TEST(CircularBufferSwapTest, swapTwoCBTest) {
    CircularBuffer cb1{ 2,'a'};
    CircularBuffer cb2{ 2,'b' };
    cb1.swap(cb2);
    EXPECT_EQ(cb2[0], 'a');
    EXPECT_EQ(cb2[1], 'a');
    EXPECT_EQ(cb1[0], 'b');
    EXPECT_EQ(cb1[1], 'b');
}

TEST(CircularBufferSwapTest, swapTwoCBSmallerTest) {
    CircularBuffer cb1{ 2,'a' };
    CircularBuffer cb2{ 3,'b' };
    cb1.swap(cb2);
    EXPECT_EQ(cb2[0], 'a');
    EXPECT_EQ(cb2[1], 'a');
    EXPECT_EQ(cb1[0], 'b');
    EXPECT_EQ(cb1[1], 'b');
    EXPECT_EQ(cb1[2], 'b');
    EXPECT_EQ(cb1.capacity(), 3);
    EXPECT_EQ(cb2.capacity(), 2);
}

TEST(CircularBufferSwapTest, swapTwoCBBiggerTest) {
    CircularBuffer cb1{ 3,'a' };
    CircularBuffer cb2{ 2,'b' };
    cb1.swap(cb2);
    EXPECT_EQ(cb2[0], 'a');
    EXPECT_EQ(cb2[1], 'a');
    EXPECT_EQ(cb2[2], 'a');
    EXPECT_EQ(cb1[0], 'b');
    EXPECT_EQ(cb1[1], 'b');
    EXPECT_EQ(cb1.capacity(), 2);
    EXPECT_EQ(cb2.capacity(), 3);
}

TEST(CircularBufferRequestTest, requestTest) {
    CircularBuffer cb{ 2 };
    cb.pushBack('a');
    cb.pushBack('b');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
}

TEST(CircularBufferRequestTest, requestConstTest) {
    const CircularBuffer cb{ 2,'a'};
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
}

TEST(CircularBufferRequestTest, atRequestTest) {
    CircularBuffer cb{ 2 };
    cb.pushBack('a');
    cb.pushBack('b');
    EXPECT_EQ(cb.at(0), 'a');
    EXPECT_EQ(cb.at(1), 'b');
}

TEST(CircularBufferRequestTest, atRequestConstTest) {
    const CircularBuffer cb{ 2,'a' };
    EXPECT_EQ(cb.at(0), 'a');
    EXPECT_EQ(cb.at(0), 'a');
}

TEST(CircularBufferRequestTest, frontTest) {
    CircularBuffer cb{ 2 };
    cb.pushBack('a');
    cb.pushBack('b');
    EXPECT_EQ(cb.front(), 'a');
}

TEST(CircularBufferRequestTest, frontConstTest) {
    const CircularBuffer cb{ 2,'a' };
    EXPECT_EQ(cb.front(), 'a');
}

TEST(CircularBufferRequestTest, backTest) {
    CircularBuffer cb{ 2 };
    cb.pushBack('a');
    cb.pushBack('b');
    EXPECT_EQ(cb.back(), 'b');
}

TEST(CircularBufferRequestTest, backConstTest) {
    const CircularBuffer cb{ 2,'a' };
    EXPECT_EQ(cb.back(), 'a');
}

TEST(CircularBufferOperatorTest, eqFullTest) {
    CircularBuffer cb{ 2,'a' };
    CircularBuffer cb1{ 4 };
    cb1.pushBack('a'); 
    cb1.pushBack('b');
    cb1.pushBack('c');
    cb1.pushBack('d');
    cb = cb1;
    EXPECT_EQ(cb.capacity(), 4);
    EXPECT_EQ(cb.size(), 4);
    EXPECT_EQ(cb.reserve(), 0);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb[3], 'd');
    EXPECT_EQ(cb.back(), 'd');
    EXPECT_EQ(cb.front(), 'a');
}

TEST(CircularBufferOperatorTest, eqNulFullTest) {
    CircularBuffer cb{ 2,'a' };
    CircularBuffer cb1{ 4 };
    cb1.pushBack('a');
    cb1.pushBack('b');
    cb1.pushBack('c');
    cb = cb1;
    EXPECT_EQ(cb.capacity(), 4);
    EXPECT_EQ(cb.size(), 3);
    EXPECT_EQ(cb.reserve(), 1);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb.back(), 'c');
    EXPECT_EQ(cb.front(), 'a');
}

TEST(CircularBufferOperatorTest, eqSizeEqTest) {
    CircularBuffer cb{ 4,'a' };
    CircularBuffer cb1{ 4 };
    cb1.pushBack('a');
    cb1.pushBack('b');
    cb1.pushBack('c');
    cb = cb1;
    EXPECT_EQ(cb.capacity(), 4);
    EXPECT_EQ(cb.size(), 3);
    EXPECT_EQ(cb.reserve(), 1);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb.back(), 'c');
    EXPECT_EQ(cb.front(), 'a');
}

TEST(CircularBufferOperatorTest, eqthisTest) {
    CircularBuffer cb{ 3 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb = cb;
    EXPECT_EQ(cb.capacity(), 3);
    EXPECT_EQ(cb.size(), 3);
    EXPECT_EQ(cb.reserve(), 0);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'c');
    EXPECT_EQ(cb.back(), 'c');
    EXPECT_EQ(cb.front(), 'a');
}

TEST(CircularBufferEqTest, eqTest) {
    CircularBuffer cb{ 2,'a' };
    CircularBuffer cb1{2, 'a'};
    bool eq = (cb == cb1);
    EXPECT_TRUE(eq);
}

TEST(CircularBufferEqTest, notEqCapicityTest) {
    CircularBuffer cb{ 2,'a' };
    CircularBuffer cb1{ 4, 'a' };
    bool eq = (cb == cb1);
    EXPECT_FALSE(eq);
}

TEST(CircularBufferEqTest, notEqIndexTest) {
    CircularBuffer cb{ 3, 'a' };
    CircularBuffer cb1{ 3, 'a' };
    cb.pushBack('a');
    bool eq = (cb == cb1);
    EXPECT_TRUE(eq);
}

TEST(CircularBufferEqTest, notEqSizeTest) {
    CircularBuffer cb{ 3, 'a' };
    CircularBuffer cb1{ 3, };
    bool eq = (cb == cb1);
    EXPECT_FALSE(eq);
}

TEST(CircularBufferEqTest, notEqElemTest) {
    CircularBuffer cb{ 3, 'a' };
    CircularBuffer cb1{ 3, 'b'};
    bool eq = (cb == cb1);
    EXPECT_FALSE(eq);
}

TEST(CircularBufferNotEqTest, eqTest) {
    CircularBuffer cb{ 2,'a' };
    CircularBuffer cb1{ 2, 'a' };
    bool notEq = (cb != cb1);
    EXPECT_FALSE(notEq);
}

TEST(CircularBufferNotEqTest, notEqCapicityTest) {
    CircularBuffer cb{ 2,'a' };
    CircularBuffer cb1{ 4, 'a' };
    bool notEq = (cb != cb1);
    EXPECT_TRUE(notEq);
}

TEST(CircularBufferNotEqTest, notEqIndexTest) {
    CircularBuffer cb{ 3, 'a' };
    CircularBuffer cb1{ 3, 'a' };
    cb.pushBack('a');
    bool notEq = (cb != cb1);
    EXPECT_FALSE(notEq);
}

TEST(CircularBufferNotEqTest, notEqSizeTest) {
    CircularBuffer cb{ 3, 'a' };
    CircularBuffer cb1{ 3, };
    bool notEq = (cb != cb1);
    EXPECT_TRUE(notEq);
}

TEST(CircularBufferNotEqTest, notEqElemTest) {
    CircularBuffer cb{ 3, 'a' };
    CircularBuffer cb1{ 3, 'b' };
    bool notEq = (cb != cb1);
    EXPECT_TRUE(notEq);
}

TEST(CircularBufferThrowTest, atLessOutOfRangeTest) {
    CircularBuffer cb{ 2 };
    EXPECT_ANY_THROW(cb.at(-1));
}

TEST(CircularBufferThrowTest, atMoreOutOfRangeTest) {
    CircularBuffer cb{ 2 };
    EXPECT_ANY_THROW(cb.at(2));
}

TEST(CircularBufferThrowTest, atLessOutOfRangeConstTest) {
    const CircularBuffer cb{ 2 };
    EXPECT_ANY_THROW(cb.at(-1));
}

TEST(CircularBufferThrowTest, atMoreOutOfRangeConstTest) {
    const CircularBuffer cb{ 2 };
    EXPECT_ANY_THROW(cb.at(2));
}

TEST(CircularBufferThrowTest, atMoreBackIndexOutOfRangeTest) {
    CircularBuffer cb{ 2 };
    cb.pushBack('b');
    EXPECT_ANY_THROW(cb.at(1));
}

TEST(CircularBufferThrowTest, atLessFrontIndexOutOfRangeTest) {
    CircularBuffer cb{ 2 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.popBack();
    EXPECT_ANY_THROW(cb.at(1));
}

TEST(CircularBufferThrowTest, atMoreBackIndexOutOfRangeConstTest) {
    CircularBuffer cb{ 2 };
    cb.pushBack('b');
    const CircularBuffer cb1 = cb;
    EXPECT_ANY_THROW(cb1.at(1));
}

TEST(CircularBufferThrowTest, atLessFrontIndexOutOfRangeConstTest) {
    CircularBuffer cb{ 2 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.popBack();
    const CircularBuffer cb1 = cb;
    EXPECT_ANY_THROW(cb1.at(1));
}


TEST(CircularBufferThrowTest, rotateLessOutOfRangeTest) {
    CircularBuffer cb{ 2 };
    EXPECT_ANY_THROW(cb.rotate(-1));
}

TEST(CircularBufferThrowTest, rotateMoreOutOfRangeTest) {
    CircularBuffer cb{ 2 };
    EXPECT_ANY_THROW(cb.rotate(3));
}

TEST(CircularBufferThrowTest, eraseLessOutOfRangeTest) {
    CircularBuffer cb{ 5,'a' };
    EXPECT_ANY_THROW(cb.erase(-1, 5));
}

TEST(CircularBufferThrowTest, eraseMoreOutOfRangeTest) {
    CircularBuffer cb{ 5,'a' };
    EXPECT_ANY_THROW(cb.erase(3, 12));
}

TEST(CircularBufferThrowTest, popBackNotHaveElemTest) {
    CircularBuffer cb{ 5 };
    EXPECT_ANY_THROW(cb.popBack());
}

TEST(CircularBufferThrowTest, popFrontNotHaveElemTest) {
    CircularBuffer cb{ 5 };
    EXPECT_ANY_THROW(cb.popFront());
}

TEST(CircularBufferThrowTest, insertFullLessIndexTest) {
    CircularBuffer cb{5, 'a'};
    EXPECT_ANY_THROW(cb.insert(-1, 'b'));
}

TEST(CircularBufferThrowTest, insertFullMoreIndexTest) {
    CircularBuffer cb{ 5, 'a' };
    EXPECT_ANY_THROW(cb.insert(5, 'b'));
}

TEST(CircularBufferThrowTest, insertNotHaveElemTest) {
    CircularBuffer cb{ 5 };
    EXPECT_ANY_THROW(cb.insert(0, 'b'));
}

TEST(CircularBufferThrowTest, insertNotFullLessIndexTest) {
    CircularBuffer cb{ 5, 'a'};
    cb.erase(0,3);
    EXPECT_ANY_THROW(cb.insert(2,'b'));
}

TEST(CircularBufferThrowTest, insertNotFullMoreIndexTest) {
    CircularBuffer cb{ 5 };
    cb.pushBack('a');
    cb.pushBack('a');
    EXPECT_ANY_THROW(cb.insert(2, 'b'));
}

TEST(CircularBufferThrowTest, rotateLessFrontIndexTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    cb.pushBack('c');
    cb.pushBack('d');
    cb.erase(0, 2);
    EXPECT_ANY_THROW(cb.rotate(2));
}

TEST(CircularBufferThrowTest, rotateMoreBackIndexIndexTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('b');
    EXPECT_ANY_THROW(cb.rotate(2));
}

int main(int argc, char** argv) {
    ::testing::InitGoogleTest(&argc, argv);

    return RUN_ALL_TESTS();
}
