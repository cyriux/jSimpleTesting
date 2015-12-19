package simpletesting;

import org.junit.Assert;

public abstract class SimpleTesting {

	public SimpleTesting(String title) {
		this.title = title;
		printTitle(title);
	}

	private final String title;
	private DomainEvent[] pastEvents;
	private Command command;

	protected abstract void printTitle(String title);
	protected abstract void print(String step, String details);
	protected abstract DomainEvent[] executeCommand(Command command, DomainEvent[] pastEvents);

	
	public SimpleTesting Given(DomainEvent... pastEvents) {
		print("Given", pastEvents);
		this.pastEvents = pastEvents;
		return this;
	}

	private void print(String keyword, DomainEvent[] events) {
		for (DomainEvent e : events) {
			print(keyword, e.toString());
		}
	}

	public SimpleTesting When(Command command) {
		print("When", command.toString());
		this.command = command;
		return this;
	}

	
	public SimpleTesting Then(DomainEvent... expectedEvents) {
		print("Then", expectedEvents);
		final DomainEvent[] events = executeCommand(command, pastEvents);
		Assert.assertArrayEquals(expectedEvents, events);
		return this;
	}

	@Override
	public String toString() {
		return "SimpleTesting Scenario: " + title;
	}

}
