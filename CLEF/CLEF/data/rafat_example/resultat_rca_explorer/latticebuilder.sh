#!/bin/bash

for i in {0..1};
do
    for j in {0..0};
    do
	   dot -Tpdf step$i-$j.dot -o step$i-$j.pdf;
    done
done