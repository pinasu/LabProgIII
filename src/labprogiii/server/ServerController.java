package labprogiii.server;

public class ServerController {
    private ServerView view;
    private Server server;

    public ServerController(Server server) {
        this.view = new ServerView(this);
        this.server = server;
    }

    public void printLog(String log){
        this.view.printLog(log);
    }

}
