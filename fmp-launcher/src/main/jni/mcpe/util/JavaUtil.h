#pragma once

#include <string>
#include <jni.h>

class JavaUtil
{
public:
	static void setXblInviteJson(std::string);
	static std::string getXblInviteJson();
	static void setActivityObject(_jobject*);
	static _jobject* getActivityObject();
	static void setVM(_JavaVM*);
	static _JavaVM* getVM();
};
