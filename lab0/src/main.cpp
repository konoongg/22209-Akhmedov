#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <list>
#include <map>
#include <algorithm>

struct TWordStatistics {
	std::string word;
	int count;
};

class WordCounter {
	std::list<TWordStatistics> wordStatistics;
	std::map<std::string, int> wordCount;
	int countAllWord = 0;

public:
	void ReadFromFile(std::string fileName) {
		std::ifstream inputFile(fileName);
		std::string line;
		while (std::getline(inputFile, line)) {
			for (int i = 0; i < line.length(); ++i) {
				if (!((45 <= line[i] && line[i] <= 57) || (65 <= line[i] && line[i] <= 90) || (97 <= line[i] && line[i] <= 122))) {
					line[i] = ' ';
				}
			}
			std::istringstream wordInLine(line);
			std::string word;
			while (wordInLine >> word) {
				countAllWord++;
				if (wordCount.count(word) > 0) {
					wordCount[word]++;
				}
				else {
					wordCount[word] = 1;
				}
			}
		}
		inputFile.close();
	}

	void SortCountWord() {
		for (auto& pair : wordCount) {
			wordStatistics.push_back({pair.first, pair.second});
		}
		wordStatistics.sort([](const TWordStatistics& w1, const TWordStatistics& w2) {return w1.count > w2.count; });
	}

	void WriteCSV(std::string fileName) {
		std::ofstream outputFile(fileName);
		outputFile << "word" << "," << "count" << "," << "frequency" << std::endl;
		for (auto& word : wordStatistics) {
			outputFile << word.word << "," << word.count << "," << double(word.count)/ countAllWord << std::endl;
		}
		outputFile.close();
	}
};

int main(int argc, char* argv[]) {
	WordCounter wCouner;
	wCouner.ReadFromFile(argv[1]);
	wCouner.SortCountWord();
	wCouner.WriteCSV(argv[2]);
	return 0;
}