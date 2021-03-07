package sg.asmallmuseum.logic;

import java.util.List;

import sg.asmallmuseum.Domain.User;

public interface UserDBListener {
    void setUserListener(List<String> list);
    void setAllUserListener(List<String> list);
}
