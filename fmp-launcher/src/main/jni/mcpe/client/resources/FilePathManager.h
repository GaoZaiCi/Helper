#pragma once

#include <string>

class FilePathManager
{
public:
	FilePathManager(std::string, bool);
	~FilePathManager();
public:
	void setDataUrl(std::string);
	void setRootPath(std::string);
	void setPackagePath(std::string);
	void setSettingsPath(std::string);
	void setTemporaryFilePath(std::string);
	void setExternalFilePath(std::string);
public:
	std::string getDataUrl() const;
	std::string getPackagePath() const;
	std::string getSettingsPath() const;
	std::string getUserDataPath() const;
	std::string getExternalFilePath() const;
	std::string getResourcePacksPath() const;
	std::string getHomePath() const;
	std::string getWorldsPath() const;
	std::string getLevelArchivePath() const;
	std::string getRootPath() const;
	std::string getTemporaryFilePath() const;
public:
	static std::string WORLDS_DIR;
	static std::string ARCHIVE_DIR;
	static std::string REALM_WORLDS_DIR;
	static std::string RESOURCE_PACKS_DIR;
	static std::string HOME_DIR;
	static std::string TEMP_DIR;
};
