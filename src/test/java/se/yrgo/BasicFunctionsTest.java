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

/*
 * Enbart todos som är "not done" skall visas på första sidan.
 * Enbart todos som är "done" skall visas på sidan "done".
 * Man kan lägga till en ny todo. Den är då inte markerad som "done".
 * Man kan markera en todo som "done" genom checkbox. Hamnar då på sidan "done".
 * En todo som är "done" kan göras till "not done" genom checkbox.
 * En todo som är "done" kan raderas helt genom en knapp. Man måste bekräfta
 * borttagning.
 * Laddar man om sidan skall webbsidans tillstånd bibehållas.
 */