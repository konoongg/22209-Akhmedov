#include <gtest/gtest.h>
#include <CircularBuffer.h> 

TEST(CircularBufferInitTest, InitEmptyTest) {
    CircularBuffer cb{};

    EXPECT_EQ(cb.capacity(), 0);
    EXPECT_EQ(cb.startIndex(), 0);
    EXPECT_EQ(cb.reserve(), 0);
    EXPECT_TRUE(cb.empty());
    EXPECT_FALSE(cb.full());
}

TEST(CircularBufferInitTest, InitEmptyGivenSizeTest) {
    CircularBuffer cb{ 10 };

    EXPECT_EQ(cb.capacity(), 10);
    EXPECT_EQ(cb.startIndex(), 0); 
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
    EXPECT_EQ(cb.startIndex(), 0);
}

TEST(CircularBufferInitTest, InitCopyTest) {
    CircularBuffer cb{ 10, 'a' };
    CircularBuffer cbCopy{ 10, 'a' };
    EXPECT_EQ(cbCopy.capacity(), cb.capacity());
    EXPECT_EQ(cbCopy.reserve(), cb.reserve());
    EXPECT_EQ(cbCopy.startIndex(), cb.startIndex());
    for (int i = 0; i < cb.capacity(); ++i) {
        EXPECT_EQ(cbCopy[i], cb[i]);
    }
}

TEST(CircularBufferAdditionTest, pushBackNotFullTest) {
    CircularBuffer cb{10};
    cb.pushBack('b');
    cb.pushBack('a');
    EXPECT_EQ(cb[0], 'b');
    EXPECT_EQ(cb[1], 'a');
    for (int i = 2; i < cb.capacity(); ++i) {
        EXPECT_EQ(cb[i], 0);
    }
}

TEST(CircularBufferAdditionTest, pushBackFullTest) {
    CircularBuffer cb{ 3, 'a' };
    cb.pushBack('b');
    EXPECT_EQ(cb.startIndex(), 1);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 'b');
}

TEST(CircularBufferAdditionTest, insertNotStartIndexShiftTest) {
    CircularBuffer cb{ 3, 'a' };
    cb.insert(1, 'b');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'a');
}

TEST(CircularBufferAdditionTest, insertStartIndexShiftTest) {
    CircularBuffer cb{ 3, 'a' };
    cb.pushBack('b');
    EXPECT_EQ(cb.startIndex(), 1);
    cb.insert(1, 'c');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'c');
    EXPECT_EQ(cb[2], 'b');
}

TEST(CircularBufferAdditionTest, pushFrontNotFullTest) {
    CircularBuffer cb{ 3 };
    cb.pushFront('b');
    cb.pushFront('a');
    cb.pushFront('c');
    EXPECT_EQ(cb[0], 'c');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 'b');
}

TEST(CircularBufferAdditionTest, pushFrontFullTest) {
    CircularBuffer cb{ 3 };
    cb.pushFront('a');
    cb.pushFront('b');
    cb.pushFront('c');
    cb.pushFront('d');
    EXPECT_EQ(cb.startIndex(), 2);
    EXPECT_EQ(cb[0], 'd');
    EXPECT_EQ(cb[1], 'c');
    EXPECT_EQ(cb[2], 'b');
    cb.pushFront('e');
    EXPECT_EQ(cb.startIndex(), 1);
    EXPECT_EQ(cb[0], 'e');
    EXPECT_EQ(cb[1], 'd');
    EXPECT_EQ(cb[2], 'c');
}

TEST(CircularBufferDeletionTest, popBackNotStartIndexShiftTest) {
    CircularBuffer cb{ 3 };
    cb.pushBack('b');
    cb.pushBack('a');
    cb.pushBack('c');
    cb.popBack();
    EXPECT_EQ(cb[0], 'b');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb.reserve(), 1);
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.startIndex(), 0);
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
    EXPECT_EQ(cb.startIndex(), 0);
}

TEST(CircularBufferDeletionTest, popFrontNotStartIndexShiftTest) {
    CircularBuffer cb{ 3 };
    cb.pushFront('a');
    cb.pushFront('b');
    cb.pushFront('c');
    cb.popFront();
    EXPECT_EQ(cb[0], 'b');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.startIndex(), 0);
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
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.startIndex(), 0);
}

