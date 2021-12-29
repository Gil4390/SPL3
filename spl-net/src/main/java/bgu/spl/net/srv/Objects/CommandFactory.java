package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;

public class CommandFactory {
    public static Command makeCommand(int OpCode){
        switch (OpCode){
            case 1:
                return new RegisterCommand(1);
                break;
            case 2:
                return new LoginCommand(2);
                break;
            case 3:
                return new LogoutCommand(3);
                break;
            case 4:
                return new FollowCommand(4);
                break;
            case 5:
                return new PostCommand(5);
                break;
            case 6:
                return new PrivateMessageCommand(6);
                break;
            case 7:
                return new LogStatCommand(7);
                break;
            case 8:
                return new StatsCommand(8);
                break;
            case 9:
                return new NotificationCommand(9);
                break;
            case 10:
                return new AckCommand(10);
                break;
            case 11:
                return new ErrorCommand (11);
                break;
            case 12:
                return new BlockCommand(12);
                break;
            default:
                return null;
        }
    }
}
