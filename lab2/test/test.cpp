#include <cell/cell.h>
#include <gameController/gameController.h>
#include <gameState/gameState.h>
#include <viewer/viewer.h>
#include <gameBoard/gameBoard.h>
#include <inputParser/inputParser.h>
#include <gtest/gtest.h>
#include <iostream>
#include <fstream>
#include <string>
#include <bitset>

TEST(cell, constrFullArgsTest) {
    Cell cell{ 0 };
    EXPECT_EQ(cell.printValue(), 0);
    EXPECT_EQ(cell.needChangeStatus(), false);
}

TEST(cell, constrCordsArgsTest) {
    Cell cell{ };
    EXPECT_EQ(cell.printValue(), 0);
    EXPECT_EQ(cell.needChangeStatus(), false);
}

TEST(cell, markChangeStatusTest) {
    Cell cell{ 0 };
    cell.markChangeStatus();
    EXPECT_EQ(cell.printValue(), 0);
    EXPECT_EQ(cell.needChangeStatus(), true);
}

TEST(cell, changeStatusTest) {
    Cell cell{ 0 };
    cell.changeStatus();
    EXPECT_EQ(cell.printValue(), 1);
    EXPECT_EQ(cell.needChangeStatus(), false);
}

TEST(inputParser, inFileTest) {
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    arguments.push_back("in.txt");
    arguments.push_back("-i");
    arguments.push_back("12");
    arguments.push_back("-o");
    arguments.push_back("out.txt");
    InputParser inputParser{ arguments };
    EXPECT_EQ(inputParser.inputFile(), "in.txt");
}

TEST(inputParser, stepTest) {
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    arguments.push_back("-i");
    arguments.push_back("12");
    arguments.push_back("-o");
    arguments.push_back("out.txt");
    InputParser inputParser{ arguments };
    EXPECT_EQ(inputParser.step(), 12);
}

TEST(inputParser, outFileTest) {
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    arguments.push_back("-i");
    arguments.push_back("12");
    arguments.push_back("-o");
    arguments.push_back("out.txt");
    InputParser inputParser{ arguments };
    EXPECT_EQ(inputParser.gameOutFile(), "out.txt");
}

TEST(inputParser, onlineTest) {
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    arguments.push_back("in.txt");
    InputParser inputParser{ arguments };
    EXPECT_EQ(inputParser.gameMode(), "online");
}

TEST(inputParser, offlineTest) {
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    arguments.push_back("-i");
    arguments.push_back("12");
    arguments.push_back("-o");
    arguments.push_back("out.txt");
    InputParser inputParser{ arguments };
    EXPECT_EQ(inputParser.gameMode(), "offline");
}

TEST(inputParser, noOutFileTest) {
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    arguments.push_back("in.txt");
    InputParser inputParser{ arguments };
    EXPECT_EQ(inputParser.gameOutFile(), "none");
}

TEST(inputParser, offlineLongTest) {
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    arguments.push_back("--iterations=12");
    arguments.push_back("--output=out.txt");
    InputParser inputParser{ arguments };
    EXPECT_EQ(inputParser.gameMode(), "offline");
    EXPECT_EQ(inputParser.step(), 12);
    EXPECT_EQ(inputParser.gameOutFile(), "out.txt");
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
    InputParser inputParser{ arguments };
    Game gameState{ inputParser.inputFile() };
    std::bitset<8> countForSafeTest;
    std::bitset<8> countForBornTest;
    countForSafeTest.set(2);
    countForSafeTest.set(3);
    countForBornTest.set(3);
    EXPECT_EQ(gameState.needForBorn(), countForBornTest);
    EXPECT_EQ(gameState.needForSafe(), countForSafeTest);
    EXPECT_EQ(gameState.returnBoard().gameName(), "Test universe");
    EXPECT_EQ(gameState.returnBoard().gameSizeX(), 3);
    EXPECT_EQ(gameState.returnBoard().gameSizeY(), 3);
    EXPECT_EQ(gameState.returnBoard().returnCell(0,0).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(0, 1).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(0, 2).printValue(), 0);
    EXPECT_EQ(gameState.returnBoard().returnCell(1, 0).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(1, 1).printValue(), 0);
    EXPECT_EQ(gameState.returnBoard().returnCell(1, 2).printValue(), 0);
    EXPECT_EQ(gameState.returnBoard().returnCell(2, 0).printValue(), 0);
    EXPECT_EQ(gameState.returnBoard().returnCell(2, 1).printValue(), 0);
    EXPECT_EQ(gameState.returnBoard().returnCell(2, 1).printValue(), 0);
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
    InputParser inputParser{ arguments };
    Game gameState{ inputParser.inputFile() };
    std::bitset<8> countForSafeTest;
    std::bitset<8> countForBornTest;
    countForSafeTest.set(3);
    countForSafeTest.set(4);
    countForBornTest.set(1);
    countForBornTest.set(2);
    EXPECT_EQ(gameState.returnBoard().gameName(), "live");
    EXPECT_EQ(gameState.needForBorn(), countForBornTest);
    EXPECT_EQ(gameState.needForSafe(), countForSafeTest);
    EXPECT_EQ(gameState.returnBoard().gameSizeX(), 2);
    EXPECT_EQ(gameState.returnBoard().gameSizeY(), 2);
    EXPECT_EQ(gameState.returnBoard().returnCell(0, 0).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(0, 1).printValue(), 0);
    EXPECT_EQ(gameState.returnBoard().returnCell(1, 0).printValue(), 0);
    EXPECT_EQ(gameState.returnBoard().returnCell(1, 1).printValue(), 0);
}

