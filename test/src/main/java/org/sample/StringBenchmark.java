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

package org.sample;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

public class StringBenchmark {

    @State(Scope.Benchmark)
    public static class MyState {
        final StringBuilder stringBuilder = new StringBuilder();
        final StringBuffer stringBuffer = new StringBuffer();
        final String text = "text";
        final String time = new Long(System.currentTimeMillis()).toString();
    }

    @Benchmark
    public void testString(MyState state) {
        String s = "123" + state.text + "456" + state.time +
                "123" + state.text + "456" + state.time +
                "123" + state.text + "456" + state.time;
    }

    @Benchmark
    public void testStringBuilder(MyState state) {
        String s = state.stringBuilder.append("123").append(state.text).append("456").append(state.time)
                .append("123").append(state.text).append("456").append(state.time)
                .append("123").append(state.text).append("456").append(state.time).toString();
    }

    @Benchmark
    public void testStringBuffer(MyState state) {
        String s = state.stringBuffer.append("123").append(state.text).append("456").append(state.time)
                .append("123").append(state.text).append("456").append(state.time)
                .append("123").append(state.text).append("456").append(state.time).toString();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(StringBenchmark.class.getSimpleName())
                .forks(2).warmupForks(1)
                .warmupIterations(3).warmupTime(TimeValue.milliseconds(1000))
                .measurementIterations(5).measurementTime(TimeValue.seconds(3))
                .timeUnit(TimeUnit.MICROSECONDS)
                .mode(Mode.Throughput)
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }

}
