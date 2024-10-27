# Debug doc for database creation using the reference genome of _Chrysocyon brachyurus_

## Problem
When running the database build script downloading chromosomes on Chrysocyon brachyurus chromosome assembly
(use the provided script at `scripts/chryusocyon.sh`) it fails with the following error message:

## Debug commands
```bash
jdb -sourcepath src/main/java -classpath SnpEFF.jar org.snpeff.SnpEff build -genbank -v GCA_028533335
```

```
stop in org.snpeff.snpEffect.commandLine.SnpEffCmdBuild.<init>
```

## Ideas
Set breakpoint at snpeff.interval.interval.Interval:258 (in `size()`) and check the stack
trace each time this method is called. Also check whether the returned value is > 1.
If this is called > 1 times, that would probably mean that the first values that we get
in `org.snpeff.snpEffect.factory.SnpEffPredictorFactory.<init>` when we stringify value
from `config.getGenome()` are actually just init values.
