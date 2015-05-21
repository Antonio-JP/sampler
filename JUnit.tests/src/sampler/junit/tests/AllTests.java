package sampler.junit.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sampler.junit.tests.performance.AbstractionPerformance;

@RunWith(Suite.class)
@SuiteClasses({AbstractionPerformance.class})
public class AllTests {

}
