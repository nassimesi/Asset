# Product Comparison Matrices

Product comparison matrices (PCMs) are matrices that depict a set of products against a set of their characteristics, hence representing a set of products of the same family in a way that eases their comparison.
They gather in their cells heterogeneous data, including both binary attributes (yes/no values that can be considered as features) and multi-valued attributes.
PCMs can represent software families (for instance, see the [software comparison category](https://en.wikipedia.org/wiki/Category:Software_comparisons) on wikipedia).
Therefore, we consider them being interesting candidates for the extraction of (extended) variability information.

The main drawback of using PCMs as input dataset lies in the fact that they are not formalised and in most cases they need to be cleaned before being automatically processed:

> Despite their apparent simplicity, PCMs contain heterogeneous, ambiguous, uncontrolled and partial information that hinders their efficient exploitation.

We rely on the work of Bécan et al. and Sannier et al. about PCM formalisation to define the input format of our PCMs for our experiments.

### References

> Sannier Nicolas, Acher Mathieu and Baudry Benoit. 2013. From Comparison Matrix to Variability Model: The Wikipedia Case Study. In _28th IEEE/ACM International Conference on Automated Software Engineering_

> Bécan Guillaume, Sannier Nicolas, Acher Mathieu, Barais Olivier, Blouin Arnaud and Baudry Benoit. 2014. Automating the Formalization of Product Comparison Matrices. In _29th IEEE/ACM International Conference on Automated Software Engineering_

## Format

### Layout

It may happen that some columns in a PCM share a cell, or that some cells are missing. For our experiments, we consider rectangular matrices, i.e., where the intersection of a column and a row represents exactly one cell. This will leave no place for ambiguity and ease the automated processing.

Also, most of the times the set of characteristics are depicted in the first row, and the set of products in the first column. However, there can be PCMs where this is not the case. We will process PCMs respecting the default representation, once again to ease the automated processing.

### Cell values

By studying the two aforementioned references on PCM formalisation, we can identify 7 types of cell values in raw PCMs:

 - _Boolean_: yes/no values stating if the product possesses the characteristic or not
 - _Single-value_: giving one value for the characteristic
 - _Multi-values_: giving several values for the characteristic
 - _Partial_ or _conditional_: giving a value under certain conditions (not true all the time)
 - _Unknown_: stating explicitly that the information is unknown
 - _Empty_: giving no value 
 - _Inconsistent_: giving a value that is not consistent with the other existing values of the characteristic (e.g., a string value whereas all other values are dates)

Single and multi values can be of types Integer, Double or String (representing a variability concept, e.g., a product or a feature).

In our experiments, we consider software families described by binary features and multi-valued attributes. Binary features can be seen as attributes with values of boolean types. Multi-valued attributes may have a set of values of types Integer, Double or String. As we use the Pattern Structure framework, we replace the partial, unknown, empty and inconsistent values of raw PCMs by the Pattern Structure value "*", which states that there is no information.

Therefore, as input for our experiments, a cleaned PCM's cell may contained:

 - _Boolean valued_: representing if the product posseses the characteristic represented by the column or not. These characteristics are thus considered as features
 - _Non-empty set of values_: representing the value(s) of the characteristics. These characteristics are thus considered as multi-valued attributes with a domain of types Integer, Double or String
 - _"*"_: represneting the absence of information

### Value consistency

The last issue of automated processing of PCMs are the fact that some cells may contain the same information but represented differently.
Therefore, it is important to use the same keyword to represent the same concept, so they can be considered equals during the automated processing.
For instance, a characteristic displaying the cost of a software could give the value "0€" for a software, and "Free" for another one. 
To be able to automatically group these software into the "free software category" we need to give them the same value, "0€" for example.