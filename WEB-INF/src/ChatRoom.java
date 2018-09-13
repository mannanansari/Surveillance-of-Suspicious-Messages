import java.util.Stack;

public class ChatRoom extends Stack{
	private String name;
	private String description;
	public ChatRoom(String name,String description){
		this.name=name;
		this.description=description;
	}

	public void addChatEntry(ChatEntry entry) {
	push(entry);
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}
}
