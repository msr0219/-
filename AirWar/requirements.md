# 飞机大战游戏（雷霆战机风格）需求文档

## 1. 游戏概述

### 1.1 游戏类型
- **类型**: 2D垂直卷轴射击游戏（Vertical Scrolling Shooter）
- **风格**: 经典街机风格，类似雷霆战机
- **平台**: Java桌面应用（Swing/AWT）

### 1.2 核心玩法
玩家控制一架战斗机在屏幕底部向上移动，消灭上方袭来的敌机和障碍物，收集道具提升战斗力，挑战更高分数。

### 1.3 目标用户
- 休闲游戏玩家

---

## 2. 游戏元素类层次结构

### 2.1 GameElement 枚举定义

| 枚举值 | 含义 | 说明 |
|--------|------|------|
| PLAYER | 玩家战机 | 玩家控制的主体 |
| ENEMY_SMALL | 小型敌机 | 低血量、高速、低分值（mob.png） |
| ENEMY_MEDIUM | 中型敌机 | 中血量、中速、中分值（elite.png） |
| ENEMY_LARGE | 大型敌机 | 高血量、低速、高分值（elitePlus.png） |
| ELITE_PRO | 精英敌机 | 精英级、特殊攻击、高分值（elitePro.png） |
| BOSS | Boss敌机 | 超高血量、特殊攻击模式（boss.png） |
| PLAYER_BULLET | 玩家子弹 | 玩家发射的攻击弹药 |
| ENEMY_BULLET | 敌机子弹 | 敌机发射的攻击弹药 |
| POWERUP | 道具 | 增益物品（武器升级、护盾、生命等） |
| EXPLOSION | 爆炸效果 | 元素销毁时的视觉效果 |
| BACKGROUND | 背景 | 滚动的星空背景 |

### 2.2 ElementObj 子类设计

#### 2.2.1 Player（玩家战机）
- **继承**: ElementObj
- **属性**:
  - `hp`: 生命值（默认3）
  - `powerLevel`: 武器等级（1-5），影响子弹数量和形态
  - `bulletDamage`: 子弹伤害（默认1，最高5）
  - `hasShield`: 是否有护盾
  - `speed`: 移动速度
- **方法重写**:
  - `model(gameTime)`: 调用 updateImage() → move() → add()
  - `updateImage()`: 更新飞机图片（固定使用hero.png）
  - `move()`: 根据键盘输入移动，边界检测
  - `add()`: 根据powerLevel和bulletDamage发射子弹
  - `keyClick(isPressed, key)`: 处理键盘输入
  - `pk(other)`: 处理碰撞（敌机、敌机子弹、道具）

#### 2.2.2 EnemyPlane（敌机基类）
- **继承**: ElementObj
- **属性**:
  - `hp`: 生命值
  - `score`: 击杀得分
  - `speed`: 移动速度
  - `shootInterval`: 射击间隔（毫秒）
  - `lastShootTime`: 上次射击时间
- **方法重写**:
  - `model(gameTime)`: updateImage() → move() → add()
  - `move()`: 随机或固定路径移动
  - `add()`: 定时发射子弹

#### 2.2.3 EnemySmall（小型敌机）
- **继承**: EnemyPlane
- **属性**: hp=1, score=100, speed=8, shootInterval=0（不射击）
- **移动模式**: 直线向下或轻微左右摆动

#### 2.2.4 EnemyMedium（中型敌机）
- **继承**: EnemyPlane
- **属性**: hp=3, score=300, speed=5, shootInterval=2000
- **移动模式**: 蛇形或Z字形移动

#### 2.2.5 EnemyLarge（大型敌机）
- **继承**: EnemyPlane
- **属性**: hp=6, score=600, speed=3, shootInterval=1500
- **移动模式**: 左右巡航移动

#### 2.2.6 ElitePro（精英敌机）
- **继承**: EnemyPlane
- **属性**: hp=10, score=1000, speed=4, shootInterval=1000
- **特殊能力**: 发射追踪子弹、快速移动
- **移动模式**: 随机折线移动

