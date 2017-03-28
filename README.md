# Faris

[![Project](https://img.shields.io/badge/Project-Faris-4B0082.svg)](https://github.com/kariminf/Faris)
[![License](https://img.shields.io/badge/License-Apache_2-4B0082.svg)](http://www.apache.org/licenses/LICENSE-2.0)
<!---
[![Travis](https://img.shields.io/travis/kariminf/Faris.svg)](https://travis-ci.org/kariminf/Faris)
[![codecov](https://img.shields.io/codecov/c/github/kariminf/Faris.svg)](https://codecov.io/gh/kariminf/Faris)
[![jitpack](https://jitpack.io/v/kariminf/Faris.svg)](https://jitpack.io/#kariminf/Faris)
 -->

Faris is a framework intended to represent ideas in sentences, and arrange them according to their factualness level (truth level).
The representation must be MultiLingual, since ideas have nothing to do with language.

Keywords:
Knowledge representation, Sentences, Language modeling, MultiLingual,

## Name meaning

* FAris: [FARabi](http://en.wikipedia.org/wiki/Al-Farabi) & [ARIStotle](http://en.wikipedia.org/wiki/Aristotle).
The concepts used in this project came from Aristotle's categories of beings.
[AlFarabi follows the same categorization](http://www.sacred-texts.com/isl/palf/palf06.htm).

* FARIS: Factual Arrangement and Representation of Ideas in Sentences.
Idea's are arranged according to their truth level (Mental State: think, believe).

* Faris: (فارس) A knight in Arabic, and also a male name.

## Features


The core functions of Faris is to represent different ideas in sentences:
* Using three layers for representation:
  * **Linguistic:** these are the parts of speech, such as verb, noun, adjective, etc.
  * **Philosophical:** based on Aristotle's categories of beings, such as action, substance, time, place, etc.
  * **Knowledge:** each idea can be a simple thought with one action, a conditional one or an opinion about what others think.
  A mind can hold many ideas which are grouped by their Mental State.
* @FIX Communicating (input/output): Faris uses STON as an input/output format.
* @TODO Browsing mechanisms through all Faris structures, which can be used to extract just a subset of ideas.

The reasoning functions of Faris:
* Detecting duplicate substances, actions and ideas.
* @TODO Generalization: substances, actions and ideas.
* @TODO Conflicting information detection
* @TODO Generalization of truth: if something is believed by many, it might be true.

## License

Copyright (C) 2016-2017 Abdelkrime Aries

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
