#!/bin/bash

for i in {0..2};
do
    for j in {0..4};
    do
	   dot -Tpdf step$i-$j.dot -o step$i-$j.pdf;
    done
done