#### 2.2.7 Boss（Boss敌机）
- **继承**: EnemyPlane
- **属性**: hp=50, score=5000, speed=2, shootInterval=500
- **特殊能力**: 多方向散射射击、阶段攻击模式
- **方法**:
  - `changePhase()`: 根据血量切换攻击阶段

#### 2.2.8 Bullet（子弹基类）
- **继承**: ElementObj
- **属性**:
  - `damage`: 伤害值
  - `isPlayerBullet`: 是否玩家子弹
- **方法重写**:
  - `move()`: 向上（玩家子弹）或向下（敌机子弹）移动
  - `pk(other)`: 碰撞检测

#### 2.2.9 PlayerBullet（玩家子弹）
- **继承**: Bullet
- **属性**: damage=1, isPlayerBullet=true

#### 2.2.10 EnemyBullet（敌机子弹）
- **继承**: Bullet
- **属性**: damage=1, isPlayerBullet=false

#### 2.2.11 PowerUp（道具）
- **继承**: ElementObj
- **属性**:
  - `type`: 道具类型（枚举）
- **道具类型**:
  - BULLET: 武器升级（子弹数量+1）
  - BULLET_PLUS: 武器强化（子弹伤害+1）
  - BOMB: 清屏炸弹（清除所有敌机和子弹）
  - BLOOD: 生命值+1
  - FREEZE: 冻结敌机（敌机停止移动3秒）
- **方法重写**:
  - `move()`: 缓慢向下移动
  - `pk(other)`: 与玩家碰撞时触发效果

#### 2.2.12 Explosion（爆炸效果）
- **继承**: ElementObj
- **属性**:
  - `frame`: 当前帧数
  - `maxFrames`: 总帧数（固定10帧）
  - `radius`: 爆炸半径
  - `color`: 爆炸颜色（根据敌机类型不同）
- **方法重写**:
  - `model(gameTime)`: 更新帧动画，扩大半径，完成后销毁
  - `showElement(g)`: 使用Graphics绘制圆形渐变爆炸效果（无图片资源）

#### 2.2.13 Background（背景）
- **继承**: ElementObj
- **属性**:
  - `yOffset`: 垂直偏移量
- **方法重写**:
  - `move()`: 向下滚动产生视差效果

---

## 3. 配置文件设计

### 3.1 obj.pro（对象类名映射）

```properties
PLAYER=com.tedu.element.Player
ENEMY_SMALL=com.tedu.element.EnemySmall
ENEMY_MEDIUM=com.tedu.element.EnemyMedium
ENEMY_LARGE=com.tedu.element.EnemyLarge
ELITE_PRO=com.tedu.element.ElitePro
BOSS=com.tedu.element.Boss
PLAYER_BULLET=com.tedu.element.PlayerBullet
ENEMY_BULLET=com.tedu.element.EnemyBullet
POWERUP=com.tedu.element.PowerUp
EXPLOSION=com.tedu.element.Explosion
BACKGROUND=com.tedu.element.Background
```

### 3.2 GameData.pro（图片资源映射）

> **路径说明**: 资源路径为相对于 `src/` 目录的classpath路径，框架通过 `ClassLoader.getResource()` 加载资源

```properties
# 玩家战机
player=images/hero.png

# 敌机
enemy_small=images/mob.png
enemy_medium=images/elite.png
enemy_large=images/elitePlus.png
elite_pro=images/elitePro.png
boss=images/boss.png

# 子弹
player_bullet=images/bullet_hero.png
enemy_bullet=images/bullet_enemy.png

# 道具
powerup_bullet=images/prop_bullet.png
powerup_bullet_plus=images/prop_bulletPlus.png
powerup_bomb=images/prop_bomb.png
powerup_blood=images/prop_blood.png
powerup_freeze=images/prop_freeze.png

# 背景
background=images/bg.jpg
background_2=images/bg2.jpg
background_3=images/bg3.jpg
background_4=images/bg4.jpg
background_5=images/bg5.jpg
```

