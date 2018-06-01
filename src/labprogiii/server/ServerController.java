package labprogiii.server;

public class ServerController {
    private ServerView view;

    public ServerController(Server server) {
        this.view = new ServerView(this);
    }

    public void printLog(String log){
        this.view.printLog(log);
    }

}
