# Implementation

#### Directory

* [Game Elements(游戏元素)](#game-elements)

* [Plot(地图绘制)](#plot)

* [Bluetooth(蓝牙通信)](#bluetooth)

* [Sensor(传感器)](#sensor)

* [Network(网络通信)](#network)

* [Data Analysis(数据分析)](#data-analysis)

* [Server(服务器)](#server)

## Game Elements

* [Direction](../app/src/main/java/com/example/stevennl/tastysnake/model/Direction.java)

* [Pos](../app/src/main/java/com/example/stevennl/tastysnake/model/Pos.java)

* [Point](../app/src/main/java/com/example/stevennl/tastysnake/model/Point.java)

* [Map](../app/src/main/java/com/example/stevennl/tastysnake/model/Map.java)

* [Snake](../app/src/main/java/com/example/stevennl/tastysnake/model/Snake.java)

## Plot

Source: [DrawableGrid.java](../app/src/main/java/com/example/stevennl/tastysnake/widget/DrawableGrid.java)

整张地图的绘制方法封装在了DrawableGrid这个自定义控件中，在游戏界面的布局中我们只需要简单的添加这个控件即可在屏幕上显示一张地图：

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:DrawableGrid="http://schemas.android.com/apk/res-auto"
    android:id="@+id/activity_drawable_grid_test"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.example.stevennl.tastysnake.controller.test.DrawableGridTestActivity">
    <com.example.stevennl.tastysnake.widget.DrawableGrid
        android:id="@+id/drawablegrid_test_grid"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        DrawableGrid:showGridLine="false"/>
</RelativeLayout>
```

在Activity中对这个控件进行初始化：

```java
DrawableGrid grid = (DrawableGrid) findViewById(R.id.drawablegrid_test_grid);
grid.setMap(map);
grid.setBgColor(Config.COLOR_MAP_BG);
```

这里的`setMap(Map map)`方法将一张地图绑定到此控件，该控件每隔一定时间遍历地图中的Point数组，将每个点的信息绘制到屏幕上。关于Map和Point等元素的定义请参考[Game Elements](#game-elements)。

了解了绘图过程之后，我们接下来看一下这个自定义控件的内部实现，它继承自SurfaceView，SurfaceView的特点是可以在子线程中绘图，这样在频繁绘图时不会阻塞主线程。我们首先需要给SurfaceView添加回调接口，以便在恰当的生命周期开始绘图与停止绘图：

```java
/**
 * This view can divide the screen to several grids and draw the content of each grid.
 */
public class DrawableGrid extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * Initialize from XML resources file.
     *
     * @param context The context
     * @param attrs The attributes set
     */
    public DrawableGrid(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "DrawableGrid()");
        if (attrs != null) {
            initCustomAttr(context, attrs);
        }
        setKeepScreenOn(true);
        getHolder().addCallback(this);  // 添加回调接口
    }

    // Surface创建时开启绘图线程进行绘图
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated()");
        drawThread = new DrawThread(holder);
        drawThread.setRunning(true);
        drawThread.start();
    }

    // Surface尺寸等参数变化时更新绘图所依赖的参数
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged()");
        drawThread.updateParams();
    }

    // Surface被销毁时停止绘图线程
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed()");
        drawThread.setRunning(false);
    }
}
```

绘图线程DrawThread的`updateParams()`方法计算了进行绘图所需要的参数，包括每个格子的水平、垂直方向上的宽度、偏移量等：

```java
private void updateParams() {
    int width = getWidth();
    int height = getHeight();
    if (showGridLine) {
        int horLineCnt = rowCount + 1;
        int verLineCnt = colCount + 1;
        width -= verLineCnt;
        height -= horLineCnt;
    }
    horInterval = width / colCount;
    verInterval = height / rowCount;
    horOffset = (width % colCount) / 2;
    verOffset = (height % rowCount) / 2;
}
```

该线程的`run()`方法每隔一段时间调用`draw()`方法进行地图绘制：

```java
@Override
public void run() {
    while (running) {
        try {
            if (!pause) {
                draw();
                Thread.sleep(Config.FREQUENCY_DRAW);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }
}

private void draw() {
    try {
        // 从SurfaceHolder中获取画布并锁定该画布
        canvas = holder_.lockCanvas();
        if (canvas != null) {
            canvas.drawColor(bgColor);
            drawMapContent(canvas);  // 在锁定的画布上绘制地图内容
        }
    } catch (Exception e) {
        Log.e(TAG, "", e);
    } finally {
        if (canvas != null) {
            holder_.unlockCanvasAndPost(canvas);  // 解锁画布，提交更改
        }
    }
}

private void drawMapContent(Canvas canvas) {
    if (map == null) return;
    float left, top, right, bottom;
    // 遍历地图上每个点，分别绘制每个点的内容
    for (int i = 0; i < rowCount; ++i) {
        for (int j = 0; j < colCount; ++j) {
            Point point = map.getPoint(i, j);
            if (showGridLine || point.getType() != Point.Type.BLANK) {
                paint.setColor(point.getColor());
                // 计算当前Point绘制的起始位置、终止位置等参数
                left = (showGridLine ? horOffset + 1 + j * (horInterval + 1)
                        : horOffset + j * horInterval);
                top = (showGridLine ? verOffset + 1 + i * (verInterval + 1)
                        : verOffset + i * verInterval);
                right = left + horInterval;
                bottom = top + verInterval;
                // 绘制当前Point的内容
                drawGrid(left, top, right, bottom, point.getType(), canvas);
            }
        }
    }
}
```

`drawGrid()`方法根据Point的内容将图形绘制到指定位置，目前这个游戏的地图上的元素只有圆形、矩形，直接调用`canvas.drawRect()`和`canvas.drawCircle()`即可完成绘制，网上有许多资料说明如何用canvas绘制几何图形，这里就不再展开叙述了。

## Bluetooth

Source: [BluetoothManager.java](../app/src/main/java/com/example/stevennl/tastysnake/util/bluetooth/BluetoothManager.java)

Reference: [Android Developer-bluetooth](https://developer.android.com/guide/topics/connectivity/bluetooth.html)

* Based on android.bluetooth.BluetoothAdapter

* Server: [AcceptThread](../app/src/main/java/com/example/stevennl/tastysnake/util/bluetooth/thread/AcceptThread.java)

* Client: [ConnectThread](../app/src/main/java/com/example/stevennl/tastysnake/util/bluetooth/thread/ConnectThread.java)

* Data Channel: [ConnectedThread](../app/src/main/java/com/example/stevennl/tastysnake/util/bluetooth/thread/ConnectedThread.java)

* [Custom Packet](./custom_packet.md)

## Sensor

Source: [SensorController.java](../app/src/main/java/com/example/stevennl/tastysnake/util/sensor/SensorController.java)

使用安卓设备提供的加速度传感器，获取x/y两个方向的加速度。为了获取初始数据确定x/y的使用，在测试中发现如下极端数据：（横屏）

```java
/**
 *      Left    Right   Up      Down
 * X    6       -6      0       0
 * Y    0       0       -5      7
 * Z    7       7       8       6
 */
```

观察可知，平面上的上下左右运动跟z加速度无关，去除；在判定上下运动时，跟y无关；在判定左右运动时，跟x无关。一个直接的实现想法是，首先判定x、y的绝对值大小：比如当x的绝对值比y小时，说明当前应当优先判断上下运动。然后判断x的正负情况，如果x为正，则是下运动。

为了优化重力感应体验，使用一个加速度accXAcc、accYAcc，来衡量加速度xAccValue、yAccValue的变化速度，也就是衡量玩家游戏时摆动手机的速度。具体如下：

1. 生成两个方法，供上层使用：在蛇做上下运动时，只判断左右方向；做左右运动时，只判断上下方向。

2. ACC_BOUND可以调整加速度accXAcc的阈值，控制灵敏度。

3. MIN_SEN可以控制判定的灵敏度，减少误操作。

4. HIGH_SEN控制极端情况，保证在某一方向的极端操作得到响应。

5. 此操作的优势之一是响应玩家摆动动作，而不是手机的加速度状态。另外，在向某一个角大幅度摆动时，可以得到“之”字型的走位。

6. 优化的几个参数还在调整，可以在[sensor_optimize分支](https://github.com/stevennL/TastySnake/tree/sensor_optimize)下查看，目前还未与主分支合并。

## Network

Source: [NetworkUtil.java](../app/src/main/java/com/example/stevennl/tastysnake/util/network/NetworkUtil.java)

应用中的网络请求模块使用了Google提供的轻量级网络访问框架[volley](https://android.googlesource.com/platform/frameworks/volley/+/4ad53e3321d9bed5a216d65623d92c91c5457e55)，此框架提供了一个请求队列(RequestQueue)，可以将网络请求添加至此队列，交给volley去发送。

有了这个框架，我们要做的就是将应用中的请求URL和请求参数封装起来。为此，我们先进行通用的GET请求和POST请求的封装：

```java
/**
 * Send a GET request.
 *
 * @param url The server url
 * @param params GET parameters
 * @param resListener Called when receiving response
 * @param errListener Called when error occurs
 */
public void get(String url, Map<String, String> params,
                Response.Listener<String> resListener, Response.ErrorListener errListener) {
    String getUrl = buildUrl(url, params);
    Log.d(TAG, "Get url: " + getUrl);
    StringRequest req = new StringRequest(getUrl, resListener, errListener);
    req.setRetryPolicy(new DefaultRetryPolicy(Config.REQ_TIMEOUT,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    req.setTag(REQUEST_TAG);
    queue.add(req);
}

/**
 * Send a POST request.
 *
 * @param url The server url
 * @param params POST parameters
 * @param resListener Called when receiving response
 * @param errListener Called when error occurs
 */
public void post(String url, final Map<String, String> params,
                 Response.Listener<String> resListener, Response.ErrorListener errListener) {
    StringRequest req = new StringRequest(Request.Method.POST, url, resListener, errListener) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return params;
        }
    };
    req.setRetryPolicy(new DefaultRetryPolicy(Config.REQ_TIMEOUT,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    req.setTag(REQUEST_TAG);
    queue.add(req);
}
```

有了通用的发送GET和POST请求的方法后，就可以开始封装应用所需要发送的每一个具体请求了。在此之前，我们先定义一个回调接口`ResultListener<T>`，方法`void onGotResult(T result)`将在客户端收到服务端返回的信息并解析出结构化的数据之后调用，这个结构化的数据将存放在result参数中，方法`void onError(VolleyError err)`将在出现异常之后调用：

```java
/**
 * Listener for network result.
 *
 * @param <T> The type of result
 */
public interface ResultListener<T> {
    /**
     * Called when the result is got
     *
     * @param result The result got
     */
    void onGotResult(T result);

    /**
     * Called when error occurs.
     *
     * @param err {@link VolleyError}
     */
    void onError(VolleyError err);
}
```

接下来进行具体请求的封装，第一个需要用到的请求是向服务器发送最新统计出的能力指数值W，关于数据统计的细节可以直接看[数据分析部分](#data-analysis)。我们使用POST请求完成这项功能，参数除了附带W值，还要附带上设备的ID号：

```java
/**
 * Insert a W value to remote database.
 *
 * @param w The W value to be inserted
 * @param listener A {@link ResultListener}
 */
public void insertW(int w, @Nullable final ResultListener<String> listener) {
    Map<String, String> params = new HashMap<>();
    params.put("id", Config.DEVICE_ID);
    params.put("w", String.valueOf(w));
    post(Config.URL_INSERT_W, params, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d(TAG, "insertW() response: " + response);
            if (listener != null) {
                listener.onGotResult(response);
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "insertW(): " + error.toString());
            if (listener != null) {
                listener.onError(error);
            }
        }
    });
}
```

第二个要用到的请求是获得服务器数据库中的W的平均值，使用GET请求实现，无需附带参数：

```java
/**
 * Get average W value from remote server.
 *
 * @param listener A {@link ResultListener}
 */
public void getAvgW(@Nullable final ResultListener<Integer> listener) {
    get(Config.URL_GET_AVG_W, null, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d(TAG, "getAvgW() response: " + response);
            int avgW = 0;
            try {
                // 服务器返回的是字符串，使用如下方法解析成整数值
                avgW = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                Log.e(TAG, "getAvgW() error:", e);
            }
            if (listener != null) {
                listener.onGotResult(avgW);
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "getAvgW(): " + error.toString());
            if (listener != null) {
                listener.onError(error);
            }
        }
    });
}
```

其余的请求方法如`getAllW()`、`removeW()`、`removeAllW()`等供调试使用，这里就不一一叙述了。

## Data Analysis

Docs: [Database](./database.md) [Data Analysis](./data_analysis.md)

* Analyze using data in local database

* Analyze using data from remote server

* Upload local data to remote through [UploadService](../app/src/main/java/com/example/stevennl/tastysnake/util/network/UploadService.java)

## Server

* Server config

* Insert new W value

* Calculate average W value