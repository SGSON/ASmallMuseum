package sg.asmallmuseum.persistence;

import sg.asmallmuseum.Domain.User;

public interface UserDBInterface {
    public void addUser();
    public User getUser();
    public void updateUser();
    public void deleteUser();
}
