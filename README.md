[![CircleCI](https://circleci.com/gh/strohs/springboot-grammar-match/tree/master.svg?style=svg)](https://circleci.com/gh/strohs/springboot-grammar-match/tree/master)

[![codecov](https://codecov.io/gh/strohs/springboot-grammar-match/branch/master/graph/badge.svg)](https://codecov.io/gh/strohs/springboot-grammar-match)


Spring Boot JSGF Grammar Matcher
=================================================
This is a Spring Boot REST application that identifies: Dates, Times, Numbers, and (U.S.) currency strings from within
 a string of fully spelled out words. It then converts any successfully recognized strings into a shortened, 
 pretty-printed format (using Java Format strings). 
 
It accepts a string of space separated words as input via HTTP GET or POST, tries to detect dates,times,numbers 
within the input string, and if found, converts them into a "pretty-printed" format.
For example, this string contains a time within it: *"call me back its's nine fifty four pm"* is converted 
 into *"call me back its 9:54pm"*.
 
This project started as an exploration of different ways to recognize the dates and numbers that were getting output 
by a speech recognition engine I was using. This particular engine didn't abbreviate any of the words it returned to you. 
All recognized words were returned completely spelled out. Fully spelled words can be ugly to present to end users, 
and I needed a way to detect them and then format them into a shorter, "nicer looking" string. To help me do this,
I used Java Speech Grammar Files [JSGF](https://www.w3.org/TR/2000/NOTE-jsgf-20000605/) along with the open source
[sphinx recognition engine](https://cmusphinx.github.io/) from Carnegie Mellon University.

### Examples
The input string of words must be fully spelled out (in english) in order for the grammars to successfully "detect" a 
potential date/time/number/currency . No punctuation should be used in the input string.

#### Dates
* dates are u.s. english "format" of month day year
* given the string: `today is november first two thousand eighteen`
    * the grammar matcher returns: `today is Nov 1, 2018`

#### Times
* this grammar keys off the words "pm" or "am"
* given the string: `the current time is five oclock pm`
* the grammar matcher returns: `the current time is 5:00 pm`

#### Numbers
* the number grammar recognizes numbers from 0 to 999_999
* given the string: `you are currently number three hundred fifty four`
    * returns: `you are currently number 354`

#### Currency
* the currency grammar recognizes U.S "dollars" from 1 cent to 999,999 dollars. This particular grammar is actually 
keyed off the words "dollar(s)" or "cent(s)"
* given: `you have twenty two dollars and thirty five cents`
* returns: `you have $22.35`

#### Combine all the Above
* given: `today is october first two thousand nineteen and the current time is eight fifty four pm you are due three thousand dollars`
* returns: `today is Oct, 1, 2019 and the current time is 8:54 pm and you are due $3000`

## Running
This is a Java Gradle project and you'll need at least Java 1.8 installed on your machine. Gradle is optional, as
gradle wrapper is included

* from the project's root directory run: `./gradlew bootRun`
    * this should compile and start the spring boot server
* there are a couple of ways to invoke the application:
    1. open your web browser of choice to: `http://localhost:8080`
        * this will open a HTML page you can use to submit your text string to spring boot
    2. you can call the API directly via HTTP GET or POST, using your browser or other testing tool:
        * for GET requests, you can enter the string of text to match right on the URL as a path parameter:
            * `http://localhost:8080/api/v1/match/please call me at three twenty three pm`
        * for POST requests, you can submit the text string as `text/plain` from the POST body (or as FORM encoded)
* the output will be returned as a JSON object, [MatchResult](src/main/java/com/grammarmatch/domain/MatchResult.java)


### Algorithm Notes
The project uses a brute force algorithm to find sub strings that match a date/time/number/currency. It takes your 
original input string, and breaks it into a sequence of smaller and smaller strings, trying to match each substring 
against a Java Speech Recognition grammar (JSGF). This is not an ideal approach, so you **probably don't** want to 
use this project in a production app! It has quadratic performance which worsens as the input string gets larger!  

[JSGF](https://www.w3.org/TR/2000/NOTE-jsgf-20000605/) is a speech 
recognition technology that is used to enumerate the possible ways something may be spoken by a user. For example,
a user may speak the time "12:00 pm" as "twelve p.m." or "twelve o'clock pm" or "twelve noon". JSGF takes as input, 
a string of text and returns as output a transformed string, indicating the input matched the grammar. If the input did
not match, then some NO_MATCH token is returned or possibly a null value.

JSGF was typically used by (older) speech recognition engines. These engines used it to limit the domain of words 
that they would recognize, thus helping to improve recognition accuracy. Today, there are more powerful recognition
 engines using neural networks and statistical language models that can accurately recognize millions of words.
 



#### JSGF Grammars 
The actual [JSGF grammars](src/main/resources/static/grammars) used for matching are in 
the `resources/static/grammars` directory. Take a look and modify them as needed.