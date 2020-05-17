package org.snpeff.snpEffect.testCases.integration;

import org.junit.Test;
import org.snpeff.snpEffect.commandLine.SnpEffCmdBuild;
import org.snpeff.snpEffect.commandLine.SnpEffCmdProtein;
import org.snpeff.util.Gpr;

import junit.framework.Assert;

/**
 * Test COVID19 build
 *
 * @author pcingola
 */
public class TestCasesIntegrationCovid19 extends TestCasesIntegrationBase {

	public TestCasesIntegrationCovid19() {
		super();
		testsDir = "tests/integration/covid19/";
	}

	@Test
	public void test_01() {
		Gpr.debug("Test");
		verbose = true;
		String genome = "test_NC_045512_01";
		SnpEffCmdBuild buildCmd = buildGetBuildCmd(genome);

		// Make sure all proteins are OK
		SnpEffCmdProtein protCmd = buildCmd.getSnpEffCmdProtein();
		Assert.assertEquals(5, protCmd.getTotalOk());
		Assert.assertEquals(0, protCmd.getTotalErrors());
		Assert.assertEquals(0, protCmd.getTotalWarnings());
	}
}
