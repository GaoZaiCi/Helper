//
// Created by Gao在此 on 2020/3/18.
//
#pragma once
class ReactNativeMgr{
public:
    void rnJscallCpp(std::string const&);
    void rnJscallPython(JNIEnv *, jobject *, jstring *, jobject *);
};
