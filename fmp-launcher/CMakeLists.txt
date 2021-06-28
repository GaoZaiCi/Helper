# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
#CMakeLists.txt
cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

#1.配置第三方so库.h头文件路径
#include_directories(src/main/jni/include)
#include_directories(src/main/jni/minecraftpe)
#include_directories(src/main/jni/netease)

#指定so库输出路径
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI})
#添加第三方库
add_library(minecraftpe SHARED IMPORTED)
#添加库的路径
set_target_properties(minecraftpe
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/src/main/jni/include/libminecraftpe.so)



#网易支持链接库
add_library( # Sets the name of the library.
        # 设置so文件名称.
        netease-support

        # Sets the library as a shared library.
        SHARED
        # 设置这个so文件为共享.

        # Provides a relative path to your source file(s).
        # 设置这个so文件为共享.
        src/main/jni/netease-support.cpp
        src/main/jni/fmp-launcher.cpp
        src/main/jni/FileUtil.cpp
        src/main/jni/md5/md5.cpp
        src/main/jni/md5/md5file.cpp
        src/main/jni/substrate/hde64.c
        src/main/jni/substrate/SubstrateDebug.cpp
        src/main/jni/substrate/SubstrateHook.cpp
        src/main/jni/substrate/SubstratePosixMemory.cpp
        src/main/jni/elfhook/elfhook.cpp
        src/main/jni/elfhook/elfio.cpp
        src/main/jni/elfhook/elfutils.cpp
        src/main/jni/inlinehook/inlineHook.c
        src/main/jni/inlinehook/relocate.c
        )


# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib
        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.


target_link_libraries( # Specifies the target library.
        # 制定目标库.
        netease-support
        minecraftpe
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
