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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A JUnit test case providing examples of the small enhancements made to Collection classes/APIs in Java 10 (J10).
 *
 * <h2>Creating Unmodifiable Collections</h2>
 * In J10, the {@link List}, {@link java.util.Map} and {@link Set} interfaces have been extended to support creating
 * these types from an immutable copy of a supplied {@link java.util.Collection}, using a new static factory named
 * copyOf, e.g. {@link List#copyOf(Collection)}. The collection created by these new factory methods have similar
 * properties to those created by the of() static factory methods on the same interface, that were added in Java 9,
 * except the elements are sourced from a supplied Collection, rather than supplied individual elements. These
 * properties are described in the class-level Javadoc of each enhanced (List, Map, Set) interface, under the heading
 * "Unmodifiable [List|Set|Map]". In summary, these are -
 * <br/>
 * - The created Collection is unmodifiable. Any attempt to add, remove, or replace elements results in an exception.
 * Although there is nothing preventing the elements themselves from being modified if they're mutable.
 * <br/>
 * - Subsequent changes made to the source Collection are _not_ reflected (copied through) to the created Collection.
 * <br/>
 * - In the case of Lists only, the order of the elements in the created Collection is the same as the iteration
 * order of the source Collection. (For Set and Map, the iteration order of the created collection is undefined).
 */
public class CollectionEnhancementsTest {

  /**
   * Provides  an example of how the new {@link List#copyOf(Collection)} method can be used to create an immutable
   * copy of another List.
   */
  @Test
  public void createUnmodifiableListFromList(){
    //  Create a mutable List
    List<String> colours = Stream.of("red", "yellow", "green").collect(Collectors.toList());

    List<String> copiedColours = List.copyOf(colours);
    assertThat(copiedColours).containsExactlyElementsOf(colours); // Assert element order as well as presence & no.

    String newColour = "blue";
    colours.add(newColour);
    assertThat(copiedColours).doesNotContain(newColour);
  }

  /**
   * Provides  an example of how the new {@link List#copyOf(Collection)} method can be used to create an immutable
   * List by copying another type of compatible Collection, in this case a Set.
   * <p>
   * As you'd expected, the new copyOf(Collection) methods do not support creating a List or Set from a Map (even one
   * that implements {@link java.util.Collection}). Attempts to do so result in a compilation error, due to a mismatch
   * between the type of the Map elements (entry) and the type of the elements in the Collection to be created.
   */
  @Test
  public void createUnmodifiableListFromSet(){
    //  Create a mutable Set
    Set<Integer> uniqueIntegers = Stream.of(1, 2, 3).collect(Collectors.toSet());

    List<Integer> integerList = List.copyOf(uniqueIntegers);
    assertThat(integerList).containsExactlyElementsOf(uniqueIntegers); // Assert element order as well as presence & no.

    int nextInteger = 4;
    uniqueIntegers.add(nextInteger);
    assertThat(integerList).doesNotContain(nextInteger);
  }

  /**
   * Provides an example of how the new {@link Set#copyOf(Collection)} method can be used to create an immutable Set
   * by copying another type of compatible Collection, in this case a List.
   */
  @Test
  public void createdUnmodifiableSetFromList() {
    //  Create a mutable List, with duplicate values
    List<String> colours = List.of("red", "yellow", "green", "red");

    Set<String> uniqueColours = Set.copyOf(colours);
    // Assert duplicates removed. Note order of elements in created Set is undefined (not preserved from source List)
    assertThat(uniqueColours).containsExactlyInAnyOrder("yellow", "red", "green");
  }
}