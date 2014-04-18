package com.cegeka.infrastructure;

import com.cegeka.domain.books.BookEntity;
import com.cegeka.domain.users.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotifyBookAvailableCommand {

    public static final String NOTIFY_BOOK_AVAILABLE_SUBJECT = "notify-book-available-subject";
    public static final String NOTIFY_BOOK_AVAILABLE_CONTENT = "notify-book-available-content";
    public static final String BOOK_RESOURCE_PATH = "#/book/";

    @Value("${application_url}")
    private String applicationUrl;

    @Resource
    private EmailComposer emailComposer;

    public void alertWatchersBookIsAvailable(BookEntity book) {
        for (UserEntity watcher : book.getWatchers()) {
            emailComposer.sendEmail(watcher.getEmail(),
                    NOTIFY_BOOK_AVAILABLE_SUBJECT,
                    NOTIFY_BOOK_AVAILABLE_CONTENT,
                    watcher.getLocale(),
                    notifyBookAvailableTemplateParameters(watcher.getProfile().getFirstName(), book));
        }
    }

    private Map<String, Object> notifyBookAvailableTemplateParameters(String addressee, BookEntity book) {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("addressee", addressee);
        values.put("title", book.getTitle());
        values.put("author", book.getAuthor());
        values.put("link", applicationUrl + BOOK_RESOURCE_PATH + book.getId());
        return values;
    }
}
