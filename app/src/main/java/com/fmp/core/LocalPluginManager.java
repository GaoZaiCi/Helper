package com.fmp.core;

import com.fmp.core.http.bean.HelperAccount;
import com.fmp.core.http.bean.HelperPlugin;
import com.fmp.mods.PluginData;
import com.fmp.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalPluginManager extends GamePluginManager {
    private static LocalPluginManager localPluginManager;

    public static LocalPluginManager getInstance() {
        if (localPluginManager == null)
            localPluginManager = new LocalPluginManager();
        return localPluginManager;
    }

    /**
     * 判断是否为插件
     * @param name 插件名称
     * @return 状态
     */
    public static boolean isPlugin(String name) {
        return name.endsWith(".js") || name.endsWith(".modpkg") || name.endsWith(".fmod") || name.endsWith(".nmod");
    }

    /**
     * 刷新本地数据
     *
     * @return 返回列表
     */
    public List<PluginData.Item> refreshPlugin() {
        File[] localPlugins = getModFilesDir().listFiles();
        PluginData data = PluginData.getData();
        List<PluginData.Item> newList = new ArrayList<>();
        List<PluginData.Item> list = data.getList();
        List<HelperPlugin> allMods = GamePluginManager.getInstance().getAllMods();
        List<Integer> userMods = new ArrayList<>();
        HelperAccount userData = HelperCore.getInstance().getUserData();
        if (userData != null && userData.gameMods != null) {
            for (int id:userData.gameMods){
                userMods.add(new Integer(id));
            }
        }
        int newCount = 0;
        if (localPlugins != null && localPlugins.length != 0) {
            //遍历本地插件
            for (File plugin : localPlugins) {
                if (isPlugin(plugin.getName())) {
                    boolean bool = false;
                    //遍历下载状态
                    for (PluginData.Item item : list) {
                        if (plugin.getName().equals(item.getName())) {
                            newList.add(item);
                            bool = true;
                            //遍历授权状态
                            for (HelperPlugin helperPlugin : allMods) {
                                if (helperPlugin.name.equals(plugin.getName()) && (userMods.contains(helperPlugin.id) || userMods.contains(-1))) {
                                    item.setBuy(true);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    if (!bool) {
                        PluginData.Item item = data.getNewItem();
                        item.setName(plugin.getName());
                        item.setPath(plugin.getAbsolutePath());
                        item.setSize(plugin.length());
                        item.setMd5(getModMD5(plugin.getAbsolutePath()));
                        item.setPosition(list.size() + (newCount++));
                        newList.add(item);
                    }
                }
            }
        }
        listSort(newList);
        data.setList(newList);
        data.saveData();
        return newList;
    }

    /**
     * 更新插件加载位置
     *
     * @param oldItem 旧位置
     * @param newItem 新位置
     */
    public void updatePluginPosition(PluginData.Item oldItem, PluginData.Item newItem) {
        int oldPos = oldItem.getPosition(), newPos = newItem.getPosition();
        PluginData pluginData = PluginData.getData();
        List<PluginData.Item> list = pluginData.getList();
        //更新加载位置
        list.get(oldPos).setPosition(newPos);
        list.get(newPos).setPosition(oldPos);
        listSort(list);//重新排序
        pluginData.saveData();//保存数据
    }

    /**
     * 对插件列表进行排序（按位置）
     *
     * @param list 插件列表
     */
    private void listSort(List<PluginData.Item> list) {
        //Collections的sort方法默认是升序排列，如果需要降序排列时就需要重写compare方法
        Collections.sort(list, (item1, item2) -> {
            if (item1.getPosition() > item2.getPosition()) {
                return 1;
            } else if (item1.getPosition() < item2.getPosition()) {
                return -1;
            } else if (item1.getPosition() == item2.getPosition()) {
                item2.setPosition(item2.getPosition() + 1);
            }
            return 0;
        });
    }

    /**
     * 获取插件列表
     *
     * @return 本地插件列表
     */
    public List<PluginData.Item> getPluginList() {
        return refreshPlugin();
    }

    /**
     * 获取启用的插件列表
     *
     * @return 插件列表
     */
    public List<PluginData.Item> getEnablePluginList() {
        List<PluginData.Item> newList = new ArrayList<>();
        List<PluginData.Item> list = PluginData.getData().getList();
        for (PluginData.Item item : list) {
            if (item.isEnable()) {
                newList.add(item);
            }
        }
        return newList;
    }

    /**
     * 设置插件启用状态
     *
     * @param item 插件成员
     * @param enable 启用状态
     * @return 操作状态
     */
    public boolean setPluginEnable(PluginData.Item item, boolean enable) {
        PluginData pluginData = PluginData.getData();
        List<PluginData.Item> list = pluginData.getList();
        for (PluginData.Item mItem : list) {
            if (item.getName().equals(mItem.getName())) {
                mItem.setEnable(enable);
                return pluginData.saveData();
            }
        }
        return false;
    }

    public boolean removePlugin(PluginData.Item item) {
        PluginData pluginData = PluginData.getData();
        List<PluginData.Item> list = pluginData.getList();
        for (PluginData.Item mItem : list) {
            if (item.getName().equals(mItem.getName())) {
                list.remove(mItem);
                new File(mItem.getPath()).delete();
                return pluginData.saveData();
            }
        }
        return false;
    }

    /**
     * 添加插件文件
     *
     * @param path 路径
     * @return 操作状态
     * @throws PluginException 操作异常
     */
    public boolean addPluginFile(String path) throws PluginException {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            PluginData pluginData = PluginData.getData();
            List<PluginData.Item> list = pluginData.getList();
            for (PluginData.Item item : list) {
                if (item.getName().equals(file.getName())) {
                    throw new PluginException("重复的插件" + item.getName());
                }
            }
            PluginData.Item newItem = pluginData.getNewItem();
            newItem.setEnable(false);
            newItem.setMd5(getModMD5(path));
            newItem.setName(file.getName());
            newItem.setPath(path);
            newItem.setSize(file.length());
            newItem.setPosition(list.size() + 1);
            list.add(newItem);
            pluginData.saveData();
            return FileUtil.copyFile(file.getAbsolutePath(), new File(GamePluginManager.getInstance().getModFilesDir(), file.getName()).getAbsolutePath());
        }
        return false;
    }

    /**
     * 获取文件MD5值
     *
     * @return MD5
     */
    private String getModMD5(String path) {
        File file = new File(path);
        MessageDigest digest;
        FileInputStream in;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return HelperCore.EMPTY_STRING;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
}