### 3.3 地图文件设计（level_x.map）

#### level_1.map
```properties
# 波次1
WAVE=1
ENEMY_SMALL=50,50;200,50;350,50;125,50;275,50
ENEMY_MEDIUM=150,100;300,100

# 波次2
WAVE=2
ENEMY_SMALL=50,50;175,50;300,50;425,50
ENEMY_MEDIUM=100,100;250,100;400,100
ENEMY_LARGE=225,150

# 波次3 - Boss战
WAVE=3
BOSS=225,50
```

#### level_2.map
```properties
# 更高难度，更多敌机
WAVE=1
ENEMY_SMALL=50,50;125,50;200,50;275,50;350,50;425,50
ENEMY_MEDIUM=75,100;225,100;375,100

WAVE=2
ENEMY_SMALL=100,50;225,50;350,50
ENEMY_MEDIUM=50,100;175,100;300,100;425,100
ENEMY_LARGE=150,150;350,150

WAVE=3
ENEMY_MEDIUM=125,50;325,50
ENEMY_LARGE=225,100

WAVE=4
BOSS=225,50
```

---

## 4. 游戏流程和状态管理

### 4.1 游戏循环三阶段

#### 阶段一：gameLoad()
1. 加载 `GameData.pro` 资源配置
2. 加载 `obj.pro` 对象映射
3. 加载当前关卡地图文件（level_1.map）
4. 创建玩家对象并初始化位置（屏幕底部中央）
5. 创建背景对象
6. 初始化分数、生命、关卡等数据

#### 阶段二：gameRun()
1. **元素更新**: 调用所有元素的 `model()` 方法
2. **碰撞检测**: 调用 `ElementPK()` 检测所有碰撞
3. **波次管理**: 根据波次进度生成敌机
4. **关卡进度**: 完成所有波次后进入下一关
5. **状态检查**: 玩家生命值为0时进入 gameOver()

#### 阶段三：gameOver()
1. 停止游戏循环
2. 显示游戏结束界面（分数、最高记录）
3. 等待用户输入（重新开始/退出）

### 4.2 UI界面流程

```
开始界面 → 游戏界面 → 暂停界面 → 游戏界面
                          ↓
                     游戏结束界面 → 开始界面
```

### 4.3 用户输入映射

| 按键 | 功能 |
|------|------|
| ↑ / W | 向上移动 |
| ↓ / S | 向下移动 |
| ← / A | 向左移动 |
| → / D | 向右移动 |
| 空格 | 射击（自动射击，按住连发） |
| P / ESC | 暂停/继续 |
| Enter | 开始游戏/重新开始 |

### 4.4 暂停机制
- 按下P或ESC键暂停游戏
- 暂停时显示半透明遮罩和"暂停"文字
- 再次按下P或ESC键恢复游戏

---

## 5. 碰撞检测矩阵

### 5.1 碰撞交互表

| 元素A | 元素B | 交互结果 |
|-------|-------|----------|
| PLAYER | ENEMY_SMALL | 玩家受伤，敌机销毁 |
| PLAYER | ENEMY_MEDIUM | 玩家受伤，敌机销毁 |
| PLAYER | ENEMY_LARGE | 玩家受伤，敌机销毁 |
| PLAYER | ELITE_PRO | 玩家受伤，敌机销毁 |
| PLAYER | BOSS | 玩家受伤 |
| PLAYER | ENEMY_BULLET | 玩家受伤，子弹销毁 |
| PLAYER | POWERUP | 触发道具效果，道具销毁 |
| PLAYER_BULLET | ENEMY_SMALL | 敌机受伤，子弹销毁 |
| PLAYER_BULLET | ENEMY_MEDIUM | 敌机受伤，子弹销毁 |
| PLAYER_BULLET | ENEMY_LARGE | 敌机受伤，子弹销毁 |
| PLAYER_BULLET | ELITE_PRO | 敌机受伤，子弹销毁 |
| PLAYER_BULLET | BOSS | Boss受伤，子弹销毁 |
| PLAYER_BULLET | ENEMY_BULLET | 双方销毁 |

