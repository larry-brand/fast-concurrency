package org.sample;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class StringBenchmark {

    @State(Scope.Benchmark)
    public static class MyState {
        String text = "text";
        String time = new Long(System.currentTimeMillis()).toString();
    }

    @Benchmark
    public void testString(MyState state) {
        String s = "";
        for (int i=0; i<100; i++) {
            s += "123" + state.text + "456" + state.time +
                    "123" + state.text + "456" + state.time +
                    "123" + state.text + "456" + state.time;
        }
    }

    @Benchmark
    public void testStringBuilder(MyState state) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<100; i++) {
            sb.append("123").append(state.text).append("456").append(state.time)
                    .append("123").append(state.text).append("456").append(state.time)
                    .append("123").append(state.text).append("456").append(state.time);
        }
        String s = sb.toString();
    }

    @Benchmark
    public void testStringBuffer(MyState state) {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<100; i++) {
            sb.append("123").append(state.text).append("456").append(state.time)
                    .append("123").append(state.text).append("456").append(state.time)
                    .append("123").append(state.text).append("456").append(state.time);
        }
        String s = sb.toString();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(StringBenchmark.class.getSimpleName())
                .forks(1).warmupForks(1)
                .warmupIterations(2).warmupTime(TimeValue.milliseconds(1000))
                .measurementIterations(3).measurementTime(TimeValue.seconds(2))
                .timeUnit(TimeUnit.MICROSECONDS)
                .mode(Mode.Throughput)
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }

}
