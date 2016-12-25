# Custom Packet

Custom packet to transfer data among devices.

Author: [@stevennL](https://github.com/stevennL)

Source: [Packet.java](../app/src/main/java/com/example/stevennl/tastysnake/model/Packet.java)

## Implementation

* Packet size is **fixed**: 5 bytes.

* Provide methods to convert a Packet object to a byte[] object and vice versa.

* To convert, use String object as a bridge, namely each packet can be denoted as a string. The length of the string equals to the packet size. 

## Packet Type

* Let the string notation of each packet be of the form: **TABCD**.

* **T** is an arbitrary character that could distinguish each packet type.

* **ABCD** are four characters storing the data of the packet. If no data are required to store, the character will be a **space**.

Packet types are defined as follow:

| Type | Data | Description |
|:----:|------|-------------|
|FOOD_LENGTHEN|AB: the row number of the food. CD: the column number of the food.|Packet containing the position of food that will lengthen the snake.|
|FOOD_SHORTEN|AB: the row number of the food. CD: the column number of the food.|Packet containing the position of food that will shorten the snake.|
|MOVE|A: the move direction.|Packet containing a snake's move direction.|
|RESTART|A: the new attacker.|Packet containing a signal to restart the game.|
|TIME|AB: the remaining time.|Pakcet containing the remaining time of attack-defend switch.|
|WIN|A: the winner. B: the cause of the game ending.|Packet containing a winning message.|
|PREPARED|No data to be stored.|Packet to synchronize two player before game starts.|