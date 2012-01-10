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
package org.pitest.mutationtest;

import static org.pitest.functional.Prelude.and;
import static org.pitest.functional.Prelude.not;
import static org.pitest.functional.Prelude.or;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import org.pitest.coverage.execute.CoverageOptions;
import org.pitest.extension.Configuration;
import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.Prelude;
import org.pitest.functional.predicate.Predicate;
import org.pitest.internal.ClassPath;
import org.pitest.internal.PathNamePredicate;
import org.pitest.internal.classloader.ClassPathRoot;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.instrument.PercentAndConstantTimeoutStrategy;
import org.pitest.mutationtest.report.OutputFormat;
import org.pitest.testng.TestGroupConfig;
import org.pitest.util.Glob;

public class ReportOptions {

  private Configuration                              config;
  private Collection<Predicate<String>>              classesInScope;
  private Collection<Predicate<String>>              targetClasses;
  private Collection<Predicate<String>>              excludedMethods          = Collections
                                                                                  .emptyList();

  private Collection<Predicate<String>>              excludedClasses          = Collections
                                                                                  .emptyList();

  private Collection<String>                         codePaths;

  private String                                     reportDir;
  private Collection<File>                           sourceDirs;
  private Collection<String>                         classPathElements;
  private Collection<? extends MethodMutatorFactory> mutators;

  private int                                        dependencyAnalysisMaxDistance;
  private boolean                                    mutateStaticInitializers = true;

  private final List<String>                         jvmArgs                  = new ArrayList<String>();
  private int                                        numberOfThreads          = 0;
  private float                                      timeoutFactor            = PercentAndConstantTimeoutStrategy.DEFAULT_FACTOR;
  private long                                       timeoutConstant          = PercentAndConstantTimeoutStrategy.DEFAULT_CONSTANT;

  private Collection<Predicate<String>>              targetTests;

  private Collection<String>                         loggingClasses           = new ArrayList<String>();

  private int                                        maxMutationsPerClass;

  private boolean                                    verbose                  = false;
  private boolean                                    failWhenNoMutations      = false;

  private final Collection<OutputFormat>             outputs                  = new LinkedHashSet<OutputFormat>();

  private TestGroupConfig                            groupConfig;

  public ReportOptions() {
  }

  public boolean isVerbose() {
    return this.verbose;
  }

  @SuppressWarnings("unchecked")
  public Predicate<String> getClassesInScopeFilter() {
    return and(or(this.classesInScope), not(isBlackListed()));
  }

  public void setClassesInScope(
      final Collection<Predicate<String>> classesInScope) {
    this.classesInScope = classesInScope;
  }

  /**
   * @return the reportDir
   */
  public String getReportDir() {
    return this.reportDir;
  }

  /**
   * @param reportDir
   *          the reportDir to set
   */
  public void setReportDir(final String reportDir) {
    this.reportDir = reportDir;
  }

  /**
   * @return the sourceDirs
   */
  public Collection<File> getSourceDirs() {
    return this.sourceDirs;
  }

  public Collection<String> getClassPathElements() {
    return this.classPathElements;
  }

  public void setClassPathElements(final Collection<String> classPathElements) {
    this.classPathElements = classPathElements;
  }

  /**
   * @param sourceDirs
   *          the sourceDirs to set
   */
  public void setSourceDirs(final Collection<File> sourceDirs) {
    this.sourceDirs = sourceDirs;
  }

  /**
   * @return the mutators
   */
  public Collection<? extends MethodMutatorFactory> getMutators() {
    return this.mutators;
  }

  /**
   * @param mutators
   *          the mutators to set
   */
  public void setMutators(
      final Collection<? extends MethodMutatorFactory> mutators) {
    this.mutators = mutators;
  }

  /**
   * @return the dependencyAnalysisMaxDistance
   */
  public int getDependencyAnalysisMaxDistance() {
    return this.dependencyAnalysisMaxDistance;
  }

