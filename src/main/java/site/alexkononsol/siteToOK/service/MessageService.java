package site.alexkononsol.siteToOK.service;

import site.alexkononsol.siteToOK.entity.Message;

import java.util.List;
import java.util.Set;

public interface MessageService {
    Set<String> getDialogues(String userName);
    List<Message> getMessagesList(String user1, String user2);
    List<Message> getLast5Messages(String userName, String nameSender);
    void save(Message message);
}