### 5.2 伤害规则
- 玩家子弹对所有敌机造成1点伤害
- 敌机子弹对玩家造成1点伤害
- 玩家与敌机直接碰撞造成1点伤害
- 护盾可以抵挡一次伤害（不扣血）

### 5.3 道具效果

| 道具类型 | 效果 |
|----------|------|
| BULLET | 武器升级（子弹数量+1，最高5发） |
| BULLET_PLUS | 武器强化（子弹伤害+1，最高3点） |
| BOMB | 清除屏幕上所有敌机和敌机子弹 |
| BLOOD | 生命值+1（最高5条） |
| FREEZE | 冻结敌机（敌机停止移动3秒） |

---

## 6. 武器升级系统

### 6.1 武器等级表

| 等级 | 子弹数量 | 子弹形态 | 特殊效果 |
|------|----------|----------|----------|
| 1 | 1发 | 单发直线 | 无 |
| 2 | 2发 | 双发扩散 | 无 |
| 3 | 3发 | 三发散射 | 无 |
| 4 | 4发 | 四发扇形 | 穿透效果 |
| 5 | 5发 | 五发全屏 | 穿透+跟踪 |

---

## 7. 分数系统

### 7.1 得分规则

| 击杀对象 | 基础得分 |
|----------|----------|
| 小型敌机 | 100分 |
| 中型敌机 | 300分 |
| 大型敌机 | 600分 |
| 精英敌机 | 1000分 |
| Boss | 5000分 |

### 7.2 连击奖励
- 连续击杀敌机获得额外分数加成
- 5连击：1.5倍得分
- 10连击：2倍得分
- 20连击：3倍得分

---

## 8. 难度递增机制

### 8.1 关卡难度
- 关卡越高，敌机数量越多
- 关卡越高，敌机血量越高
- 关卡越高，敌机射击频率越快

### 8.2 波次递增
- 每个关卡包含3-4波敌机
- 最后一波为Boss战
- 波次之间有短暂间隔

---

## 9. 非功能性需求

### 9.1 性能要求
- 游戏循环频率：100次/秒（sleep 50ms）
- 渲染频率：100次/秒
- 支持同时显示至少50个游戏元素

### 9.2 视觉要求
- 画面分辨率：500×700像素
- 颜色深度：32位真彩色
- 支持透明背景（PNG格式）

### 9.3 兼容性
- Java版本：JDK 8及以上
- 操作系统：Windows、Linux、macOS

### 9.4 代码质量
- 遵循MVC架构模式
- 使用设计模式：单例、模板方法、工厂
- 配置驱动，易于扩展
- 代码注释覆盖率≥30%

---

## 10. 代码结构规范

### 10.1 包结构

```
src/com/tedu/
├── game/
│   ├── GameStart.java        # 游戏入口
│   └── GameJFrame.java       # 主窗体
├── controller/
│   ├── GameThread.java       # 游戏逻辑线程
│   ├── GameKeyListener.java  # 键盘输入监听
│   └── GameMouseListener.java # 鼠标输入监听
├── element/
│   ├── ElementObj.java       # 元素基类（模板方法）
│   ├── GameElement.java      # 元素类型枚举
│   ├── Player.java           # 玩家战机
│   ├── EnemyPlane.java       # 敌机基类
│   ├── EnemySmall.java       # 小型敌机
│   ├── EnemyMedium.java      # 中型敌机
│   ├── EnemyLarge.java       # 大型敌机
│   ├── ElitePro.java         # 精英敌机
│   ├── Boss.java             # Boss敌机
│   ├── Bullet.java           # 子弹基类
│   ├── PlayerBullet.java     # 玩家子弹
│   ├── EnemyBullet.java      # 敌机子弹
│   ├── PowerUp.java          # 道具
│   ├── Explosion.java        # 爆炸效果
│   └── Background.java       # 背景
├── show/
│   ├── GameMainJPanel.java   # 主画布（渲染线程）
│   ├── StartJPanel.java      # 开始界面
│   ├── GameOverJPanel.java   # 游戏结束界面
│   └── PauseJPanel.java      # 暂停界面
├── manager/
│   ├── ElementManager.java   # 元素管理器（单例）
│   └── GameLoad.java         # 资源加载器（工厂）
└── text/
    ├── GameData.pro          # 图片资源配置
    ├── obj.pro               # 对象类名配置
    ├── level_1.map           # 关卡1地图
    ├── level_2.map           # 关卡2地图
    └── level_3.map           # 关卡3地图
```