  /**
   * @param dependencyAnalysisMaxDistance
   *          the dependencyAnalysisMaxDistance to set
   */
  public void setDependencyAnalysisMaxDistance(
      final int dependencyAnalysisMaxDistance) {
    this.dependencyAnalysisMaxDistance = dependencyAnalysisMaxDistance;
  }

  public List<String> getJvmArgs() {
    return this.jvmArgs;
  }

  public void addChildJVMArgs(final List<String> args) {
    this.jvmArgs.addAll(args);
  }

  public ClassPath getClassPath() {
    if (this.classPathElements != null) {
      return createClassPathFromElements();
    } else {
      return new ClassPath();
    }
  }

  private ClassPath createClassPathFromElements() {
    return new ClassPath(
        FCollection.map(this.classPathElements, stringToFile()));
  }

  private F<String, File> stringToFile() {
    return new F<String, File>() {

      public File apply(final String a) {
        return new File(a);
      }

    };
  }

  public Collection<Predicate<String>> getTargetClasses() {
    return this.targetClasses;
  }

  @SuppressWarnings("unchecked")
  public Predicate<String> getTargetClassesFilter() {
    return Prelude.and(or(this.targetClasses), not(isBlackListed()));
  }

  public void setTargetClasses(final Collection<Predicate<String>> targetClasses) {
    this.targetClasses = targetClasses;
  }

  public void setTargetTests(
      final Collection<Predicate<String>> targetTestsPredicates) {
    this.targetTests = targetTestsPredicates;
  }

  public boolean hasValueForClassesInScope() {
    return (this.classesInScope != null) && !this.classesInScope.isEmpty();
  }

  public boolean isMutateStaticInitializers() {
    return this.mutateStaticInitializers;
  }

  public void setMutateStaticInitializers(final boolean mutateStaticInitializers) {
    this.mutateStaticInitializers = mutateStaticInitializers;
  }

  public int getNumberOfThreads() {
    return this.numberOfThreads;
  }

  public void setNumberOfThreads(final int numberOfThreads) {
    this.numberOfThreads = numberOfThreads;
  }

  public float getTimeoutFactor() {
    return this.timeoutFactor;
  }

  public long getTimeoutConstant() {
    return this.timeoutConstant;
  }

  public void setTimeoutConstant(final long timeoutConstant) {
    this.timeoutConstant = timeoutConstant;
  }

  public void setTimeoutFactor(final float timeoutFactor) {
    this.timeoutFactor = timeoutFactor;
  }

  public Collection<Predicate<String>> getTargetTests() {
    return this.targetTests;
  }

  @Override
  public String toString() {
    return "ReportOptions [isValid=" + ", classesInScope="
        + this.classesInScope + ", targetClasses=" + this.targetClasses
        + ", reportDir=" + this.reportDir + ", sourceDirs=" + this.sourceDirs
        + ", classPathElements=" + this.classPathElements + ", mutators="
        + this.mutators + ", dependencyAnalysisMaxDistance="
        + this.dependencyAnalysisMaxDistance + ", mutateStaticInitializers="
        + this.mutateStaticInitializers + ", showHelp=" + ", jvmArgs="
        + this.jvmArgs + ", numberOfThreads=" + this.numberOfThreads
        + ", timeoutFactor=" + this.timeoutFactor + ", timeoutConstant="
        + this.timeoutConstant + ", targetTests=" + this.targetTests
        + ", loggingClasses=" + this.loggingClasses + "]";
  }

  @SuppressWarnings("unchecked")
  public Predicate<String> getTargetTestsFilter() {
    if ((this.targetTests == null) || this.targetTests.isEmpty()) {
      return not(isBlackListed());
    } else {
      return Prelude.and(or(this.targetTests), not(isBlackListed()));
    }

  }

  private Predicate<String> isBlackListed() {
    return new Predicate<String>() {

      public Boolean apply(final String a) {
        return or(ReportOptions.this.excludedClasses).apply(a);
      }

    };
  }

