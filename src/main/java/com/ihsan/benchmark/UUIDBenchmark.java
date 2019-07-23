/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * # JMH version: 1.21
 * # VM version: JDK 12.0.1, OpenJDK 64-Bit Server VM, 12.0.1+12
 * # VM invoker: /Users/ihsan/Desktop/jdk-12.0.1.jdk/Contents/Home/bin/java
 * # VM options: -Dfile.encoding=UTF-8
 * # Warmup: 3 iterations, 5 s each
 * # Measurement: 1 iterations, 10 s each
 * # Timeout: 10 min per iteration
 * # Threads: 1 thread, will synchronize iterations
 *
 * Using 4.0-SNAPSHOT:
 *
 * With fix:
 *
 * Benchmark                Mode  Cnt    Score   Error   Units
 * UUIDBenchmark.toData    thrpt         5.904          ops/us
 * UUIDBenchmark.toObject  thrpt       120.280          ops/us
 * UUIDBenchmark.toData     avgt         0.174           us/op
 * UUIDBenchmark.toObject   avgt         0.010           us/op
 *
 *
 * Without fix:
 *
 * Benchmark                Mode  Cnt   Score   Error   Units
 * UUIDBenchmark.toData    thrpt        0.775          ops/us
 * UUIDBenchmark.toObject  thrpt       58.748          ops/us
 * UUIDBenchmark.toData     avgt        0.917           us/op
 * UUIDBenchmark.toObject   avgt        0.025           us/op
 *
 */

package com.ihsan.benchmark;

import com.hazelcast.internal.serialization.InternalSerializationService;
import com.hazelcast.internal.serialization.impl.DefaultSerializationServiceBuilder;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.spi.serialization.SerializationService;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@Warmup(iterations = 3, time = 5)
@Fork(1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(iterations = 1, time = 10)
public class UUIDBenchmark {
    private SerializationService serializationService;
    private static final int TEST_DATA_SIZE = 1000000; // 1 million
    Random random = new Random();
    List<UUID> testUuids = new ArrayList<>(TEST_DATA_SIZE);
    Iterator<UUID> uuidIterator;
    List<Data> testDatas = new ArrayList<>(TEST_DATA_SIZE);
    Iterator<Data> dataIterator;

    @Setup
    public void setup() {
        DefaultSerializationServiceBuilder defaultSerializationServiceBuilder = new DefaultSerializationServiceBuilder();
        serializationService = defaultSerializationServiceBuilder.setVersion(InternalSerializationService.VERSION_1)
                                                                 .build();
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            UUID uuid = new UUID(random.nextLong(), random.nextLong());
            testUuids.add(uuid);
            testDatas.add(serializationService.toData(uuid));
        }
        uuidIterator = testUuids.iterator();
        dataIterator = testDatas.iterator();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    public void toData() {
        if (!uuidIterator.hasNext()) {
            uuidIterator = testUuids.iterator();
        }
        serializationService.toData(uuidIterator.next());
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    public void toObject() {
        if (!dataIterator.hasNext()) {
            dataIterator = testDatas.iterator();
        }
        serializationService.toData(dataIterator.next());
    }
}
