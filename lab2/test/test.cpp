#include <cell/cell.h>
#include <gameController/gameController.h>
#include <gameState/gameState.h>
#include <viewer/viewer.h>
#include <gameBoard/gameBoard.h>
#include <gtest/gtest.h>
#include <iostream>
#include <fstream>
#include <string>

TEST(cell, constrFullArgsTest) {
    Cell cell{ 1, 2, 0 };
    EXPECT_EQ(cell.Y(), 2);
    EXPECT_EQ(cell.X(), 1);
    EXPECT_EQ(cell.printValue(), 0);
    EXPECT_EQ(cell.needChangeStatus(), false);
}

TEST(cell, constrCordsArgsTest) {
    Cell cell{ 1, 2 };
    EXPECT_EQ(cell.Y(), 2);
    EXPECT_EQ(cell.X(), 1);
    EXPECT_EQ(cell.printValue(), 0);
    EXPECT_EQ(cell.needChangeStatus(), false);
}

TEST(cell, constrNotArgsTest) {
    Cell cell{};
    EXPECT_EQ(cell.Y(), 0);
    EXPECT_EQ(cell.X(), 0);
    EXPECT_EQ(cell.printValue(), 0);
    EXPECT_EQ(cell.needChangeStatus(), false);
}

TEST(cell, markChangeStatusTest) {
    Cell cell{ 1, 2, 0 };
    cell.markChangeStatus();
    EXPECT_EQ(cell.printValue(), 0);
    EXPECT_EQ(cell.needChangeStatus(), true);
}

TEST(cell, changeStatusTest) {
    Cell cell{ 1, 2, 0 };
    cell.changeStatus();
    EXPECT_EQ(cell.printValue(), 1);
    EXPECT_EQ(cell.needChangeStatus(), false);
}

TEST(gameState, constrOnlineStandartTest) {
    std::ofstream file("in.txt");
    if (file.is_open()) {
        file << "#Life 1.06" << std::endl;
        file << "#N Test universe" << std::endl;
        file << "#R B3 / S23" << std::endl;
        file << "#S 3 / 3" << std::endl;
        file << "0 1" << std::endl;
        file << "0 0" << std::endl;
        file << "1 0" << std::endl;
        file.close();
    }
    else {
        std::cout << "failed open file" << std::endl;
    }
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    arguments.push_back("in.txt");
    Game gameState( arguments );
    std::vector<int> countForSafeTest;
    std::vector<int> countForBornTest;
    countForSafeTest.push_back(2);
    countForSafeTest.push_back(3);
    countForBornTest.push_back(3);
    EXPECT_EQ(gameState.gameMode(), "online");
    EXPECT_EQ(gameState.needForBorn(), countForBornTest);
    EXPECT_EQ(gameState.needForSafe(), countForSafeTest);
    EXPECT_EQ(gameState.returnBoard().gameName(), "Test universe");
    EXPECT_EQ(gameState.gameOutFile(), "none");
    EXPECT_EQ(gameState.returnBoard().gameSizeX(), 3);
    EXPECT_EQ(gameState.returnBoard().gameSizeY(), 3);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(0 , 0)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(0, 1)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(0, 2)].printValue(), 0);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(1, 0)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(1, 1)].printValue(), 0);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(1, 2)].printValue(), 0);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(2, 0)].printValue(), 0);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(2, 1)].printValue(), 0);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(2, 2)].printValue(), 0);
}

TEST(gameState, constrOnlineNonDataTest) {

    std::ofstream file("in.txt");
    if (file.is_open()) {
        file << "#Life 1.06" << std::endl;
        file << "#S 2 / 2" << std::endl;
        file << "0 0" << std::endl;
        file << "0 0" << std::endl;
        file << "0 0" << std::endl;
        file.close();
    }
    else {
        std::cout << "failed open file" << std::endl;
    }
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    arguments.push_back("in.txt");
    Game gameState(arguments);
    std::vector<int> countForSafeTest;
    std::vector<int> countForBornTest;
    countForSafeTest.push_back(3);
    countForSafeTest.push_back(4);
    countForBornTest.push_back(1);
    countForBornTest.push_back(2);
    EXPECT_EQ(gameState.gameMode(), "online");
    EXPECT_EQ(gameState.returnBoard().gameName(), "live");
    EXPECT_EQ(gameState.gameOutFile(), "none");
    EXPECT_EQ(gameState.needForBorn(), countForBornTest);
    EXPECT_EQ(gameState.needForSafe(), countForSafeTest);
    EXPECT_EQ(gameState.returnBoard().gameSizeX(), 2);
    EXPECT_EQ(gameState.returnBoard().gameSizeY(), 2);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(0, 0)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(0, 1)].printValue(), 0);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(1, 0)].printValue(), 0);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(1, 1)].printValue(), 0);
}

