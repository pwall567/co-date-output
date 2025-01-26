# co-date-output

[![Build Status](https://github.com/pwall567/co-date-output/actions/workflows/build.yml/badge.svg)](https://github.com/pwall567/co-date-output/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/static/v1?label=Kotlin&message=v2.0.21&color=7f52ff&logo=kotlin&logoColor=7f52ff)](https://github.com/JetBrains/kotlin/releases/tag/v2.0.21)
[![Maven Central](https://img.shields.io/maven-central/v/net.pwall.util/co-date-output?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.pwall.util%22%20AND%20a:%22co-date-output%22)

Non-blocking date output functions

## Background

This is a Kotlin coroutine-aware version of the [`date-output`](https://github.com/pwall567/date-output) date output
function library.
All of the `outputX` functions in that library are replicated here as `coOutputX`, taking a suspend function as a
parameter, and as `CoOutput.outputX` &ndash; an extension function on
[`CoOutput`](https://github.com/pwall567/co-int-output/blob/main/README.md#cooutput).
This brings the efficiencies (in particular, the avoidance of object creation) of the original library to non-blocking
output.

## Dependency Specification

The latest version of the library is 1.6, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>net.pwall.util</groupId>
      <artifactId>co-date-output</artifactId>
      <version>1.6</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'net.pwall.util:co-date-output:1.6'
```
### Gradle (kts)
```kotlin
    implementation("net.pwall.util:co-date-output:1.6")
```

Peter Wall

2025-01-26
