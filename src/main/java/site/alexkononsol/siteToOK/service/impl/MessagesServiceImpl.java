package site.alexkononsol.siteToOK.service.impl;

import org.springframework.stereotype.Service;
import site.alexkononsol.siteToOK.entity.Message;
import site.alexkononsol.siteToOK.repositories.MessageRepository;
import site.alexkononsol.siteToOK.service.MessageService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class MessagesServiceImpl implements MessageService {
    private final MessageRepository repository;

    public MessagesServiceImpl(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Set<String> getDialogues(String userName) {
        Set<String> whoWroteNames = repository.dialogues(userName);
        return whoWroteNames;
    }

    @Override
    public List<Message> getMessagesList(String user1, String user2) {
        ArrayList<Message> messages = user2.equals("administration")?repository.requestsToAdministration(user1):repository.corespondence(user1,user2);
        return messages;
    }

    @Override
    public List<Message> getLast5Messages(String userName, String nameSender) {
        ArrayList<Message> lastFiveMessages = new ArrayList<>();
        Iterator<Message> iterator = getMessagesList(userName,nameSender).iterator();
        for(int i = 0;( i < 5 )&& iterator.hasNext();i++){
            lastFiveMessages.add( iterator.next());
        }
        return lastFiveMessages;
    }

    @Override
    public void save(Message message) {
        repository.save(message);
    }
}
