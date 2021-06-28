#pragma once

#include <memory>
#include <string>

class LevelData;
class ChunkSource;
class Player;
typedef int StorageVersion;

class LevelStorage {
public:	
	virtual ~LevelStorage();
	virtual bool loadLevelData(LevelData&) = 0;
	virtual std::string getFullPath() const = 0;
	virtual void savePlayerData(const std::string&, std::string&&) = 0;
};
