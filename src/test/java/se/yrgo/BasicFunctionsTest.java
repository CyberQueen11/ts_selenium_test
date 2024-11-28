package se.yrgo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BasicFunctionsTest {

    WebDriver driver;

    @BeforeEach
    void setupTest() {
        driver = new FirefoxDriver();
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    void checkTitle() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://yrgo-amazing-todo-app.netlify.app/");

        assertEquals("Todo App", driver.getTitle());
    }
}

/**
 * Only todos marked as "not done" should be displayed on the homepage.
 * Only todos marked as "done" should be displayed on the "done" page.
 * You can add a new todo, which will initially be marked as "not done."
 * A todo can be marked as "done" using a checkbox, and it will then move to the
 * "done" page.
 * A todo marked as "done" can be changed back to "not done" using a checkbox.
 * A todo marked as "done" can be permanently deleted using a button. Deletion
 * must be confirmed.
 * When the page is reloaded, the state of the website should be preserved.
 */