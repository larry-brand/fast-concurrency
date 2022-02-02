# Fast Concurrency library
Implementation of some Java Concurrency classes with better performance.
Project is in progress.

List of classes
----
FastLinkedBlockingQueue - implementation of LinkedBlockingQueue with StampedLock

Testing
--------

Compile to jars
`mvn clean verify`

Run tests using jcstress
`java -jar test/target/jcstress.jar`

Run benchmarks using jmh
`java -cp test/target/benchmarks.jar org.sample.MyBenchmark`