TEST(CircularBufferDeletionTest, clearTest) {
    CircularBuffer cb{ 3, 'a'};
    cb.clear();
    for (int i = 0; i < cb.capacity(); ++i) {
        EXPECT_EQ(cb[i], 0);
    }
    EXPECT_EQ(cb.size(), 0);
    EXPECT_EQ(cb.startIndex(), 0);
}

TEST(CircularBufferDeletionTest, generlyEraseTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.erase(1, 3);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.startIndex(), 0);
    EXPECT_EQ(cb.reserve(), 2);
}

TEST(CircularBufferDeletionTest, toEndEraseTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.erase(1, 3);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.startIndex(), 0);
    EXPECT_EQ(cb.reserve(), 2);
}

TEST(CircularBufferDeletionTest, fromStartEraseTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.erase(0, 2);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb.size(), 2);
    EXPECT_EQ(cb.startIndex(), 0);
    EXPECT_EQ(cb.reserve(), 2);
}

TEST(CircularBufferDeletionTest, fromStartToEndEraseTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.erase(0, 3);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 0);
    EXPECT_EQ(cb[2], 0);
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb.size(), 1);
    EXPECT_EQ(cb.startIndex(), 0);
    EXPECT_EQ(cb.reserve(), 3);
}

TEST(CircularBufferpermutationsTest, linearizeTest) {
    CircularBuffer cb{ 4, 'a' };
    EXPECT_EQ(cb.startIndex(), 0);
    cb.pushFront('b');
    cb.linearize();
    EXPECT_EQ(cb.startIndex(), 0);
}

TEST(CircularBufferPermutationsTest, linearizeTest) {
    CircularBuffer cb{ 4, 'a' };
    EXPECT_EQ(cb.startIndex(), 0);
    cb.pushFront('b');
    cb.linearize();
    EXPECT_EQ(cb.startIndex(), 0);
}

TEST(CircularBufferPermutationsTest, isLinearizedTrueTest) {
    CircularBuffer cb{ 4, 'a' };
    EXPECT_EQ(cb.startIndex(), 0);
    EXPECT_TRUE(cb.isLinearized());
 }

TEST(CircularBufferPermutationsTest, isLinearizedFalseTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.pushFront('b');
    EXPECT_FALSE(cb.isLinearized());
}

TEST(CircularBufferPermutationsTest, rotateSizeTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.rotate(2);
    EXPECT_EQ(cb.startIndex(), 2);
}

TEST(CircularBufferPermutationsTest, rotateNotChangeSizeTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.rotate(0);
    EXPECT_EQ(cb.startIndex(), 0);
}

TEST(CircularBufferChangeOfSizeTest, setMoreCapacityTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.setCapacity(6);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 'a');
    EXPECT_EQ(cb[3], 'a');
    EXPECT_EQ(cb[4], 0);
    EXPECT_EQ(cb[5], 0);
    EXPECT_EQ(cb.capacity(), 6);
    EXPECT_EQ(cb.reserve(), 2);
}

TEST(CircularBufferChangeOfSizeTest, setLessCapacityTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.setCapacity(2);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb.capacity(), 2);
    EXPECT_EQ(cb.reserve(), 0);
}

TEST(CircularBufferChangeOfSizeTest, setEqCapacityTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.setCapacity(4);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 'a');
    EXPECT_EQ(cb[3], 'a');
    EXPECT_EQ(cb.capacity(), 4);
    EXPECT_EQ(cb.reserve(), 0);
}

TEST(CircularBufferChangeOfSizeTest, notFullSetCapacityTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('a');
    cb.pushBack('a');
    cb.setCapacity(6);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 'a');
    EXPECT_EQ(cb[3], 0);
    EXPECT_EQ(cb[4], 0);
    EXPECT_EQ(cb[5], 0);
    EXPECT_EQ(cb.capacity(), 6);
    EXPECT_EQ(cb.reserve(), 3);
}

TEST(CircularBufferChangeOfSizeTest, moreResizeTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.resize(6,'b');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 'a');
    EXPECT_EQ(cb[3], 'a');
    EXPECT_EQ(cb[4], 'b');
    EXPECT_EQ(cb[5], 'b');
    EXPECT_EQ(cb.capacity(), 6);
    EXPECT_EQ(cb.reserve(), 0);
}

TEST(CircularBufferChangeOfSizeTest, lessResizeTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.resize(2,'b');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb.capacity(), 2);
    EXPECT_EQ(cb.reserve(), 0);
}