TEST(gameState, constrOnlineNonFileTest) {
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    Game gameState(arguments);
    std::vector<int> countForSafeTest;
    std::vector<int> countForBornTest;
    countForSafeTest.push_back(3);
    countForSafeTest.push_back(4);
    countForBornTest.push_back(1);
    countForBornTest.push_back(2);
    EXPECT_EQ(gameState.gameMode(), "online");
    EXPECT_EQ(gameState.returnBoard().gameName(), "live");
    EXPECT_EQ(gameState.needForBorn(), countForBornTest);
    EXPECT_EQ(gameState.needForSafe(), countForSafeTest);
    EXPECT_EQ(gameState.gameOutFile(), "none");
    EXPECT_EQ(gameState.returnBoard().gameSizeX(), 2);
    EXPECT_EQ(gameState.returnBoard().gameSizeY(), 2);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(0, 0)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(0, 1)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(1, 0)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(1, 1)].printValue(), 1);
}

TEST(gameState, constrOfflineShortTest) {
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    arguments.push_back("-i");
    arguments.push_back("12");
    arguments.push_back("-o");
    arguments.push_back("out.txt");
    Game gameState(arguments);
    std::vector<int> countForSafeTest;
    std::vector<int> countForBornTest;
    countForSafeTest.push_back(3);
    countForSafeTest.push_back(4);
    countForBornTest.push_back(1);
    countForBornTest.push_back(2);
    EXPECT_EQ(gameState.gameMode(), "offline");
    EXPECT_EQ(gameState.returnBoard().gameName(), "live");
    EXPECT_EQ(gameState.step(), 12);
    EXPECT_EQ(gameState.gameOutFile(), "out.txt");
    EXPECT_EQ(gameState.needForBorn(), countForBornTest);
    EXPECT_EQ(gameState.needForSafe(), countForSafeTest);
    EXPECT_EQ(gameState.returnBoard().gameSizeX(), 2);
    EXPECT_EQ(gameState.returnBoard().gameSizeY(), 2);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(0, 0)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(0, 1)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(1, 0)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(1, 1)].printValue(), 1);
}

TEST(gameState, constrOfflineLongTest) {
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    arguments.push_back("--iterations=12");
    arguments.push_back("--output=out.txt");
    Game gameState(arguments);
    std::vector<int> countForSafeTest;
    std::vector<int> countForBornTest;
    countForSafeTest.push_back(3);
    countForSafeTest.push_back(4);
    countForBornTest.push_back(1);
    countForBornTest.push_back(2);
    EXPECT_EQ(gameState.gameMode(), "offline");
    EXPECT_EQ(gameState.returnBoard().gameName(), "live");
    EXPECT_EQ(gameState.step(), 12);
    EXPECT_EQ(gameState.gameOutFile(), "out.txt");
    EXPECT_EQ(gameState.needForBorn(), countForBornTest);
    EXPECT_EQ(gameState.needForSafe(), countForSafeTest);
    EXPECT_EQ(gameState.returnBoard().gameSizeX(), 2);
    EXPECT_EQ(gameState.returnBoard().gameSizeY(), 2);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(0, 0)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(0, 1)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(1, 0)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(1, 1)].printValue(), 1);
}

TEST(gameController, tickTest) {
    std::ofstream file("in.txt");
    if (file.is_open()) {
        file << "#Life 1.06" << std::endl;
        file << "#N Test universe" << std::endl;
        file << "#R B3 / S23" << std::endl;
        file << "#S 3 / 3" << std::endl;
        file << "0 1" << std::endl;
        file << "0 0" << std::endl;
        file << "1 0" << std::endl;
        file.close();
    }
    else {
        std::cout << "failed open file" << std::endl;
    }
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    arguments.push_back("in.txt");
    arguments.push_back("--iterations=1");
    arguments.push_back("--output=out.txt");
    Game gameState(arguments);
    Viewer viewer{ gameState.returnBoard(), gameState.gameMode()};
    GameController controller{ gameState, viewer };
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(0, 0)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(0, 1)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(0, 2)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(1, 0)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(1, 1)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(1, 2)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(2, 0)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(2, 1)].printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().gameBoard()[gameState.returnBoard().realIndex(2, 2)].printValue(), 1);
}

int main(int argc, char** argv) {
    ::testing::InitGoogleTest(&argc, argv);

    return RUN_ALL_TESTS();
}
