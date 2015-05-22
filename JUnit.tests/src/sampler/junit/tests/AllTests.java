package sampler.junit.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sampler.junit.tests.performance.AbstractionPerformance;
import sampler.junit.tests.performance.JDTASTPerformance;

@RunWith(Suite.class)
@SuiteClasses({AbstractionPerformance.class, JDTASTPerformance.class})
public class AllTests {

}
