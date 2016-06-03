package Services;

import models.User;
import repositories.UsersRepository;

public class AuthenticationService {
    private static User LoggedUser;

    public static User getLoggedUser() {
        return LoggedUser;
    }

    public static void setLoggedUser(User loggedUser) {
        LoggedUser = loggedUser;
    }

    public static void Authenticate(String username, String password) {
        UsersRepository userRep = new UsersRepository("Users.txt", User.class);
        setLoggedUser(userRep.getByUsernameAndPassword(username, password));
    }
}		

