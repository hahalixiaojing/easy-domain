package cn.easylib.domain.visual.application;

public class ApplicationDescriptor {
    private final String name;
    private final String applicationServiceDescription;

    private final String clsName;
    private final String methodName;

    private final boolean isCommand;

    public ApplicationDescriptor(String name,
                                 String applicationServiceDescription,
                                 String clsName,
                                 String methodName,
                                 boolean isCommand
    ) {
        this.name = name;
        this.applicationServiceDescription = applicationServiceDescription;
        this.clsName = clsName;
        this.methodName = methodName;
        this.isCommand = isCommand;
    }

    public String getName() {
        return name;
    }

    public String getApplicationServiceDescription() {
        return applicationServiceDescription;
    }

    public String getClsName() {
        return clsName;
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean isCommand() {
        return isCommand;
    }
}
