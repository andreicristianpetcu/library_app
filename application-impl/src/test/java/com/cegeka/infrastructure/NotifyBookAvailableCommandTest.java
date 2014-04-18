package com.cegeka.infrastructure;

import com.cegeka.domain.books.BookEntity;
import com.cegeka.domain.users.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Locale;
import java.util.Map;

import static com.cegeka.domain.books.BookEntityTestFixture.aBookWithOneCopy;
import static com.cegeka.domain.user.UserEntityTestFixture.aUserEntity;
import static com.cegeka.infrastructure.NotifyBookAvailableCommand.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NotifyBookAvailableCommandTest {

    @Mock
    protected EmailComposer emailComposerMock;

    @Captor
    protected ArgumentCaptor<Map<String, Object>> valuesForTemplateFillerCaptor;

    @InjectMocks
    private NotifyBookAvailableCommand notifyBookAvailableCommand = new NotifyBookAvailableCommand();

    @Test
    public void whenReturnBook_shouldSendEmailToWatchersAndClearWatchersList() {
        BookEntity book = aBookWithOneCopy();

        UserEntity juliet = aUserEntity("juliet@mailinator.com");
        UserEntity watcher = aUserEntity("secondWatcher@mailinator.com");
        watcher.setLocale(Locale.JAPAN);

        book.lendTo(juliet);
        book.addWatcher(watcher);

        notifyBookAvailableCommand.alertWatchersBookIsAvailable(book);

        verify(emailComposerMock).sendEmail(
                eq(watcher.getEmail()),
                eq(NOTIFY_BOOK_AVAILABLE_SUBJECT),
                eq(NOTIFY_BOOK_AVAILABLE_CONTENT),
                eq(watcher.getLocale()),
                valuesForTemplateFillerCaptor.capture());

        assertThat(capturedActuals().get("addressee")).isEqualTo(watcher.getProfile().getFirstName());
        assertThat(capturedActuals().get("title")).isEqualTo(book.getTitle());
        assertThat(capturedActuals().get("author")).isEqualTo(book.getAuthor());
        assertThat(capturedActuals().get("author")).isEqualTo(book.getAuthor());
        assertThat((String)capturedActuals().get("link")).endsWith(BOOK_RESOURCE_PATH + book.getId());

    }

    private Map<String, Object> capturedActuals() {
        return valuesForTemplateFillerCaptor.getValue();
    }

}
