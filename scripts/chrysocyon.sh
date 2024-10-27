#!/bin/bash

# This is a script that downloads nucletide sequences for Chrysocyon brachyrus

for i in {58..95}; do
  id="CM0524$i.1"
  echo "Downloading $id..."
  curl "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&id=$id&rettype=gbwithparts&retmode=text" >> config/data/GCA_028533335.1/genes.gbk
  echo "Done."
done
