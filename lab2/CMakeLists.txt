cmake_minimum_required(VERSION 3.23)
project(lab2)

set(EXECUTABLE_OUTPUT_PATH "${CMAKE_CURRENT_SOURCE_DIR}/bin")
add_subdirectory(test/googletest)  
add_subdirectory(src/gameController gameController)

add_executable(lab2 
    src/main.cpp 
    src/cell/cell.cpp
    src/gameController/gameController.cpp
    src/gameState/gameState.cpp
    src/viewer/viewer.cpp
    src/cell/cell.h
    src/gameController/gameController.h
    src/gameState/gameState.h
    src/viewer/viewer.h
    src/gameBoard/gameBoard.cpp 
    src/gameBoard/gameBoard.h
    src/inputParser/inputParser.cpp
    src/inputParser/inputParser.h)
