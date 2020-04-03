# Project

##### CLEF/DATA/

This directory contains the input and output datasets used for experimentations.

Three different datasets are studied here:
1. Product comparison matrices of Wikipedia, taken from the [software comparison category](https://en.wikipedia.org/wiki/Category:Software_comparisons)
2. Excerpts of a product comparison matrix representing [JHipster 3.6.1 variants](https://github.com/xdevroey/jhipster-dataset/tree/master/v3.6.1.) 
3. A product comparison matrix gathering information about [bot variants developed by the Robocode community](https://github.com/but4reuse/RobocodeSPL_teaching) 

###### Input datasets

* **0\_raw\_PCMs/** contains .CSV  files representing "raw" PCMs, i.e., as extracted from Wikipedia before manual cleaning.
* **0\_clean\_PCMs/** contains .CSV  files representing the same previous PCMs of Wikipedia after manual cleaning (see [PCM](https://gite.lirmm.fr/jcarbonnel/CLEF/blob/master/Documentation/ProductComparisonMatrix.md)). It also contains the PCMs about JHipster and Robocode which are already cleaned.


###### Output datasets

All other directories are automatically generated with CLEF.

Each on of them contains:
* An .RCFT file representing the formal context obtained from the PCM.
* A directory **varibility/** containing 4 text files respectivelly representing the extracted binary implications (transitive closure + transitive reduction), co-occurrences and mutex.
* A directory **FCA/** containing the .DOT files representing the associated concept lattice and AC-poset.

##### CLEF/SRC/

This directory contains 4 main packages:

1. A package **main**, containing the class _testExtraction_ having a method main.

2. A package **productdescription**, containing classes to manipulate complex variant descriptions as product comparison matrices.
A _ProductMatrix_ possesses a list of _Characteristics_, which can be boolean _Feature_ or multi-valued _Attribute_.
_CharacteristicType_ represents the 4 type of characteristics: a _Feature_ is mandatorily a type Boolean, and an _Attribute_ can be of type Integer, Double or Literal.

An _Attribute_ possesses an _AbstractOntology_ automatically built on its value set. 
All _Characteristic_ possess a method _scaling()_ which takes a _ProductMatrix_ and return the same _ProductMatrix_ augmented with new boolean features representing the binary scaling of the current characteristic. 
If the characteristic is a _Feature_, this feature is added to the returned matrix.
If the characteristic is an _Attribute_, a feature is created for each value of its associated taxonomy and added to the returned matrix.

The binary scaled matrix is then saved in an .RCFT file (i.e., representing a formal context) and the tool RCAExplore is called to compute the assocatied AC-poset.
.RCFT files are saved in CLEF/data/pcm\_name/pcm\_name.rcft, and the computed AC-poset is saved in CLEF/data/pcm\_name/FCA/AC-poset/step0-0.dot.

To obtain a .PDF file representing the AC-poset from the .DOT file:

```
dot -Tpdf step0-0.dot -o ac-poset.pdf
```

3. A package **taxonomy** containing classes to automatically build taxonomies based on an _Attribute_ value set.
The type of the taxonomy depends on the type of the _Attribute_.

4. A package **variabilityextraction**  containing 4 classes to extract variability information from the AC-poset built from the product comparison matrix.


##### Running CLEF

The main file is  CLEF/src/main/testExtraction.java 

* To initialise a product matrix from a cleaned PCM, one has to indicate the corresponding .CSV file to be processed.
The .CSV file has to be in CLEF/data/0\_clean\_PCMs/.
For instance, to initialise a product matrix from the file representing the excerpt of 500 JHipster variants: 

```java
ProductMatrix pm = new ProductMatrix("jhipster3.6.1-testresults_500.csv");
```

* The method _computeLattice()_ computes the conceptual structures corresponding to the matrix.
This method first applies the binary scaling on each multi-valued attribute of the matrix to produce an boolean matrix.
An .RCFT file is computed from the obtained boolean matrix and then saved.
The tool RCAExplore, that computes conceptual structures from .RCFT files is then called.

```java
pm.computeLattice();
```

* To extract the binary implications from the obtained AC-poset, an instance of BinaryImplicationExtractor retrieves the AC-poset in the directory specified in parameter.
 The methode _computeRelationships()_ has to be called first.
If one wants to save the extracted relationships in a text file, the method _exportsInTextFile()_ has to be called

```java
String path = "data/" + pm.getName() + "/";
BinaryImplicationExtractor bie = new BinaryImplicationExtractor(path);
bie.computeRelationships();
bie.exportsInTextFile();
```

* The same process is applicable to _AllBinaryImplicationExtractor_, _CooccurrenceExtractor_ and _MutexExtractor_.
