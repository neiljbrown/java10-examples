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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * JUnit test case providing examples of enhancements made to {@link java.util.Optional} in Java 10 (J10).
 */
public class OptionalTypeTest {

  /**
   * In J10, {@link Optional} has been extended with a single new method {@link Optional#orElseThrow()} which returns
   * the value if present, else throws an exception. The method is identical in behaviour to existing method
   * {@link Optional#get()}, and seems only to  have been added as a more clearly named alternative. It's assumed that
   * {@link Optional#get()} will be deprecated in the future.
   */
  @Test
  public void orElseThrow() {
    var surnames = List.of("Adams", "Brown", "Campbell");

    // Illustrates new method Optional.orElseThrow() returns the Optional's value when it has one
    String firstSurname = surnames.stream()
      .filter(s -> s.startsWith("A"))
      .findFirst() // Returns Optional
      .orElseThrow();
    assertThat(firstSurname).isEqualTo(surnames.get(0));

    // Illustrates new method Optional.orElseThrow() throws an exception if the Optional has no value
    assertThatThrownBy(() ->
      surnames.stream()
        .filter(s -> s.startsWith("Z"))
        .findAny() // Returns Optional
        .orElseThrow()
    ).isInstanceOf(NoSuchElementException.class);
  }
}