TEST(gameState, constrOnlineNonFileTest) {
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    InputParser inputParser{ arguments };
    Game gameState{ inputParser.inputFile() };
    std::bitset<8> countForSafeTest;
    std::bitset<8> countForBornTest;
    countForSafeTest.set(1);
    countForSafeTest.set(2);
    countForBornTest.set(3);
    countForBornTest.set(4);
    EXPECT_EQ(gameState.returnBoard().gameName(), "live");
    EXPECT_EQ(gameState.needForBorn(), countForBornTest);
    EXPECT_EQ(gameState.needForSafe(), countForSafeTest);
    EXPECT_EQ(gameState.returnBoard().gameSizeX(), 2);
    EXPECT_EQ(gameState.returnBoard().gameSizeY(), 2);
    EXPECT_EQ(gameState.returnBoard().returnCell(0, 0).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(0, 1).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(1, 0).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(1, 1).printValue(), 1);
}

TEST(gameState, constrOfflineShortTest) {
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    arguments.push_back("-i");
    arguments.push_back("12");
    arguments.push_back("-o");
    arguments.push_back("out.txt");
    InputParser inputParser{ arguments };
    Game gameState{ inputParser.inputFile() };
    std::bitset<8> countForSafeTest;
    std::bitset<8> countForBornTest;
    countForSafeTest.set(1);
    countForSafeTest.set(2);
    countForBornTest.set(3);
    countForBornTest.set(4);
    EXPECT_EQ(gameState.returnBoard().gameName(), "live");
    EXPECT_EQ(gameState.needForBorn(), countForBornTest);
    EXPECT_EQ(gameState.needForSafe(), countForSafeTest);
    EXPECT_EQ(gameState.returnBoard().gameSizeX(), 2);
    EXPECT_EQ(gameState.returnBoard().gameSizeY(), 2);
    EXPECT_EQ(gameState.returnBoard().returnCell(0, 0).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(0, 1).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(1, 0).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(1, 1).printValue(), 1);
}

TEST(gameState, constrOfflineLongTest) {
    std::vector<std::string> arguments;
    arguments.push_back("skip");
    arguments.push_back("--iterations=12");
    arguments.push_back("--output=out.txt");
    InputParser inputParser{ arguments };
    Game gameState{ inputParser.inputFile() };
    std::bitset<8> countForSafeTest;
    std::bitset<8> countForBornTest;
    countForSafeTest.set(1);
    countForSafeTest.set(2);
    countForBornTest.set(3);
    countForBornTest.set(4);
    EXPECT_EQ(gameState.returnBoard().gameName(), "live");
    EXPECT_EQ(gameState.needForBorn(), countForBornTest);
    EXPECT_EQ(gameState.needForSafe(), countForSafeTest);
    EXPECT_EQ(gameState.returnBoard().gameSizeX(), 2);
    EXPECT_EQ(gameState.returnBoard().gameSizeY(), 2);
    EXPECT_EQ(gameState.returnBoard().returnCell(0, 0).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(0, 1).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(1, 0).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(1, 1).printValue(), 1);
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
    InputParser inputParser{ arguments };
    Game gameState{ inputParser.inputFile() };
    Viewer viewer{ gameState.returnBoard(), inputParser.gameMode()};
    GameController controller{ inputParser, gameState, viewer };
    EXPECT_EQ(gameState.returnBoard().returnCell(0, 0).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(0, 1).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(0, 2).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(1, 0).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(1, 1).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(1, 2).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(2, 0).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(2, 1).printValue(), 1);
    EXPECT_EQ(gameState.returnBoard().returnCell(2, 1).printValue(), 1);
}

int main(int argc, char** argv) {
    ::testing::InitGoogleTest(&argc, argv);

    return RUN_ALL_TESTS();
}
