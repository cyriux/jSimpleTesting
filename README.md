# jSimpleTesting

A Java simplified example of testing Event Sourcing systems, inspired by gregoryyoung/Simple.Testing

Consider the following business scenarios (and test cases as well) expressed directly with the domain events and commands (example taken from Brian Donahue from the dddcqrs mailing list):

```
@Test
	public void eatHalfOfTheCookiesBatch() {
		scenario("Eat 10 of the 20 cookies of the batch")
			.Given(new BatchCreatedWithCookies(20))
			.When(new EatCookies(10))
			.Then(new CookiesEaten(10, 10));
	}
	
	@Test
	public void eatAllTheCookiesBatch() {
		scenario("Eat 20 of the 20 cookies of the batch")
		.Given(
			new BatchCreatedWithCookies(20), 
			new CookiesEaten(8, 12))
		.When(new EatCookies(12))
		.Then(new CookiesEaten(12, 0));
	}
```

When executed they check that the behavior is as expected, in typical jUnit style, but they also report the details of the scenarios:

```
Eat 10 of the 20 cookies of the batch
	Given Batch Created With 20 Cookies
	When EatCookies 10 cookies
	Then 10 Cookies Eaten and 10 remaining cookies
Eat 20 of the 20 cookies of the batch
	Given Batch Created With 20 Cookies
	Given 8 Cookies Eaten and 12 remaining cookies
	When EatCookies 12 cookies
	Then 12 Cookies Eaten and 0 remaining cookies
```

The above uses a default console reporting, and relies on the toString() method of each domain event. The scenario(...) method is provided by the test and does the wiring with the particular aggregate.

This is for illustration purpose, it is not meant to be a general purpose testing framework to use for production work.
