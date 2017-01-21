package co.za.rightit.healthchecks.api.slack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SlackMessage {
	
	private final String channel;
    
	private final String username;
    
	private final String text;
    
	private final String icon_url;
	
	private String icon_emoji;
    
	private final List<Map<String, Object>> attachments;
	
	public SlackMessage() {
		this(null, null, null, null);
	}

    public SlackMessage(String username, String iconUrl, String channel, String message) {
        this.channel = channel;
        this.text = message;
        this.username = username;
        this.icon_url = iconUrl;
        if(this.icon_url == null) {
        	this.icon_emoji = ":computer:";
        }
        this.attachments = new LinkedList<>();
    }

    public SlackMessage(String username, String iconUrl, String channel, String message, Color color) {
        this.channel = channel;
        this.text = "";
        this.username = username;
        this.icon_url = iconUrl;
        if(this.icon_url == null) {
        	this.icon_emoji = ":ghost:";
        }
        this.attachments = new LinkedList<>();
        final Map<String, Object> attachment = new HashMap<>();
        attachment.put("fallback", "Summary");
        attachment.put("color", color.value);
        attachment.put("text", message);
        attachments.add(attachment);
    }

    public SlackMessage withAttachment(String text, Color color) {
        Map<String, Object> attachment = new HashMap<>();
        attachment.put("color", color.value);
        attachment.put("text", text);
        attachments.add(attachment);
        return this;
    }
    

    public String getIcon_emoji() {
		return icon_emoji;
	}

	public String getChannel() {
		return channel;
	}

	public String getUsername() {
		return username;
	}

	public String getText() {
		return text;
	}

	public String getIcon_url() {
		return icon_url;
	}

	public List<Map<String, Object>> getAttachments() {
		return attachments;
	}


	public static class Color {
        public static final Color WARNING = of("warning");
        public static final Color GOOD = of("good");
        public static final Color DANGER = of("danger");
        public static final Color TEAL = of("#35B3AB");

        private final String value;

        public Color(final String value) {
            this.value = value;
        }

        static Color of(String value) {
            return new Color(value);
        }

    }

}
