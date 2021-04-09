
# Human Genomes

There are several version of the human genome supported by SnpEff

Here we explain the subtle differences between each version:


### Who is who

Here is a brief explanation of who are the key releases of the Human Genome (all quotes are from their respective web sites, at the time I created this page):

**[Ensembl](http://www.ebi.ac.uk/):**:
- *“Ensembl creates, integrates and distributes reference datasets and analysis tools that enable genomics. We are based at EMBL-EBI (European Molecular Biology Laboratory, European Bioinformatics Institute)”*
- *“Ensembl transcripts displayed on our website are products of the Ensembl automatic gene annotation system (a collection of gene annotation pipelines), termed the Ensembl annotation process. All Ensembl transcripts are based on experimental evidence and thus the automated pipeline relies on the mRNAs and protein sequences deposited into public databases from the scientific community. Manually-curated transcripts are produced by the HAVANA group.”*


**[RefSeq:](https://www.ncbi.nlm.nih.gov/refseq/)**
- Provided by NCBI (National Center for Biotechnology Information)
- *“The Reference Sequence (RefSeq) collection provides a comprehensive, integrated, non-redundant, well-annotated set of sequences, including genomic DNA, transcripts, and proteins”*


**[Gencode](https://www.gencodegenes.org/):**
- *“The goal of the GENCODE project is to identify and classify all gene features in the human and mouse genomes with high accuracy based on biological evidence, and to release these annotations for the benefit of biomedical research and genome interpretation.”*
- *“The GENCODE annotation is made by merging the manual gene annotation produced by the Ensembl-Havana team and the Ensembl-genebuild automated gene annotation. … The GENCODE releases coincide with the Ensembl releases…. In practical terms, the GENCODE annotation is essentially identical to the Ensembl annotation.”*

**[GRCh]([https://www.ncbi.nlm.nih.gov/grc/human])**: Genome Reference Consortium (human) 


### Human Genome versions

Which Human genome version correspond to which genome names:

- GRCh38.*NN* (e.g. GRCh38.103): These are genome annotations from ENSEMBL, created from GRCh38/hg38 reference genome sequence.
 
- GRCh37.*NN* (e.g. GRCh37.75): These are the genome annotations from ENSEMBL, created from GRCh37/hg19 reference genome sequence. **WARNING:** Ensembl stopped releasing genomes based on GRCh37/hg19 on February 2014.

- GRCh38.p*NN* (e.g. GRCh38.p13): These are RefSeq transcripts from NCBI mapped to GRCh38/hg38 reference genome sequence

- GRCh37.p*NN* (e.g. GRCh37.p13): These are RefSeq transcripts from NCBI mapped to GRCh38/hg19 reference genome sequence

- hg38: This is a genome that uses RefSeq transcripts mapped to GRCh38/hg38 reference genome sequence

- hg19: This is a genome that uses RefSeq transcripts mapped to GRCh38/hg38 reference genome sequence


### Important considerations

There are some things you need to consider when looking at genomic variants results.

- **Do not mix genome versions.** It is important not to confuse different genome versions when comparing results. For example, if you use SnpEff to annotate variants using GRCh38.103 (from ENSEMBL) and then look at the variant using UCSC's genome browser (which uses RefSeq transcripts) there might be differences because your are using different transcripts set, thus the variant annotations may not match.

- **Canonical is ill-defined:** Everybody has a different definition of what a canonical transcript is (see details in the next section).
 
### Important considerations: RefSeq

These are some considerations to keep in mind while working with RefSeq transcripts, this includes SnpEff genomes hg19, hg38, GRCh38.p13, GRCh37.p13, etc.

- **RefSeq transcripts may NOT match the reference genome.** This is a surprise for a lot of people, but RefSeq was designed as a consensus of transcript sequences as opposed as predicted from the reference genome. As a result a RefSeq transcript may not match the reference genome.

- **RefSeq transcripts differ ~5% respect to the reference genome.** This is a consequence of the previous item. Between 3% to 7% of the transcripts in RefSeq do not exactly match the reference genome, thus the proteins inferred from the genomic CDSs sequences are different than the "real" RefSeq CDS sequences. Most of the time, the difference (if any) is only one amino acid in the whole protein, but sometimes the difference is much larger.

- **Variant annotations using RefSeq may not be precise at the exact loci where the RefSeq transcript doesn't match the genome reference.** This is yet another consequence of the previous items, but since the transcript do not match the reference genome, and variant annotations are based on the reference genome, the variant annotaion predictions might be off at those genomic loci.

- **NCBI's gene IDs are just gene names, simetime with '_1', '_2', ..., etc.** Gene IDs from NCBI genomes (e.g. GRCh38.p13) are just gene names. If a gene is mapped to multiple genomic loci, then the same gene name is used and string is added to make it unique ('_1', for the first duplicate, '_2' for the second and so on). For example, here are the gene IDs for gene 'KIR3DL3' (note the last line is 'KIR3DL3_46', so there are 47 loci for this gene):

```
# Note: Results edited for readability
$ grep "gene\t" GCF_000001405.39_GRCh38.p13_genomic.gtf | grep KIR3DL3
NC_000019.10    BestRefSeq  gene    54724442    54736632    .   +   .   gene_id "KIR3DL3"; ...
NW_016107300.1  BestRefSeq  gene    26066   38262   .   +   .   gene_id "KIR3DL3_1"; ...
NW_016107301.1  BestRefSeq  gene    26066   38253   .   +   .   gene_id "KIR3DL3_2"; ...
NW_016107302.1  BestRefSeq  gene    26075   38226   .   +   .   gene_id "KIR3DL3_3"; ...
NW_016107303.1  BestRefSeq  gene    26066   38222   .   +   .   gene_id "KIR3DL3_4"; ...
NW_016107304.1  BestRefSeq  gene    26066   38255   .   +   .   gene_id "KIR3DL3_5"; ...
NW_016107305.1  BestRefSeq  gene    26072   38219   .   +   .   gene_id "KIR3DL3_6"; ...
...
NT_187686.1 BestRefSeq  gene    177446  189666  .   -   .   gene_id "KIR3DL3_44"; ...
NT_187687.1 BestRefSeq  gene    132277  144472  .   -   .   gene_id "KIR3DL3_45"; ...
NT_113949.2 BestRefSeq  gene    139138  151310  .   -   .   gene_id "KIR3DL3_46"; ...
```

- **UCSC transcripts (hg19/hg38) are not unique.** Transcript IDs might not be unique. Many assume that IDs are unique, but this is not always true to UCSC's genomic files. 

- **A UCSC transcripts (hg19/hg38) can map to multiple loci.** A transcript from hg19/hg39 can map to multiple genomic loci, this is a consequence of transcripts IDs not being unique. For example, transcript NR_110738.1 is mapped to 123 loci:

```
$ cat hg38.refseq | cut -f 2 | sort | uniq -c | sort -rn | head
 123 NR_110738.1
  92 NR_110737.1
  92 NM_014218.3
  91 NM_001368251.1
  71 NM_001281972.2
  54 NM_001291696.1
  54 NM_001281971.2
  53 NM_014513.2
  53 NM_001360171.1
  52 NM_014512.1
 ```


### Canonical transcripts

You need to be careful because the definition of "Canonical trnascript" changes for each data source and sometimes for each genome version.

The definition used by SnpEff is: *“The canonical transcript is defined as either the longest CDS, if the gene has translated transcripts, or the longest cDNA.”* (Ref: https://www.ncbi.nlm.nih.gov/pmc/articles/PMC2686571)

Just to show that there are subtle differences, here some of the definitions Canonical from some prominent genomic source

- Ensembl (Ref: http://useast.ensembl.org/Help/Glossary)

  1. Longest CCDS translation with no stop codons
  1. longest Ensembl/Havana merged translation with no stop codons
  1. longest translation with no stop codons. 
  1. If no translation, choose the longest non-protein-coding transcript. 
  1. Note: “…does not necessarily reflect the most biologically relevant transcript of a gene”

- UCSC (Ref: https://genome.ucsc.edu/FAQ/FAQgenes.html)

  1. hg19: “Generally, this is the longest isoform.”
  1. hg38: “The canonical transcript is chosen using the APPRIS principal transcript when available. If no APPRIS tag exists for any transcript associated with the cluster, then a transcript in the BASIC set is chosen. If no BASIC transcript exists, then the longest isoform is used”.

- UniProt (Ref: https://www.uniprot.org/help/canonical_and_isoforms)

  1. It is the most prevalent.
  1. It is the most similar to orthologous sequences found in other species.
  1. By virtue of its length or amino acid composition, it allows the clearest description of domains, isoforms, genetic variation, post-translational modifications, etc.
  1. In the absence of any information, we choose the longest sequence.


As you can see these definitions do not match and obviously these differences could affect your analysis.

