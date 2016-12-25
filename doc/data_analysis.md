# Data Analysis

Use data in [database](./database.md) to analyze user's behaviour.

Author: [@xuanqu](https://github.com/xuanqu) [@stevennL](https://github.com/stevennL)

## Local

Data analysis in local device.

Source: [AnalysisData.java](../app/src/main/java/com/example/stevennl/tastysnake/model/AnalysisData.java)

### Description

1. 您到目前为止一共进行了N局游戏。

2. 赢X局，其中智商碾压A局，侥幸获胜B局。

3. 输Y局，其中被对方戏耍C局，因失误失败D局。

4. 每一局的平均时长为T秒。

5. 每一局你的蛇的平均长度为L1节。

6. 每一局对方的蛇的平均长度为L2节。

7. 您的能力指数为W。

8. 您的技术评估为P。

### Definition

智商碾压：win=true && cause=HIT_ENEMY

侥幸获胜：win=true && (cause=OUT || cause=SUICIDE)

被对方戏耍：win=false && cause=HIT_ENEMY

因失误失败：win=false && (cause=OUT || cause=SUICIDE)

W = (100/N)\*((7\*A+5\*B)\*(18-log2(T+1))+(1\*C+3\*D)\*log2(T+2))

| P | Range |
|:-:|:-----:|
|王者|W >= 8500|
|大师|6100 <= W < 8500|
|黄金|3800 <= W < 6100|
|白银|1500 <= W < 3800|
|青铜|W < 1500|

Use [formula_test.m](./program/formula_test.m)(MATLAB) to test W and P.

## Remote

Data analysis using data from remote server.

### Description

1. 您的能力高出平均水平U%，值得鼓励！

2. 您的能力等于平均水平，加油！

3. 您的能力低于平均水平U%，再加把劲！

### Definition

```java
avg = getAvgWValueFromServer();
if (W > avg) {
    showDescription1();
    U = 100 * (W - avg) / avg;
} else if (W == avg) {
    showDescription2();
} else if (W < avg) {
    showDescription3();
    U = 100 * (avg - W) / avg;
}
```