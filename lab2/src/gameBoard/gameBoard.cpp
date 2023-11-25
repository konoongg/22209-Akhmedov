#include "../gameBoard/gameBoard.h"
#include "../cell/cell.h"
#include <iostream>
#include <vector>

GameBoard::GameBoard() {
	this->name = "standart";
	this->sizeX = 3;
	this->sizeY = 4;
	board.resize(sizeX * sizeY);
	for (int i = 0; i < sizeX * sizeY; ++i) {
		board[i] = Cell{  };
	}
}

GameBoard::GameBoard(int sizeX, int sizeY, std::string name) {
	this->name = name;
	this->sizeX = sizeX;
	this->sizeY = sizeY;
	board.resize(sizeX * sizeY);
	for (int i = 0; i < sizeX * sizeY; ++i) {
		board[i] = Cell{ };
	}
}

const int GameBoard::realIndex(int x, int y) const {
	return (y * sizeX) + x;
}

const Cell&  GameBoard::returnCell(int x, int y) const{
	return board[realIndex((x + sizeX) % sizeX, (y + sizeY) % sizeY)];
	
}

 Cell& GameBoard::returnCell(int x, int y) {
	 return board[realIndex((x + sizeX) % sizeX, (y + sizeY) % sizeY)];
}

const int GameBoard::gameSizeX() const {
	return sizeX;
}

const int GameBoard::gameSizeY() const {
	return sizeY;
}

const std::string GameBoard::gameName() const {
	return name;
}
