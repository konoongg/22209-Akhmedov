#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <list>
#include <map>
#include <algorithm>

struct TWordStatistics {
	std::wstring word;
	int count;
};

class WordCounter {
	std::list<TWordStatistics> wordStatistics;
	std::map<std::wstring, int> wordCount;
	int countAllWord = 0;

public:
	void ReadFromFile(std::string fileName) {
		std::wifstream inputFile(fileName);
		std::wstring line;
		while (std::getline(inputFile, line)) {
			for (int i = 0; i < line.length(); ++i) {
				if (!(std::isalpha(line[i]) || std::isdigit(line[i]))) {
					line[i] = ' ';
				}
			}
			std::wistringstream wordInLine(line);
			std::wstring word;
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
		std::wofstream outputFile(fileName);
		outputFile << "word" << "," << "count" << "," << "frequency" << std::endl;
		for (auto& word : wordStatistics) {
			outputFile << word.word << "," << word.count << "," << double(word.count)/ countAllWord << std::endl;
		}
		outputFile.close();
	}
};

int main(int argc, char* argv[]) {
	if (argc < 2) {
		std::cerr << "need more argc";
		return 1;
	}
	setlocale(LC_ALL, "ru");
	WordCounter wCouner;
	wCouner.ReadFromFile(argv[1]);
	wCouner.SortCountWord();
	wCouner.WriteCSV(argv[2]);
	return 0;
}