  public Collection<String> getLoggingClasses() {
    if (this.loggingClasses.isEmpty()) {
      return DefaultMutationConfigFactory.LOGGING_CLASSES;
    } else {
      return this.loggingClasses;
    }
  }

  public void setLoggingClasses(final Collection<String> loggingClasses) {
    this.loggingClasses = loggingClasses;
  }

  public Collection<Predicate<String>> getExcludedMethods() {
    return this.excludedMethods;
  }

  public void setExcludedMethods(
      final Collection<Predicate<String>> excludedMethods) {
    this.excludedMethods = excludedMethods;
  }

  public int getMaxMutationsPerClass() {
    return this.maxMutationsPerClass;
  }

  public void setMaxMutationsPerClass(final int maxMutationsPerClass) {
    this.maxMutationsPerClass = maxMutationsPerClass;
  }

  public void setVerbose(final boolean verbose) {
    this.verbose = verbose;
  }

  public void setExcludedClasses(
      final Collection<Predicate<String>> excludedClasses) {
    this.excludedClasses = excludedClasses;
  }

  public void addOutputFormats(final Collection<OutputFormat> formats) {
    this.outputs.addAll(formats);
  }

  public Iterable<OutputFormat> getOutputFormats() {
    return this.outputs;
  }

  public Collection<Predicate<String>> getExcludedClasses() {
    return this.excludedClasses;
  }

  public Collection<Predicate<String>> getClassesInScope() {
    return this.classesInScope;
  }

  public boolean shouldFailWhenNoMutations() {
    return this.failWhenNoMutations;
  }

  public void setFailWhenNoMutations(final boolean failWhenNoMutations) {
    this.failWhenNoMutations = failWhenNoMutations;
  }

  public void addClassPathElements(
      final List<String> additionalClassPathElements) {
    final List<String> elements = new ArrayList<String>();
    FCollection.mapTo(ClassPath.getClassPathElementsAsFiles(), fileToString(),
        elements);
    elements.addAll(additionalClassPathElements);
    setClassPathElements(elements);
  }

  private static F<File, String> fileToString() {
    return new F<File, String>() {

      public String apply(final File a) {
        return a.getAbsolutePath();
      }

    };
  }

  @SuppressWarnings("unchecked")
  public CoverageOptions createCoverageOptions() {
    return new CoverageOptions(Prelude.and(this.getTargetClassesFilter(),
        this.getClassesInScopeFilter(), not(commonClasses())), this.config,
        this.isVerbose(), this.getDependencyAnalysisMaxDistance());
  }

  private static F<String, Boolean> commonClasses() {
    return new F<String, Boolean>() {

      public Boolean apply(final String name) {
        return name.startsWith("java") || name.startsWith("sun/")
            || name.startsWith("org/junit") || name.startsWith("junit");
      }

    };
  }

  public MutationClassPaths getMutationClassPaths() {

    return new MutationClassPaths(this.getClassPath(), createClassesFilter(),
        createPathFilter());
  }

  public ClassFilter createClassesFilter() {
    return new ClassFilter(this.getClassesInScopeFilter(),
        this.getTargetTestsFilter(), this.getTargetClassesFilter());
  }

  public PathFilter createPathFilter() {
    return new PathFilter(createCodePathFilter(),
        not(new DefaultDependencyPathPredicate()));
  }

  private Predicate<ClassPathRoot> createCodePathFilter() {
    if (this.codePaths != null && !this.codePaths.isEmpty()) {
      return new PathNamePredicate(Prelude.or(Glob
          .toGlobPredicates(this.codePaths)));
    } else {
      return new DefaultCodePathPredicate();
    }
  }

  public Collection<String> getCodePaths() {
    return this.codePaths;
  }

  public void setCodePaths(final Collection<String> codePaths) {
    this.codePaths = codePaths;
  }

  public void setConfiguration(final Configuration configuration) {
    this.config = configuration;
  }

  public void setGroupConfig(final TestGroupConfig groupConfig) {
    this.groupConfig = groupConfig;
  }

  public TestGroupConfig getGroupConfig() {
    return this.groupConfig;
  }

}