### 10.2 核心类职责

| 类名 | 职责 | 设计模式 |
|------|------|----------|
| ElementManager | 管理所有游戏元素，提供增删查方法 | 单例模式 |
| ElementObj | 所有游戏元素的基类，定义模板方法 | 模板方法模式 |
| GameLoad | 根据配置文件反射创建对象 | 工厂模式 |
| GameThread | 游戏主循环，控制逻辑更新 | - |
| GameMainJPanel | UI渲染，双缓冲绘制 | - |

---

## 11. 资源说明

### 11.1 现有素材清单

| 文件 | 大小 | 用途 |
|------|------|------|
| hero.png | 15KB | 玩家战机 |
| mob.png | 7KB | 小型敌机 |
| elite.png | 11KB | 中型敌机 |
| elitePlus.png | 9KB | 大型敌机 |
| elitePro.png | 13KB | 精英敌机 |
| boss.png | 70KB | Boss敌机 |
| bullet_hero.png | 800B | 玩家子弹 |
| bullet_enemy.png | 1KB | 敌机子弹 |
| prop_bullet.png | 3KB | 武器升级道具 |
| prop_bulletPlus.png | 2KB | 武器强化道具 |
| prop_bomb.png | 2KB | 炸弹道具 |
| prop_blood.png | 3KB | 生命道具 |
| prop_freeze.png | 3KB | 冻结道具 |
| bg.jpg~bg5.jpg | 56KB~397KB | 背景图片 |
| img.png | 2KB | 备用素材（可用于UI图标或爆炸效果） |

### 11.2 代码绘制元素（无图片资源）

以下元素通过Java Graphics API直接绘制，不使用图片资源：

| 元素 | 绘制方式 |
|------|----------|
| Explosion | 圆形渐变爆炸效果 |
| StartJPanel | 绘制标题文字、开始按钮 |
| PauseJPanel | 绘制半透明遮罩、暂停文字 |
| GameOverJPanel | 绘制游戏结束文字、分数、重新开始按钮 |

---

## 12. 扩展计划

### 12.1 短期扩展
- 添加音效系统
- 添加排行榜功能
- 添加更多关卡

### 12.2 长期扩展
- 添加多人游戏模式
- 添加自定义战机系统
- 添加成就系统
- 添加道具商店

---

## 13. 验收标准

### 13.1 功能验收
1. 玩家可以通过方向键/WASD控制战机移动
2. 玩家可以发射子弹消灭敌机
3. 敌机按照波次从上方出现并攻击
4. Boss战正常触发
5. 道具系统正常工作
6. 分数系统正确计算
7. 暂停/继续功能正常
8. 游戏结束/重新开始功能正常

### 13.2 性能验收
1. 游戏运行流畅，无明显卡顿
2. 碰撞检测准确无误
3. 元素渲染正确，无闪烁
4. 内存使用稳定，无内存泄漏

### 12.3 代码验收
1. 符合MVC架构
2. 使用规定的设计模式
3. 配置文件正确配置
4. 代码结构清晰，命名规范