#pragma once

class Cell {
	int cellValue;
	bool isChange;
public:
	Cell(int value);
	Cell();
	const int printValue() const;
	void changeStatus();
	const bool needChangeStatus() const;
	void markChangeStatus();
};