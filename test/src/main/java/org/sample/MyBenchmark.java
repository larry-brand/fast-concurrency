package org.sample;

import fast.FastLinkedBlockingQueue;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MyBenchmark {

    @State(Scope.Benchmark)
    public static class MyState {
        final BlockingQueue<Integer> javaQueue = new LinkedBlockingQueue();
        final BlockingQueue<Integer> myQueue = new FastLinkedBlockingQueue();
    }

    @Benchmark
    public void testJavaQueue(MyState state) throws InterruptedException {
        state.javaQueue.put(Integer.valueOf(7));
        state.javaQueue.peek();
        state.javaQueue.peek();
        state.javaQueue.peek();
        state.javaQueue.peek();
        state.javaQueue.peek();
        state.javaQueue.peek();
        state.javaQueue.peek();
        state.javaQueue.peek();
        state.javaQueue.peek();
        state.javaQueue.peek();
    }

    @Benchmark
    public void testMyQueue(MyState state) throws InterruptedException {
        state.myQueue.put(Integer.valueOf(7));
        state.myQueue.peek();
        state.myQueue.peek();
        state.myQueue.peek();
        state.myQueue.peek();
        state.myQueue.peek();
        state.myQueue.peek();
        state.myQueue.peek();
        state.myQueue.peek();
        state.myQueue.peek();
        state.myQueue.peek();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MyBenchmark.class.getSimpleName())
                .forks(2).warmupForks(1)
                .warmupIterations(3).warmupTime(TimeValue.milliseconds(500))
                .measurementIterations(3).measurementTime(TimeValue.seconds(2))
                .timeUnit(TimeUnit.MICROSECONDS)
                .mode(Mode.Throughput)
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }

}
