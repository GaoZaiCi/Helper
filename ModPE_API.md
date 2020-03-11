## 基本API
**获取设备标识**
`String Helper.getUserUUID();`

**获取手机IMEI**
`String Helper.getIMEI();`

**初始化插件ID**
`boolean Helper.init(String);`

**加载指定路径JS**
`boolean Helper.loadJS(String);`

## 游戏API
**设置操作员状态**
`void Helper.PlayerSetOperator(boolean);`

**攻击实体**
`boolean Helper.PlayerAttack(long);`

**获取全部玩家**
`long[] Helper.LevelGetAllPlayer();`
