#pragma once

class Cell {
	int x;
	int y;
	int cellValue;
	bool isChange;
public:
	Cell(int xCord, int yCord, int value);
	Cell(int xCord, int yCord);
	Cell();
	const int printValue() const;
	const int X() const;
	const int Y() const;
	void changeStatus();
	const bool needChangeStatus() const;
	void markChangeStatus();
};