[![CircleCI](https://circleci.com/gh/strohs/springboot-grammar-match/tree/master.svg?style=svg)](https://circleci.com/gh/strohs/springboot-grammar-match/tree/master)

[![codecov](https://codecov.io/gh/strohs/springboot-grammar-match/branch/master/graph/badge.svg)](https://codecov.io/gh/strohs/springboot-grammar-match)


Spring Boot Grammar Matcher
===============================
A Spring Boot REST application that takes a string of fully spelled out (in English) words, searches for: Dates,
Times,Currencies, Numbers and Ordinal values, and then transforms them into a pretty printed representation.

For Example:

> DATE Example: november first two thousand eighteen --> Nov 1, 2018

> TIME Example: the current time is five o'clock p.m.  --> the current time is 5:00pm

> NUMBER example: you are number three hundred and fifty one --> you are number 351

> ORDINAL (number) example: she came in fifty first place --> she came in 51st place

> CURRENCY example: he has two hundred and fifty four thousand dollars --> he has $254,000

....More to Come