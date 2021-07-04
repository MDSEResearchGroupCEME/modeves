# MODEVES
[![Website](https://img.shields.io/website?label=MODEVES.COM&style=for-the-badge&url=https%3A%2F%2Fcodestackr.com)](https://modeves.com)
<br />
The MODEVES introduces a novel framework to support both Static as well as Dynamic Assertion Based Verification for wide-ranging embedded systems. The major features are:
1. A modeling methodology to embed the verification constraints along with the structural and behavioral aspects of embedded systems at higher abstraction level.
2. A transformation engine to automatically generate System Verilog Register Transfer Level (RTL), Timed Automata model, SystemVerilog Assertions (SVAs) and CTL Assertions from high level model.
3. Supports both Static as well as Dynamic Assertion Based Verification (ABV). Particularly, Dynamic ABV of embedded systems can be performed by using the automatically generated SystemVerilog RTL and assertions code through a UVM-compliance (Universal Verification Methodology) simulator. Static ABV of embedded systems can be performed by using the automatically generated Timed Automata Model and CTL assertions code through UPPAAL Tool.
## MODEVES Transformation Engine (MTE)
We develop MODEVES Transformation Engine (MTE) in order to transform the high level models into SystemVerilog RTL, Timed Automata model, SystemVerilog Assertions and CTL assertions code. Currently, we provide source code of MTE for evaluation. However, we will also release it as an Eclipse plugin in near future. The main interface of MTE is given below.
<p align="center"> <img src="http://www.modeves.com/images/MTE1.jpg" width="550" alt="accessibility text"> </p>
Once SystemVerilog RTL, Timed Automata model, SystemVerilog Assertions and CTL assertions code has been generated through MTE, both Static as well as Dynamic Assertion Based Verification has been performed. Few details can be found
<a href="http://www.modeves.com/dvquesta.html">Here.</a>
<br/>

