# Computer Science Game Online | CSGO
![Author](https://img.shields.io/badge/Author-gansir-green) ![Author](https://img.shields.io/badge/Author-Potatovender-green) ![Author](https://img.shields.io/badge/Author-Darrevis-green)  ![language](https://img.shields.io/badge/OpenJDK-1.8.x-green)
### 0x01 Introduction：

```
A good example for networking basic TPS shooting game via JDK.
```

**Feature**

* Low Time Complexity and Space Complexity.
* Customizable FPS.
* Unlimited numbers of Player.
* Short and Simple.



### 0x02 How To Use：
* Find somebody to host the server (They can also run the game while hosting) (Better in Local Network)
* Copy the server's IP adress and configure it in the client
* Start the game

In ClientNetworkConst:
```bash
    private final String SERVER_ADRESS = "127.0.0.1"; //"Change this to the server's IP address";
    private final int PORT = 9067; //"Make sure to have the port of the server configured (it is configured by default)"
    private final int TICK = 10; //"Change this for network refreshing rate"
    private final String FST_SPLIT_REGEX = "&";
    private final String SEC_SPLIT_REGEX = " ";
    private final String LOGIN_REQ = "201";
```