TEST(CircularBufferChangeOfSizeTest, eqResizeTest) {
    CircularBuffer cb{ 4, 'a' };
    cb.resize(4, 'b');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 'a');
    EXPECT_EQ(cb[3], 'a');
    EXPECT_EQ(cb.capacity(), 4);
    EXPECT_EQ(cb.reserve(), 0);
}

TEST(CircularBufferChangeOfSizeTest, notFullResizeTest) {
    CircularBuffer cb{ 4 };
    cb.pushBack('a');
    cb.pushBack('a');
    cb.pushBack('a');
    cb.resize(6, 'b');
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'a');
    EXPECT_EQ(cb[2], 'a');
    EXPECT_EQ(cb[3], 'b');
    EXPECT_EQ(cb[4], 'b');
    EXPECT_EQ(cb[5], 0);
    EXPECT_EQ(cb.capacity(), 6);
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

TEST(CircularBufferSwapTest, swapInCBTest) {
    CircularBuffer cb{ 2};
    cb.pushBack('a');
    cb.pushBack('b');
    cb.swapInBuffer(0,1);
    EXPECT_EQ(cb[0], 'b');
    EXPECT_EQ(cb[1], 'a');
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
    cb1.pushBack('a');
    cb1.pushBack('b');
    cb = cb1;
    EXPECT_EQ(cb.capacity(), 4);
    EXPECT_EQ(cb.reserve(), 0);
    EXPECT_EQ(cb.startIndex(), 0);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'a');
    EXPECT_EQ(cb[3], 'b');
}

TEST(CircularBufferOperatorTest, eqNulFullTest) {
    CircularBuffer cb{ 2,'a' };
    CircularBuffer cb1{ 4 };
    cb1.pushBack('a');
    cb1.pushBack('b');
    cb1.pushBack('a');
    cb = cb1;
    EXPECT_EQ(cb.capacity(), 4);
    EXPECT_EQ(cb.reserve(), 1);
    EXPECT_EQ(cb.startIndex(), 0);
    EXPECT_EQ(cb[0], 'a');
    EXPECT_EQ(cb[1], 'b');
    EXPECT_EQ(cb[2], 'a');
    EXPECT_EQ(cb[3], 0);
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
    EXPECT_FALSE(eq);
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
    EXPECT_TRUE(notEq);
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
    EXPECT_ANY_THROW(cb.at(-1), 'a');
}

TEST(CircularBufferThrowTest, atMoreOutOfRangeTest) {
    CircularBuffer cb{ 2 };
    EXPECT_ANY_THROW(cb.at(2), 'a');
}

TEST(CircularBufferThrowTest, atLessOutOfRangeConstTest) {
    const CircularBuffer cb{ 2 };
    EXPECT_ANY_THROW(cb.at(-1), 'a');
}

TEST(CircularBufferThrowTest, atMoreOutOfRangeConstTest) {
    const CircularBuffer cb{ 2 };
    EXPECT_ANY_THROW(cb.at(2), 'a');
}

TEST(CircularBufferThrowTest, rotateNotFullTest) {
    CircularBuffer cb{ 2 };
    EXPECT_ANY_THROW(cb.rotate(1));
}

TEST(CircularBufferThrowTest, rotateLessOutOfRangeTest) {
    CircularBuffer cb{ 2 };
    EXPECT_ANY_THROW(cb.rotate(-1));
}

TEST(CircularBufferThrowTest, rotateMoreOutOfRangeTest) {
    CircularBuffer cb{ 2 };
    EXPECT_ANY_THROW(cb.rotate(3));
}

TEST(CircularBufferThrowTest, swapNotEqSizeTest) {
    CircularBuffer cb{ 2,'a'};
    CircularBuffer cb1{ 3,'b' };
    EXPECT_ANY_THROW(cb.swap(cb1));
}

TEST(CircularBufferThrowTest, eraseLessOutOfRangeTest) {
    CircularBuffer cb{ 5,'a' };
    EXPECT_ANY_THROW(cb.erase(-1, 5));
}

TEST(CircularBufferThrowTest, eraseMoreOutOfRangeTest) {
    CircularBuffer cb{ 5,'a' };
    EXPECT_ANY_THROW(cb.erase(3, 12));
}

int main(int argc, char** argv) {
    ::testing::InitGoogleTest(&argc, argv);

    return RUN_ALL_TESTS();
}
