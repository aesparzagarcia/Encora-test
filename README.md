# Fixer-Kotlin

Foreign exchange rates and currency conversion JSON API Fixer is a simple and lightweight API for current and historical foreign exchange (forex) rates.

This project is an Android app which displays data from [fixer.io](https://fixer.io/) Fixer API.

# Architecture
<img width="593" alt="Screen Shot 2022-03-13 at 12 42 04" src="https://user-images.githubusercontent.com/9982524/158074941-61d6b558-46a2-45b2-be03-5eb0a119ec9d.png">


# Features
### 1st screen:
1.- Will load the [Supported Symbols Endpoint](https://fixer.io/documentation#supportedsymbols) and display a vertical list of all supported countries along with their 3-letter currency symbol.
2.- When user start writing on the EditText, the list will start updating each time receives characters.

3.- When the user clicks on a list item (that is selecting a currency symbol), we will open our 2nd screen and use the currency symbol as the "base" currency to convert to multiple other currencies.

![aspiration-1](https://user-images.githubusercontent.com/9982524/158074925-ee445baa-0f23-4ff8-9262-c32a55937d6d.gif)

> NOTE: This app works with Free Plan will only accept EUR as the selected base currency(on ApiService::class.java you can set any base to test error), so the candidate should use EUR to test for the happy path.
When selecting any other currency symbol, the api will send back such an error `{"success":false,"error":{"code":105,"type":"base_currency_access_restricted"}}`. Please parse any error type and display it plainly in an Alert Dialog via DialogFragment.

### 2nd screen contains:
+ 1 EditText will always be fixed at the top that should only accept numbers, in which the user can enter the amount that he/she wants to convert to.
+ 1 RecyclerView to display all converted currency that are returned by the [Latest Rates Endpoint](https://fixer.io/documentation#latestrates). The list should have the 3-letter currency on the left following by the converted amount which should be rounded to the decimal places.

![aspiration-2](https://user-images.githubusercontent.com/9982524/158074929-b0c81251-3124-4c8c-bef5-c36a0b23c0d2.gif)

# Build this project
In the `gradle.properties` assign your `fixerApiUrl` and `fixerApiKey`

- Get the Fixer API KEY [here](https://fixer.io/product)

```java
fixerApiUrl = "\"http://data.fixer.io/api/\""
fixerApiKey = "\"YOU_API_KEY\""
```

# Implementations
- Fully written in [Kotlin](https://kotlinlang.org/) language
- [Retrofit 2](http://square.github.io/retrofit)
- [Dagger 2](https://google.github.io/dagger/)
- [RxJava 2](https://github.com/ReactiveX/RxJava)
- [Groupie](https://github.com/lisawray/groupie)
- Other support libraries Androidx - AppCompat, RecyclerView, CardView, Design
