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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A JUnit test case providing example uses of 'var' - the new local-variable type inference feature in Java 10 (J10).
 * <p>
 * Type inference is the practice of the compiler figuring out the static types of variables and arguments so you don’t
 * have to type them, resulting in more concise, and in some cases easier to maintain (refactor) code. Prior to J10
 * there were already a small no. of cases in which Java provided type inference. For example, the diamond operator
 * avoids the need to type generic parameters twice when declaring a collection. The type of the arguments of lambda
 * expressions can also be omitted where no disambiguation is needed. However, it is still the case that when
 * declaring variables, especially intermediate ones of parameterised generic types, needing to repeat the type on
 * the left-hand side of a variable declaration is burdensome. To address this, J10 adds support for local-variable
 * type inference. This removes the need to specify the type in a local-variable declaration, which is instead
 * replaced with the reserved type (not keyword) 'var', and the compiler fills in the appropriate type from the
 * variable initializer.
 * <p>
 * In J10, 'var' can only be used when declaring _local_ variables, and index variables in for-loops. It can NOT be
 * used in other places, including class fields, method (including constructor) params and return types.
 * <p>
 * Use of 'var' is an alternative to relying on your IDE's 'extract local variable' feature. The latter avoids the
 * typing overhead, but still needs to be maintained.
 * <p>
 * Use of 'var' can in some cases reduce as well as improve the readability of code. Use should therefore be reviewed
 * on a case-by-case basis. For example, to improve readability the type of variables returned by methods (rather than
 * created inline) may often be best declared explicitly rather than using var.
 *
 * <h2>Further Reading</h2>
 * <a href="http://openjdk.java.net/jeps/286">JEP 286: Local-Variable Type Inference</a> - The original proposal for
 * this language enhancement. Outlines the motivation, scope and design of the feature.
 * <p>
 * <a href="http://openjdk.java.net/projects/amber/LVTIstyle.html">Style Guidelines for Local Variable Type Inference
 * in Java, Stuart W. Marks  2018-03-22</a> - Eplains some of the tradeoffs of using var, and provides guidelines,
 * principles for its effective use, including further examples.
 * <p>
 * <a href="https://developer.oracle.com/java/jdk-10-local-variable-type-inference">Java 10 Local Variable Type
 * Inference, Urma and Warburton</a> - A more in-depth article/discussion piece on the use of var.
 */
public class LocalVariableTypeInferenceTest {

  /**
   * Provides a simple example of how 'var' can be used as an alternative to the diamond operator when declaring a
   * local variable of a generic type. The diamond operator (introduced in J7) reduces typing by removing the need to
   * duplicate the generic type in the instantiation (right hand side of expression). Whereas, var infers the type of
   * the declared variable from the assigned type.
   */
  @Test
  public void inferTypeOfGenericLocalVariableAsAlternativeToDiamondOperator() {
    // From J7+, the diamond operator can be used to avoid the duplicate declaration of a generic type, e.g.
    final ArrayList<String> listOfStrings = new ArrayList<>();

    // From J10+, the above can be rewritten, by instead inferring the type of local variable from the declaration, e.g.
    final var anotherListOfStrings = new ArrayList<String>();

    assertThat(anotherListOfStrings).isEmpty();
  }

  /**
   * Illustrates that local-variable type inference can also be used when declaring the element in (a traditional or)
   * enhanced for-loop.
   * <p>
   * This example uses a for-loop to process the entries of a Map. This is a case which prior to the introduction
   * of var required a lot of typing / boilerplate, as the type of the entry needed to be repeated.
   */
  @Test
  public void inferTypeOfForLoopIndexVariables() {
    // Pre J10, declare a map of named countries to their named cities -
    final Map<String, List<String>> countryToCity = Map.of(
      "UK", List.of("London", "Manchester", "Birmingham", "Liverpool"),
      "Italy", List.of("Rome", "Turin", "Naples", "Milan")
    );

    // Pre J10, it's necessary to repeat the declaration of the for-loop element, in this case a verbose, generic map
    // entry, and the value of the current Map entry -
    for (Map.Entry<String, List<String>> citiesInCountry : countryToCity.entrySet()) {
      List<String> cities = citiesInCountry.getValue();
      assertThat(cities).isNotEmpty();
    }

    // J10+, use var to infer the type of the created Map -
    // In this case, using var to infer the type does NOT sacrifice readability, as the Map is being created in the
    // same method, so its type is clearly visible and apparent. However, if the code to create the Map were moved to
    // a separate method then the type  of the Map would be less clear, and it would probably be best to use an
    // explicit type for the variable, rather than var.
    final var moreCountryToCity = Map.of(
      "UK", List.of("London", "Manchester", "Birmingham", "Liverpool"),
      "Italy", List.of("Rome", "Turin", "Naples", "Milan")
    );

    // J10+, use var so compiler infers type of for-loop element without losing any readability -
    for (var citiesInCountry : moreCountryToCity.entrySet()) {
      var cities = citiesInCountry.getValue();
      assertThat(cities).isNotEmpty();
    }
  }

  /**
   * Illustrates that use of local-variable type inference is incompatible with writing polymorphic code in a method
   * - using the same object reference to refer to instances of different sub-types - because the compiler always
   * infers the exact type of the assigned instance.
   */
  @Test
  public void varDoesNotSupportPolymorphicCode() {
    // Example class inheritance hierarchy (could equally apply to implementing an interface, as well as sub-classing)
    abstract class Vehicle {
      private String regNumber;
      private Vehicle(String regNumber) {
        this.regNumber = regNumber;
      }
      private String getRegNumber() {return this.regNumber;}
    }
    class Car extends Vehicle {
      private Car(String regNumber) {
        super(regNumber);
      }
    }
    class Lorry extends Vehicle {
      private Lorry(String regNumber) {
        super(regNumber);
      }
    }

    // When declaring the explicit type (not using var) of a variable it's possible to write polymorphic code - the
    // same object reference to a super-type (in  this case a Vehicle), can be used to refer to instance of different
    // sub-types (e.g. Car or Lorry).
    List<Vehicle> vehicles = List.of(new Car("FAB 1"), new Lorry("Trucker 1"));
    List<String> regNumbers = vehicles.stream()
      .map(Vehicle::getRegNumber) // Instances of Car and Lorry, can be treated as Vehicle
      .collect(Collectors.toList());

    // When using var, the inferred type of variable is that of the declared class...
    var vehicle = new Car("130Y R4C3R");
    // ...meaning the variable cannot be used in a polymorphic fashion to refer to instances with a common super-type.
    // The following statement will not compile, due to a type/class mismatch (expecting Car, actual Lorry) -
    //vehicle = new Lorry("Y0RK13S");
  }
}