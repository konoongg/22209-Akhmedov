#include "cell.h"

Cell::Cell(int value) {
	cellValue = value;
	isChange = false;
}

Cell::Cell() {
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

void Cell::markChangeStatus() {
	isChange = true;
}

const bool Cell::needChangeStatus() const  {
	return isChange;
}