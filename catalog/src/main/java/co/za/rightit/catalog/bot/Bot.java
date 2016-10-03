package co.za.rightit.catalog.bot;

import java.util.Optional;

import org.apache.camel.component.telegram.model.IncomingMessage;
import org.apache.camel.component.telegram.model.User;

public class Bot {
	
	public String process(IncomingMessage message) {
		String text = message.getText();
		if(text == null) return null;
		if(text.contains("/subscribe")) {
			return getSubscribeReply(message.getFrom());
		} else if(text.contains("/trends")) {
			return getTrendsReply(message.getFrom());
		} else if(text.contains("/stop")) {
			return getStopReply(message.getFrom());
		} else if(text.contains("/start")) {
			return getStartReply(message.getFrom());
		}
		return null;
	}

	private String getStartReply(User from) {
		Optional<String> name = getDisplayName(from);
		String reply = "Thanks for popping by. Please use a slash(/) for a list of supported commands. Hope you enjoy your stay";
		if(name.isPresent()) {
			return reply + " " + name.get() + "!";
		} else {
			return reply + "!";
		}
	}

	private String getTrendsReply(User from) {
		Optional<String> name = getDisplayName(from);
		String reply = "No trend updates available. Please try again later";
		if(name.isPresent()) {
			return reply + " " + name.get() + ".";
		} else {
			return reply + ".";
		}
	}

	private String getSubscribeReply(User from) {
		Optional<String> name = getDisplayName(from);
		String reply = "Great. We'll inform you on new trends and deals";
		if(name.isPresent()) {
			return reply + " " + name.get() + ".";
		} else {
			return reply + ".";
		}
	}

	private String getStopReply(User from) {
		Optional<String> name = getDisplayName(from);
		String reply = "Sorry that you are leaving. Hope to see you soon";
		if(name.isPresent()) {
			return reply + " " + name.get() + ".";
		} else {
			return reply + ".";
		}
	}
	
	private Optional<String> getDisplayName(User user) {
		Optional<String> name = Optional.empty();
		if(user.getUsername() != null) {
			name = Optional.of(user.getUsername());
		} else if(user.getFirstName() != null) {
			name = Optional.of(user.getFirstName());
		}
		return name;
	} 

}
