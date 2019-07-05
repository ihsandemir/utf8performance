# utf8performance
Test For UTF8 Performance
4.0-SNAPSHOT includes the fix at https://github.com/hazelcast/hazelcast/pull/15238 while 3.12 is the official Hazelcast release 3.12.

I used fixed string during the test. Tested strings:
```java
    private String asciiString = "my map name is short";
    private String utf8String = "My uf8 map name \uc2ba ÇŞÖÜĞ \ue0a0 \uD867\uDE3D \uD867\uDE3D";
```


#Results
##toObjectUtf8 Performance

###toObjectUtf8 Throughput ops/usec:

|jdk8 3.12|jdk12 3.12|jdk 8 4.0-SNAPSHOT|jdk12 4.0-SNAPSHOT|
|---------|----------|------------------|------------------|
|  7.129  |  5.699   |     6.287        |        6.712     |

###toObjectUtf8 average time usec/op:

|jdk8 3.12|jdk12 3.12|jdk 8 4.0-SNAPSHOT|jdk12 4.0-SNAPSHOT|
|---------|----------|------------------|------------------|
|  0.215  |  0.145   |     0.161        |        0.154     |

##toObjectAscii Performance

###toObjectAscii Throughput ops/usec:

|jdk8 3.12|jdk12 3.12|jdk 8 4.0-SNAPSHOT|jdk12 4.0-SNAPSHOT|
|---------|----------|------------------|------------------|
| 12.402  |  12.570  |     9.758        |        12.447    |

###toObjectAscii average time usec/op:

|jdk8 3.12|jdk12 3.12|jdk 8 4.0-SNAPSHOT|jdk12 4.0-SNAPSHOT|
|---------|----------|------------------|------------------|
|  0.080  |  0.072   |     0.096        |        0.078     |

##toDataUtf8 Performance

###toDataUtf8 Throughput ops/usec:

|jdk8 3.12|jdk12 3.12|jdk 8 4.0-SNAPSHOT|jdk12 4.0-SNAPSHOT|
|---------|----------|------------------|------------------|
| 5.139   |  5.192   |     3.595        |        4.376    |

###toDataUtf8 average time usec/op:

|jdk8 3.12|jdk12 3.12|jdk 8 4.0-SNAPSHOT|jdk12 4.0-SNAPSHOT|
|---------|----------|------------------|------------------|
|  0.192  |  0.183   |     0.274        |        0.231     |

##toDataAscii Performance

###toDataAscii Throughput ops/usec:

|jdk8 3.12|jdk12 3.12|jdk 8 4.0-SNAPSHOT|jdk12 4.0-SNAPSHOT|
|---------|----------|------------------|------------------|
| 4.037   |  4.190   |     4.648        |        6.312    |

###toDataAscii average time usec/op:

|jdk8 3.12|jdk12 3.12|jdk 8 4.0-SNAPSHOT|jdk12 4.0-SNAPSHOT|
|---------|----------|------------------|------------------|
|  0.245  |  0.232   |     0.213        |       0.155     |

