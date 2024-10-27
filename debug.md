# Debug doc for database creation using the reference genome of _Chrysocyon brachyurus_

## Problem
When running the database build script downloading chromosomes on Chrysocyon brachyurus chromosome assembly
(use the provided script at `scripts/chryusocyon.sh`) it fails with the following error message:

```
00:00:44 Done: 0 transcripts marked
FATAL ERROR: Most Exons do not have sequences!
```

Before this happens we get messages saying that no exons have been created:

```
00:00:20 Chromosome: 'CM052494.1'       length: 25851948
00:00:20 Create exons from CDS (if needed):
00:00:20 Exons created for 0 transcripts.
00:00:20 Deleting redundant exons (if needed):
00:00:20        Total transcripts with deleted exons: 0
00:00:20 Collapsing zero length introns (if needed):
00:00:20        Total collapsed transcripts: 0
00:00:20                Adding genomic sequences to genes:
00:00:20        Done (0 sequences added).
00:00:20                Adding genomic sequences to exons:
00:00:20        Done (0 sequences added, 0 ignored).
```

## Ideas
Set breakpoint at snpeff.interval.interval.Interval:258 (in `size()`) and check the stack
trace each time this method is called. Also check whether the returned value is > 1.
The intializer values for `start` and `end` properties of the `Interval` class
are both `-1` and the size is computed this way:

```java
public int size() {
    return end - start + 1;
}
```

So `1` could be either the initializer value or it could mean that something in the `Genome`
contructor didn't find any (coding?) sequence in the chromosome.
If this is called > 1 times, that would probably mean that the first values that we get
in `org.snpeff.snpEffect.factory.SnpEffPredictorFactory.<init>` when we stringify value
from `config.getGenome()` are actually just init values.

## Debug commands
```bash
jdb -sourcepath src/main/java -classpath SnpEFF.jar org.snpeff.SnpEff build -genbank -v GCA_028533335
```

```
stop in org.snpeff.snpEffect.commandLine.SnpEffCmdBuild.<init>
```
