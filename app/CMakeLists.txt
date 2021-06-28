# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
#CMakeLists.txt
cmake_minimum_required(VERSION 3.4.1)


# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

#指定so库输出路径
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY
        ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI})

#FMP链接库
add_library(
        # 设置so文件名称
        fmp-jni
        # 设置这个so文件为共享
        SHARED
        # 添加源文件
        src/main/jni/fmp-jni.cpp
        src/main/jni/FileUtil.cpp
        src/main/jni/Base64.cpp
        )


#多玩支持链接库
add_library(
        # 设置so文件名称
        duowan-support
        # 设置这个so文件为共享
        SHARED
        # 添加源文件
        src/main/jni/duowan/duowan-support.cpp)

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
        fmp-jni

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        # 制定目标库.
        duowan-support

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})


