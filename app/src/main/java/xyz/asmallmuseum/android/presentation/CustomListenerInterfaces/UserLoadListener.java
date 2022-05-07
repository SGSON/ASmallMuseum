package xyz.asmallmuseum.android.presentation.CustomListenerInterfaces;

import java.util.List;

import xyz.asmallmuseum.android.Domain.User;

public interface UserLoadListener {
    void userInfo(User user);
    void getAllUser(List<String> list);

}
