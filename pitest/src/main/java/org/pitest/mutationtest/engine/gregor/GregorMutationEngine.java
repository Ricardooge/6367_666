/*
 * Copyright 2010 Henry Coles
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.pitest.mutationtest.engine.gregor;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.predicate.Predicate;
import org.pitest.internal.ClassByteArraySource;
import org.pitest.mutationtest.MutationConfig;
import org.pitest.mutationtest.config.MutationEngineConfiguration;
import org.pitest.mutationtest.engine.Mutater;
import org.pitest.mutationtest.engine.MutationEngine;

/**
 * The default (and currently only) mutation engine for PIT.
 * 
 * In case anyone was wondering I'm named after either Gregor Mendel or Gregor
 * Samsa, or maybe both.
 */
public class GregorMutationEngine implements MutationEngine {

  private final Set<MethodMutatorFactory> mutationOperators = new LinkedHashSet<MethodMutatorFactory>();
  private final Set<String>               loggingClasses    = new LinkedHashSet<String>();

  private final Predicate<MethodInfo>     methodFilter;

  public GregorMutationEngine(final MutationEngineConfiguration config) {
    this.methodFilter = config.methodFilter();
    this.mutationOperators.addAll(config.mutators());
    this.loggingClasses.addAll(config.doNotMutateCallsTo());
  }

  public Mutater createMutator(final MutationConfig config,
      final ClassByteArraySource byteSource) {
    return new GregorMutater(byteSource, this.methodFilter,
        this.mutationOperators, this.loggingClasses);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime
        * result
        + ((this.mutationOperators == null) ? 0 : this.mutationOperators
            .hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final GregorMutationEngine other = (GregorMutationEngine) obj;
    if (this.mutationOperators == null) {
      if (other.mutationOperators != null) {
        return false;
      }
    } else if (!this.mutationOperators.equals(other.mutationOperators)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "GregorMutationEngine [filter=" + this.methodFilter
        + ", mutationOperators=" + this.mutationOperators + "]";
  }

  public Collection<String> getMutatorNames() {
    return FCollection.map(this.mutationOperators, toName());
  }

  private static F<MethodMutatorFactory, String> toName() {
    return new F<MethodMutatorFactory, String>() {

      public String apply(final MethodMutatorFactory a) {
        return a.getName();
      }

    };
  }

}
