package ui;

import facade.*;

public abstract class UI {
    public static ServerFacade facade;

    public UI() {
        facade = new ServerFacade(8080);
    }

    public abstract String eval(String line);
}
