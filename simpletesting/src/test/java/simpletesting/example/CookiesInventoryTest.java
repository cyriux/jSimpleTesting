package simpletesting.example;

import org.junit.Test;

import simpletesting.Command;
import simpletesting.DomainEvent;
import simpletesting.SimpleTesting;

public class CookiesInventoryTest {

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

	public class BatchCreatedWithCookies implements DomainEvent {
		private final int cookies;

		public BatchCreatedWithCookies(int cookies) {
			this.cookies = cookies;
		}

		@Override
		public int hashCode() {
			return cookies;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof BatchCreatedWithCookies)) {
				return false;
			}
			final BatchCreatedWithCookies other = (BatchCreatedWithCookies) obj;
			return cookies == other.cookies;
		}

		@Override
		public String toString() {
			return "Batch Created With " + cookies + " Cookies";
		}

	}

	public class CookiesEaten implements DomainEvent {
		private final int eaten;
		private final int remaining;

		public CookiesEaten(int eaten, int remaining) {
			this.eaten = eaten;
			this.remaining = remaining;
		}

		@Override
		public int hashCode() {
			return eaten ^ remaining;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof CookiesEaten)) {
				return false;
			}
			CookiesEaten other = (CookiesEaten) obj;
			return eaten == other.eaten && remaining == other.remaining;
		}

		@Override
		public String toString() {
			return eaten + " Cookies Eaten and " + remaining + " remaining cookies";
		}

	}

	public class EatCookies implements Command {
		private final int cookies;

		public EatCookies(int cookies) {
			this.cookies = cookies;
		}

		@Override
		public String toString() {
			return "EatCookies " + cookies + " cookies";
		}

	}

	
	// the aggregate under test
	public class CookiesInventory {
		private int count;

		public void load(DomainEvent[] history) {
			for (DomainEvent e : history) {
				if (e instanceof BatchCreatedWithCookies) {
					BatchCreatedWithCookies bc = (BatchCreatedWithCookies) e;
					count += bc.cookies;
				}
				if (e instanceof CookiesEaten) {
					CookiesEaten ec = (CookiesEaten) e;
					count -= ec.eaten;
				}
			}
		}

		public DomainEvent[] apply(Command command) {
			int eaten = 0;
			if (command instanceof EatCookies) {
				EatCookies ec = (EatCookies) command;
				eaten = ec.cookies;
				count -= eaten;
			}
			return new DomainEvent[] { new CookiesEaten(eaten, count) };
		}
	}

	// Wiring the simple testing framework to the aggregate under test
	private final SimpleTesting scenario(final String title) {
		return new SimpleTesting(title) {

			@Override
			protected DomainEvent[] executeCommand(Command command, DomainEvent[] pastEvents) {
				final CookiesInventory aggregate = new CookiesInventory();
				aggregate.load(pastEvents);
				return aggregate.apply(command);
			}
			
			protected void print(String step, String details){
				System.out.println("\t" + step + " " + details);
			}

			@Override
			protected void printTitle(String title) {
				System.out.println(title);
			}

		};
	}

}
