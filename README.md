# utf8performance
Test For UTF8 Performance
Tested strings:
```java
    private String asciiString = "my map name is short";
    private String utf8String = "My uf8 map name \uc2ba ÇŞÖÜĞ \ue0a0 \uD867\uDE3D \uD867\uDE3D";
```


Results:

**toObjectUtf8 Performance ops/usec:**

|jdk8 3.12|jdk12 3.12|jdk 8 4.0-SNAPSHOT|jdk12 4.0-SNAPSHOT|
|---------|----------|------------------|------------------|
|  7.129  |  5.699   |     6.287        |        6.712     |

**toObjectAscii Performance ops/usec:**

|jdk8 3.12|jdk12 3.12|jdk 8 4.0-SNAPSHOT|jdk12 4.0-SNAPSHOT|
|---------|----------|------------------|------------------|
| 12.402  |  12.570  |     9.758        |        12.447    |


**toDataUtf8 Performance ops/usec:**

|jdk8 3.12|jdk12 3.12|jdk 8 4.0-SNAPSHOT|jdk12 4.0-SNAPSHOT|
|---------|----------|------------------|------------------|
| 5.139   |  5.192   |     3.595        |        4.376    |

