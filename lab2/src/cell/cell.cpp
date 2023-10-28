#include "cell.h"

Cell::Cell(int xCord, int yCord, int value) {
	x = xCord;
	y = yCord;
	cellValue = value;
	isChange = false;
}

Cell::Cell(int xCord, int yCord) {
	x = xCord;
	y = yCord;
	cellValue = 0;
	isChange = false;
}

Cell::Cell() {
	x = 0;
	y = 0;
	cellValue = 0;
	isChange = false;
}

void Cell::changeStatus() {
	isChange = false;
	cellValue += 1;
	cellValue %= 2;
}

const int Cell::printValue() const{
	return cellValue;
}

const int Cell::X() const{
	return x;
}

const int Cell::Y() const  {
	return y;
}
void Cell::markChangeStatus() {
	isChange = true;
}

const bool Cell::needChangeStatus() const  {
	return isChange;
}