/*
 *  Copyright 2014-present the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.neiljbrown.examples.java10;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A JUnit test case providing examples of the additional {@link java.util.stream.Collector} that have been added to
 * {@link java.util.stream.Collectors} in Java 10 (J10).
 *
 * <h2>Accumulate Elements into Unmodifiable Collections</h2>
 * The {@link java.util.stream.Collectors} static utility class has been extended to provide methods that return
 * {@link java.util.stream.Collector} that accumulate the input elements into different types of _unmodifiable_
 * collection, including List, Set and Map. (A summary  of the characteristics of _unmodifiable_ collections can be
 * found in {@link CollectionEnhancementsTest}).
 */
public class CollectorsTest {

  /**
   * Provides an example of how to collect (accumulate) the elements of a Stream into an _unmodifiable_ List using
   * the {@link java.util.stream.Collector} created by new J10 method {@link Collectors#toUnmodifiableList()}.
   */
  @Test
  public void collectStreamToUnmodifiableList() {
    List<String> colours = Stream.of("red", "yellow", "pink").collect(Collectors.toUnmodifiableList());

    // The list created by the Collector preserves the order of the elements in the Stream
    assertThat(colours).containsExactly("red", "yellow", "pink");

    // The collection created by the Collector is unmodifiable
    assertThatThrownBy(() ->
      colours.add("orange")
    ).isInstanceOf(UnsupportedOperationException.class);
  }

  /**
   * Provides an example of how to collect (accumulate) the elements of a Stream into an _unmodifiable_ Set using
   * the {@link java.util.stream.Collector} created by new J10 method {@link Collectors#toUnmodifiableSet()}.  This
   * example collects a (mutable) List into an unmodifiable Set.
   */
  @Test
  public void collectStreamToUnmodifiableSet() {
    Set<String> colours = Stream.of("red", "yellow", "pink", "red").collect(Collectors.toUnmodifiableSet());

    // Assert duplicates removed. Note order of elements in created Set is undefined (not preserved from Stream)
    assertThat(colours).containsExactlyInAnyOrder("red", "yellow", "pink");

    // The collection created by the Collector is unmodifiable
    assertThatThrownBy(() ->
      colours.add("orange")
    ).isInstanceOf(UnsupportedOperationException.class);
  }
}