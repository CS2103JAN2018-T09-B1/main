package seedu.address.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.Person;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String SEARCH_PAGE_URL = "https"
           + "://se-edu.github.io/addressbook-level4/DummySearchPage.html?name=";
    public static final String PERSON_PAGE = ".html";
    public static final String PROFILE_DIRECTORY = "/StudentPage/";
    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private WebView browser;

    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);
        loadDefaultPage();
        registerAsAnEventHandler(this);
    }
    /**
    * To load person page according to person name
    */
    public void loadPersonPage(Person person) throws IOException {
       // InputStream personPage = getClass().getResourceAsStream(PROFILE_DIRECTORY + person.getName().fullName + PERSON_PAGE);
        URL personPage = MainApp.class.getResource(PROFILE_DIRECTORY + person.getName().fullName + PERSON_PAGE);
        //URL personPage = MainApp.class.getResource(PROFILE_DIRECTORY + "Johnny Chan.html");
        loadPage(personPage.toExternalForm());
       // String stringPage = convertStreamToString(personPage);

        //loadPage(stringPage);
        //TimeUnit.MINUTES.sleep(1);
        //loadPersonPage(person);
    }

    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    public void loadDefaultPage() {
        URL defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        loadPage(defaultPage.toExternalForm());
    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) throws IOException {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection().person);
    }
}
