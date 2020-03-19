## 基本API
**获取设备标识**
`Helper.getUserUUID();`

**获取手机IMEI**
`Helper.getIMEI();`

**初始化插件ID**
`Helper.init(String);`

**加载指定路径JS**
`Helper.loadJS(String);`

**获取api版本**
`Helper.getApiVersion();`

## 游戏API
**设置操作员状态**
`Helper.PlayerSetOperator(String,boolean);`

**攻击指定实体**
`Helper.PlayerAttack(String,long);`

**获取全部玩家**
`Helper.LevelGetAllPlayer();`

**设置物品是否可放在副手**
`Helper.ItemSetAllowOffhand(int,boolean);`
