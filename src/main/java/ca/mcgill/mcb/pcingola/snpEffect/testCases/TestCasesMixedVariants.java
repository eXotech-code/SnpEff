package ca.mcgill.mcb.pcingola.snpEffect.testCases;

import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import org.junit.Assert;

import ca.mcgill.mcb.pcingola.interval.Chromosome;
import ca.mcgill.mcb.pcingola.interval.Gene;
import ca.mcgill.mcb.pcingola.interval.Genome;
import ca.mcgill.mcb.pcingola.interval.Transcript;
import ca.mcgill.mcb.pcingola.snpEffect.Config;
import ca.mcgill.mcb.pcingola.snpEffect.SnpEffectPredictor;
import ca.mcgill.mcb.pcingola.snpEffect.commandLine.SnpEff;
import ca.mcgill.mcb.pcingola.snpEffect.commandLine.SnpEffCmdEff;
import ca.mcgill.mcb.pcingola.snpEffect.factory.SnpEffPredictorFactoryRand;
import ca.mcgill.mcb.pcingola.vcf.VcfConsequence;
import ca.mcgill.mcb.pcingola.vcf.VcfEffect;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Test mixed variants
 *
 * @author pcingola
 */
public class TestCasesMixedVariants extends TestCase {

	boolean debug = false;
	boolean verbose = true;

	Random rand;
	Config config;
	Genome genome;
	Chromosome chromosome;
	Gene gene;
	Transcript transcript;
	SnpEffectPredictor snpEffectPredictor;
	String chromoSequence = "";
	char chromoBases[];

	public TestCasesMixedVariants() {
		super();
		init();
	}

	boolean compare(List<VcfEffect> effs, List<VcfConsequence> csqs) {
		boolean ok = true;
		for (VcfEffect eff : effs)
			ok &= compare(eff, csqs);

		return ok;
	}

	boolean compare(VcfEffect eff, List<VcfConsequence> csqs) {
		String et = eff.getEffect().toSequenceOntology();

		for (VcfConsequence csq : csqs) {
			// Check in same transcript
			if (csq.feature.equals(eff.getTranscriptId())) {
				String consecuences = csq.consequence;
				for (String cons : consecuences.split("&")) {
					if (et.equals(cons)) {
						if (verbose) System.out.println("\t\tOK :" + eff.getTranscriptId() + "\t" + et + "\t" + cons);
						return true;
					}
					if (verbose) System.out.println("\t\t    " + eff.getTranscriptId() + "\t" + et + "\t" + cons);
				}
			}
		}

		return false;
	}

	/**
	 * Compare with results from ENSEMBL's VEP on transcript ENST00000268124
	 */
	public void compareVep(String genome, String vcf, String trId) {
		String args[] = { genome, vcf };

		SnpEff cmd = new SnpEff(args);
		SnpEffCmdEff cmdEff = (SnpEffCmdEff) cmd.snpEffCmd();
		cmdEff.setVerbose(verbose);
		cmdEff.setSupressOutput(!verbose);
		cmdEff.setDebug(debug);

		List<VcfEntry> vcfEnties = cmdEff.run(true);
		for (VcfEntry ve : vcfEnties) {

			if (verbose) {
				System.out.println(ve);

				System.out.println("\tCSQ:");
				List<VcfConsequence> csqs = VcfConsequence.parse(ve);
				for (VcfConsequence csq : csqs)
					if (verbose) System.out.println("\t\t" + csq);

				System.out.println("\tEFF:");
				List<VcfEffect> effs = ve.parseEffects();
				for (VcfEffect eff : effs)
					if (verbose) System.out.println("\t\t" + eff);

				System.out.println("\tCompare:");
				Assert.assertTrue("EFF and CSQ do not match", compare(effs, csqs));
			}
		}
	}

	void init() {
		initRand();
		initSnpEffPredictor();
	}

	void initRand() {
		rand = new Random(20140808);
	}

	void initSnpEffPredictor() {
		// Create a config and force out snpPredictor for hg37 chromosome Y
		config = new Config("testCase", Config.DEFAULT_CONFIG_FILE);

		// Create factory
		int maxGeneLen = 1000;
		int maxTranscripts = 1;
		int maxExons = 5;
		SnpEffPredictorFactoryRand sepf = new SnpEffPredictorFactoryRand(config, rand, maxGeneLen, maxTranscripts, maxExons);

		// Chromosome sequence
		chromoSequence = sepf.getChromoSequence();
		chromoBases = chromoSequence.toCharArray();

		// Create predictor
		snpEffectPredictor = sepf.create();
		config.setSnpEffectPredictor(snpEffectPredictor);

		// No upstream or downstream
		config.getSnpEffectPredictor().setUpDownStreamLength(0);
		config.getSnpEffectPredictor().setSpliceRegionExonSize(0);
		config.getSnpEffectPredictor().setSpliceRegionIntronMin(0);
		config.getSnpEffectPredictor().setSpliceRegionIntronMax(0);

		// Build forest
		config.getSnpEffectPredictor().buildForest();

		chromosome = sepf.getChromo();
		genome = config.getGenome();
		gene = genome.getGenes().iterator().next();
		transcript = gene.iterator().next();
	}

	//	/**
	//	 * Make sure we can read VCF and parse variants without producing any exception
	//	 */
	//	public void test_01_MixedVep() {
	//		String vcfFile = "tests/mixed_01.vcf";
	//
	//		VcfFileIterator vcf = new VcfFileIterator(vcfFile);
	//		for (VcfEntry ve : vcf) {
	//			if (verbose) System.out.println(ve);
	//			for (Variant var : ve.variants()) {
	//				if (verbose) System.out.println("\t" + var);
	//			}
	//		}
	//	}

	//	public void test_02_MixedVep() {
	//		compareVep("testHg3770Chr22", "tests/mixed_02.vcf", null);
	//	}
	public void test_zzz_MixedVep() {
		compareVep("testHg3770Chr22", "tests/mixed_zzz.vcf", null);
	}

}
