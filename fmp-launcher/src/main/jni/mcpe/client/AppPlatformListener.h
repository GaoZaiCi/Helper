#pragma once

class PushNotificationMessage;

class AppPlatformListener
{
public:
	//void **vtable;	// 0
public:
	virtual void onLowMemory();
	virtual void onAppPaused();
	virtual void onAppSuspended();
	virtual void onAppResumed();
	virtual void onAppFocusLost();
	virtual void onAppFocusGained();
	virtual void onAppTerminated();
	virtual void onPushNotificationReceived(PushNotificationMessage const&);
public:
	AppPlatformListener(bool);
	AppPlatformListener();
	void initListener(float);
	void terminate();
};
