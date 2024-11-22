# Re-ApkPatcher
> [!IMPORTANT]
> Work in progress.

使用像 [Mixin](https://github.com/SpongePowered/Mixin) 一样的 API 将自己的 `Java` 或 `Kotlin` 代码注入到 dex 中，同时提供一些修改或替换 `APK Resource` 的功能。

# Dex-Mixin
`Dex-Mixin` 是 Re-ApkPatcher 的核心。

> 我希望开发者不仅可以通过它来创建用于使用 Re-ApkPatcher 修改 `APK` 的 `JAR`，还可以创建一个 `Android 项目`，并自动地在项目编译完成后使用 `Dex-Mixin` 将代码和资源注入原 `APK`。这样开发者就可以像编写一个 `Android 应用` 一样对应用代码进行修改。
>
> 我会在 Re-ApkPatcher 的第一个版本开发完成后尝试实现它 ^^

## 为什么使用 Dex-Mixin
- 对于一些未混淆的应用或精准的被注入代码匹配规则，可以让用户自行构建任意版本目标应用的修改版 `APK` 或由开发者自动化地修改多个版本的 `APK`
- 由于 `Dex-Mixin` 的原理以及使用它开发的方式，可以实现手写 `smali` 难以实现的复杂逻辑
- More...

[QWearNT](https://github.com/java30433/QWearNT) 中编写和使用了一个 `Gradle Plugin`，它通过 [Apktool](https://github.com/iBotPeaches/Apktool) 获得应用代码的 `smali`，然后解析 `smali` 文件，对代码进行修改。但由于原开发者编写它时的开发时间不充裕，导致其 `API` 不太完善，具体实现逻辑有很多待改进或不合理的地方。
最开始我想对其进行改进，但我最终决定尝试完全不同的代码注入方式，提供更好的 `API`，并使用 [Dexlib2](https://github.com/google/smali/tree/main/dexlib2) 实现对 `dex` 的修改。这也是这个项目被创建的原因。

# 用法
> 由于此项目还在开发中，目前仅能提供大致的开发方法

创建一个 `Java/Kotlin` 项目，将 `dex-mixin-api` 与目标应用的代码进行 `compileOnly`，按需求使用 `dex-mixin-api` 提供的注解并编写代码，编译 `JAR`，然后尽情使用 Re-ApkPatcher 对目标 `APK` 应用您的修改

# 贡献
欢迎向 Re-ApkPatcher 提交 Pull Request！

提交信息遵守 [AngularJS Git Commit Message Conventions
](https://docs.google.com/document/d/1QrDFcIiPjSLDn3EL15IJygNPiHORgU1_OOAqWjiDU5Y)。

> [!NOTE]
> 如果您不知道如何使用 `Dexlib2`，可以参考项目中使用到它的部分代码或阅读其 [源代码](https://github.com/google/smali/tree/main/dexlib2)。
