---
name: "java-swing-game-framework"
description: "Generates and explains the Java Swing/AWT 2D game framework architecture with MVC pattern, singleton, template method, and factory patterns. Invoke when user wants to create or understand a Java Swing game project."
---

# Java Swing 2D游戏框架

## 架构概述

这是一个基于 Java Swing/AWT 的 2D 游戏框架，采用 MVC（Model-View-Controller）架构模式，结合多种设计模式实现模块化、可扩展的游戏开发。

## 包结构

```
src/com/tedu/
├── game/          # 游戏入口
├── controller/    # 控制层（游戏循环、用户输入）
├── element/       # 模型层（游戏元素基类及子类）
├── show/          # 视图层（UI渲染）
├── manager/       # 管理层（数据管理、配置加载）
└── text/          # 配置文件（资源映射、地图数据）
```

## 核心设计模式

### 1. 单例模式（Singleton）
- **实现位置**: `manager/ElementManager.java`
- **作用**: 全局唯一的元素管理器，确保所有模块共享同一数据中心
- **实现方式**: 饱汉模式（懒加载）+ `synchronized` 线程安全

### 2. 模板方法模式（Template Method）
- **实现位置**: `element/ElementObj.java`
- **核心方法**: `model(long gameTime)` 定义对象执行的固定顺序：
  1. `updateImage()` - 更新图片
  2. `move()` - 移动
  3. `add()` - 添加子元素（如子弹）

### 3. 工厂模式（Factory）- 通过反射实现
- **实现位置**: `manager/GameLoad.java`
- **核心方法**: `getObj(String str)` 通过配置文件中的类名反射创建对象
- **配置文件**: `text/obj.pro` 定义 `key=全限定类名` 的映射关系

## 游戏循环架构

### 主线程三段式结构

```java
@Override
public void run() {
    while(true) {
        gameLoad();   // 1. 加载阶段：加载资源、地图、玩家
        gameRun();    // 2. 运行阶段：逻辑更新、碰撞检测
        gameOver();   // 3. 结束阶段：资源回收、关卡切换
        sleep(50);
    }
}
```

### 运行阶段核心逻辑

- **元素更新**: `moveAndUpdate()` 遍历所有元素类型，调用每个元素的 `model()` 模板方法
- **碰撞检测**: `ElementPK()` 使用 `Rectangle.intersects()` 判断碰撞

## 渲染架构

### 视图层双线程设计

| 线程 | 职责 | 刷新频率 |
|------|------|----------|
| GameThread | 游戏逻辑更新（移动、碰撞） | 100次/秒 |
| GameMainJPanel (Runnable) | UI重绘 | 100次/秒 |

### 渲染流程

```java
@Override
public void paint(Graphics g) {
    Map<GameElement, List<ElementObj>> all = em.getGameElements();
    for(GameElement ge : GameElement.values()) {
        List<ElementObj> list = all.get(ge);
        for(ElementObj obj : list) {
            obj.showElement(g);
        }
    }
}
```

## 用户输入处理

- 使用 `HashSet<Integer>` 记录按下的键，防止重复触发
- `keyPressed()`：记录按键，调用玩家的 `keyClick(true, key)`
- `keyReleased()`：移除按键，调用玩家的 `keyClick(false, key)`

## 配置驱动设计

### 配置文件体系

| 文件 | 内容 | 用途 |
|------|------|------|
| `GameData.pro` | `key=图片路径` | 图片资源映射 |
| `obj.pro` | `key=全限定类名` | 对象类名映射（反射创建） |
| `*.map` | `元素类型=坐标1;坐标2;...` | 地图数据 |

## 架构流程图

```
GameStart (入口)
    │
    ▼
GameJFrame (主窗体)
    │
┌───┴───┬───┐
▼       ▼   ▼
JPanel Listener Thread
(视图)  (输入) (逻辑)
    │   │   │
    └───┼───┘
        ▼
ElementManager (单例数据中心)
        │
        ▼
GameElement (枚举分类)
        │
        ▼
Map<枚举, List<ElementObj>>
        │
        ▼
ElementObj (基类，模板方法)
```

## 扩展指南

### 添加新元素类型
1. 在 `GameElement` 枚举中添加新类型
2. 在 `obj.pro` 中配置 `key=全限定类名`
3. 在 `GameData.pro` 中配置图片路径
4. 创建继承 `ElementObj` 的子类，重写必要方法

### 添加新关卡
1. 创建新的 `*.map` 文件
2. 在 `GameLoad.MapLoad()` 中加载新地图

### 修改碰撞逻辑
- 重写元素的 `pk()` 方法
- 或修改 `GameThread.ElementPK()` 方法

## 设计原则

- **MVC分层**: 视图、控制、模型清晰分离
- **单一职责**: 每个类只负责一个功能
- **依赖倒置**: 通过接口和抽象类降低耦合
- **开闭原则**: 对扩展开放，对修改关闭
- **配置驱动**: 资源和对象创建通过配置文件控制
