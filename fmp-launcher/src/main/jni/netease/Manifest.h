//
// Created by Gao在此 on 2020/4/25.
//

#pragma once

class Manifest{
  public:
    void parse(std::string const&);
    std::string getVersion()const;
    std::string getEngineVersion();
    bool versionEquals(std::string const&)const;
};