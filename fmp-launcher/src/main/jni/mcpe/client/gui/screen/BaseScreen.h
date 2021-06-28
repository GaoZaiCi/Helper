#pragma once

#include <memory>

#include "AbstractScreen.h"
class AbstractScreenSetupCleanupStrategy;

class BaseScreen : public AbstractScreen
{
public:
	//void** vtable; // 0
	std::unique_ptr<AbstractScreenSetupCleanupStrategy> cleanupStrategy; // 4
public:
	virtual void tick(int, int);
	virtual void getEyeRenderingMode() const;
	virtual std::string getScreenName() const;
	virtual std::string getScreenNameW() const;
	virtual void toGUICoordinate(int, int, int&, int&);
public:
	BaseScreen();
	void setupForRendering(ScreenContext&);
	void cleanupForRendering(ScreenContext&);
	void setScreenSetupCleanup(std::unique_ptr<AbstractScreenSetupCleanupStrategy, std::default_delete<AbstractScreenSetupCleanupStrategy> >);
};
