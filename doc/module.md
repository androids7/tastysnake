# Module

Directory:

* [Game Elements](#game-elements)

* [Plot](#plot)

* [Bluetooth](#bluetooth)

* [Sensor](#sensor)

* [Network](#network)

* [Data Analysis](#data-analysis)

* [Server](#server)

## Game Elements

Author: [@wangty6](https://github.com/wangty6)

* [Direction](../app/src/main/java/com/example/stevennl/tastysnake/model/Direction.java)

* [Pos](../app/src/main/java/com/example/stevennl/tastysnake/model/Pos.java)

* [Point](../app/src/main/java/com/example/stevennl/tastysnake/model/Point.java)

* [Map](../app/src/main/java/com/example/stevennl/tastysnake/model/Map.java)

* [Snake](../app/src/main/java/com/example/stevennl/tastysnake/model/Snake.java)

## Plot

Author: [@stevennL](https://github.com/stevennL)

Source: [DrawableGrid.java](../app/src/main/java/com/example/stevennl/tastysnake/widget/DrawableGrid.java)

* Extend SurfaceView

* Plot in a sub-thread.

## Bluetooth

Author: [@stevennL](https://github.com/stevennL)

Source: [BluetoothManager.java](../app/src/main/java/com/example/stevennl/tastysnake/util/bluetooth/BluetoothManager.java)

* Based on android.bluetooth.BluetoothAdapter

* Server: [AcceptThread](../app/src/main/java/com/example/stevennl/tastysnake/util/bluetooth/thread/AcceptThread.java)

* Client: [ConnectThread](../app/src/main/java/com/example/stevennl/tastysnake/util/bluetooth/thread/ConnectThread.java)

* Data Channel: [ConnectedThread](../app/src/main/java/com/example/stevennl/tastysnake/util/bluetooth/thread/ConnectedThread.java)

## Sensor

Author: [@YifengWong](https://github.com/YifengWong)

Source: [SensorController.java](../app/src/main/java/com/example/stevennl/tastysnake/util/sensor/SensorController.java)

* Optimization

## Network

Author: [@stevennL](https://github.com/stevennL)

Source: [NetworkUtil.java](../app/src/main/java/com/example/stevennl/tastysnake/util/network/NetworkUtil.java)

Library: [Google-volley](https://android.googlesource.com/platform/frameworks/volley/+/4ad53e3321d9bed5a216d65623d92c91c5457e55)

* GET

* POST

## Data Analysis

Author: [@xuanqu](https://github.com/xuanqu)

Docs: [data_analysis](data_analysis.md)

* Analyze using data in local database

* Analyze using data from remote server. 

* Upload local data to remote through [UploadService](../app/src/main/java/com/example/stevennl/tastysnake/util/network/UploadService.java)

## Server

Author: [@wangty6](https://github.com/wangty6)

* Server config

* Insert new W value.

* Compute average W value.