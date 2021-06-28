package com.fmp.core;

import com.fmp.Logger;
import com.fmp.core.http.bean.HelperAccount;
import com.fmp.core.http.bean.HelperPlugin;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class GamePluginManager {
    private static GamePluginManager gamePluginManager;
    private List<HelperPlugin> allMods = new ArrayList<>();

    public GamePluginManager() {
        refreshAllPlugin(null);
    }

    public static GamePluginManager getInstance() {
        if (gamePluginManager == null) {
            gamePluginManager = new GamePluginManager();
        }
        return gamePluginManager;
    }

    public File getModFilesDir() {
        return HelperNative.getApplication().getExternalFilesDir("Mod");
    }

    Integer getModKey(String name) {
        HelperAccount userData = HelperCore.getInstance().getUserData();
        if (userData != null) {
            for (HelperPlugin plugin : allMods) {
                if (plugin.name.equals(name)) {
                    for (int id:userData.gameMods){
                        if (id==plugin.id || id==-1){
                            return plugin.key;
                        }
                    }
                }
            }
        }
        return 50;
    }

    public List<HelperPlugin> getAllMods() {
        return allMods;
    }

    public void refreshAllPlugin(onRefreshPlugin refreshPlugin) {
        /*BmobQuery<FMP_USER_MOD> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<FMP_USER_MOD>() {
            @Override
            public void done(List<FMP_USER_MOD> list, BmobException e) {
                if (e == null) {
                    allMods.clear();
                    allMods.addAll(list);
                    listSort(allMods);
                    if (refreshPlugin != null)
                        refreshPlugin.onRefresh(null, allMods);
                } else {
                    if (refreshPlugin != null)
                        refreshPlugin.onRefresh(new CoreException(e), null);
                }
            }
        });*/
        BmobQuery query = new BmobQuery(HelperPlugin.CLASS_TABLE_NAME);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    allMods.clear();
                    for (int i=0,len=jsonArray.length();len>i;i++){
                        try {
                            HelperPlugin plugin=new HelperPlugin(new HelperJSONObject(jsonArray.getJSONObject(i).toString()));
                            if (plugin.enable)
                                allMods.add(plugin);
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                    listSort(allMods);
                    if (refreshPlugin != null)
                        refreshPlugin.onRefresh(null, allMods);
                } else {
                    if (refreshPlugin != null)
                        refreshPlugin.onRefresh(new CoreException(e), null);
                }
            }
        });
    }

    private void listSort(List<HelperPlugin> list) {
        Collections.sort(list, (item1, item2) -> {
            try {
                int id1 = item1.position;
                int id2 = item2.position;
                if (id1 > id2) {
                    return 1;
                } else {
                    return -1;
                }
            } catch (NullPointerException e) {
                return 0;
            }
        });
    }

    public interface onRefreshPlugin {
        void onRefresh(CoreException e, List<HelperPlugin> modList);
    }
}
