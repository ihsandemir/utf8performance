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
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/**
 *
 VM version: JDK 1.8.0_151, Java HotSpot(TM) 64-Bit Server VM, 25.151-b12
 3.12:
     Benchmark                                    Mode  Cnt   Score   Error   Units
     StringSerializationBenchmark.toDataAscii    thrpt        4.037          ops/us
     StringSerializationBenchmark.toDataUtf8     thrpt        5.139          ops/us
     StringSerializationBenchmark.toObjectAscii  thrpt       12.402          ops/us
     StringSerializationBenchmark.toObjectUtf8   thrpt        7.129          ops/us
     StringSerializationBenchmark.toDataAscii     avgt        0.245           us/op
     StringSerializationBenchmark.toDataUtf8      avgt        0.192           us/op
     StringSerializationBenchmark.toObjectAscii   avgt        0.080           us/op
     StringSerializationBenchmark.toObjectUtf8    avgt        0.215           us/op

 4.0-SNAPSHOT with the Fix:
     Benchmark                                    Mode  Cnt  Score   Error   Units
     StringSerializationBenchmark.toDataAscii    thrpt       4.648          ops/us
     StringSerializationBenchmark.toDataUtf8     thrpt       3.595          ops/us
     StringSerializationBenchmark.toObjectAscii  thrpt       9.758          ops/us
     StringSerializationBenchmark.toObjectUtf8   thrpt       6.287          ops/us
     StringSerializationBenchmark.toDataAscii     avgt       0.213           us/op
     StringSerializationBenchmark.toDataUtf8      avgt       0.274           us/op
     StringSerializationBenchmark.toObjectAscii   avgt       0.096           us/op
     StringSerializationBenchmark.toObjectUtf8    avgt       0.161           us/op

 VM version: JDK 12.0.1, Java HotSpot(TM) 64-Bit Server VM, 12.0.1+12
 3.12:
     Benchmark                                    Mode  Cnt   Score   Error   Units
     StringSerializationBenchmark.toDataAscii    thrpt        4.190          ops/us
     StringSerializationBenchmark.toDataUtf8     thrpt        5.192          ops/us
     StringSerializationBenchmark.toObjectAscii  thrpt       14.151          ops/us
     StringSerializationBenchmark.toObjectUtf8   thrpt        7.030          ops/us
     StringSerializationBenchmark.toDataAscii     avgt        0.232           us/op
     StringSerializationBenchmark.toDataUtf8      avgt        0.183           us/op
     StringSerializationBenchmark.toObjectAscii   avgt        0.072           us/op
     StringSerializationBenchmark.toObjectUtf8    avgt        0.145           us/op

 4.0-SNAPSHOT with the Fix:
     Benchmark                                    Mode  Cnt   Score   Error   Units
     StringSerializationBenchmark.toDataAscii    thrpt        6.312          ops/us
     StringSerializationBenchmark.toDataUtf8     thrpt        4.376          ops/us
     StringSerializationBenchmark.toObjectAscii  thrpt       12.447          ops/us
     StringSerializationBenchmark.toObjectUtf8   thrpt        6.712          ops/us
     StringSerializationBenchmark.toDataAscii     avgt        0.155           us/op
     StringSerializationBenchmark.toDataUtf8      avgt        0.231           us/op
     StringSerializationBenchmark.toObjectAscii   avgt        0.078           us/op
     StringSerializationBenchmark.toObjectUtf8    avgt        0.154           us/op

 *
 */

@State(Scope.Thread)
@OperationsPerInvocation(1000)
@Warmup(iterations = 1, time = 5)
@Fork(1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(iterations = 1, time = 10)
public class StringSerializationBenchmark {
    private SerializationService serializationService;
    private String asciiString = "my map name is short";
    private Data asciiData;
    private String utf8String = "My uf8 map name \uc2ba ÇŞÖÜĞ \ue0a0 \uD867\uDE3D \uD867\uDE3D";
    private Data utf8Data;

    @Setup
    public void setup() {
        DefaultSerializationServiceBuilder defaultSerializationServiceBuilder = new DefaultSerializationServiceBuilder();
        serializationService = defaultSerializationServiceBuilder.setVersion(InternalSerializationService.VERSION_1)
                                                                                      .build();
        asciiData = serializationService.toData(asciiString);
        utf8Data = serializationService.toData(utf8String);
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    public void toDataAscii(Blackhole blackhole) {
        for (int i = 0; i < 1000; i++) {
            blackhole.consume(serializationService.toData(asciiString));
        }
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    public void toObjectAscii(Blackhole blackhole) {
        for (int i = 0; i < 1000; i++) {
            blackhole.consume(serializationService.toObject(asciiData));
        }
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    public void toDataUtf8(Blackhole blackhole) {
        for (int i = 0; i < 1000; i++) {
            blackhole.consume(serializationService.toData(utf8String));
        }
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    public void toObjectUtf8(Blackhole blackhole) {
        for (int i = 0; i < 1000; i++) {
            blackhole.consume(serializationService.toObject(utf8Data));
        }
    }

}
