package sg.asmallmuseum.presentation.CustomListenerInterfaces;

import java.util.List;
import java.util.Map;

import sg.asmallmuseum.Domain.User;

public interface UserLoadListener {
    void userInfo(User user);
    void getAllUser(List<String> list);

}
