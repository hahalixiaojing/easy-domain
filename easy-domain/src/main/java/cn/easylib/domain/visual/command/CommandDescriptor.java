package cn.easylib.domain.visual.command;

public class CommandDescriptor {
    private String name;
    private String commandDescription;

    public CommandDescriptor(String name, String commandDescription) {
        this.name = name;
        this.commandDescription = commandDescription;
    }

    public String getName() {
        return name;
    }

    public String getCommandDescription() {
        return commandDescription;
    }
}
