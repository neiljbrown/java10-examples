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

import static org.assertj.core.api.Assertions.assertThat;

// Declare class and test methods as public to support selectively documenting them using Javadoc.
public class ExampleTest {

  @Test
  public void aTest() {
    assertThat(Boolean.TRUE.booleanValue()).isTrue();
  }
}
