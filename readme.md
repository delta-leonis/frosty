# frosty

## Description
Yet another gamepad library for Java, based on SDL.

## Preparation
The following packages are required:
- [GCC](https://gcc.gnu.org/install/binaries.html)
- [CMake](https://cmake.org/)
- [SDL (Simple Directmedia Layer) 2](https://www.libsdl.org/download-2.0.php)
- [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

## Building
To build for library usage, run the following inside the shell (assuming you are working in the project directory):

```Shell
cd src/native/cpp
cmake .
make
cd -
javac -d ./build src/main/java/io/leonis/frosty/*.java
cd build
jar -cvf frosty.jar io/leonis/frosty/GamePad*.java
```

Or build and run the example:

```Shell
cd src/native/cpp
cmake .
make
cd -
javac -d ./build src/*/java/io/leonis/frosty/*.java
cd build
jar -cvfe frosty.jar io/leonis/frosty/Main ./**
java -jar build/frosty.jar
```

## Contributors
- Mark Lefering
- Ryan Meulenkamp
