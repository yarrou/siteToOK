package org.alex.konon.sol.siteToOK.repositories;

import org.alex.konon.sol.siteToOK.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "select recipient from messages where sender=:userName group by recipient UNION select sender from messages where recipient=:userName group by sender",nativeQuery = true)
    public LinkedHashSet<String> dialogues(@Param("userName") String userName);

    @Query(value =
            "select * from messages where (sender=:firstUserName and recipient=:secondUserName) or (sender=:secondUserName and recipient=:firstUserName) order by date desc",
            nativeQuery = true)
    public ArrayList<Message> corespondence(@Param("firstUserName") String firstUserName ,@Param("secondUserName") String secondUser);

    @Query(value = "select * from messages where recipient is null order by date desc",
            nativeQuery = true)
    public ArrayList<Message> appeals();

    @Query(value = "select * from messages   where sender=:firstUserName and recipient is null order by date desc",
            nativeQuery = true)
    public ArrayList<Message> requestsToAdministration(@Param("firstUserName") String firstUserName );

}
