cmake_minimum_required(VERSION 3.23)
project(lab4)

set(EXECUTABLE_OUTPUT_PATH "${CMAKE_CURRENT_SOURCE_DIR}/bin")

add_executable(lab4 
    src/main.cpp
    src/consoleParser/consoleParser.cpp
    src/consoleParser/consoleParser.h
    src/CSVParser/CSVParser.h
    src/exception/wrongColumn/wrongColumn.cpp
    src/exception/wrongColumn/wrongColumn.h
    src/exception/wrongConsoleParam/wrongConsoleParam.cpp
    src/exception/wrongConsoleParam/wrongConsoleParam.h
    src/exception/wrongRow/wrongRow.cpp
    src/exception/wrongRow/wrongRow.h
)
