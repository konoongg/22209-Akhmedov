cmake_minimum_required(VERSION 3.23)
project(lab4)

set(EXECUTABLE_OUTPUT_PATH "${CMAKE_CURRENT_SOURCE_DIR}/bin")

file(GLOB_RECURSE SOURCES "src/*.cpp" "src/*.h")

add_executable(lab4 ${SOURCES})