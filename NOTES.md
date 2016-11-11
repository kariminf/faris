# Faris Notes

Here some notes about the developpment and the use.

## STON

- The actions dependent to another must come after. This constraint is useful in case of someone is thinking about someone elses thoughts.
```
act:{
    id: was;
    syn: 2604760;
    tns: PA;
    agt: [food];
    thm: [+goodfood]
act:}
act:{
    id: think;
    tns: PR;
    syn: 631737;
    agt: [mother];
    thm: [was]
act:}
```
