package se.yrgo.pageobjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage {
    private WebDriver driver;

    private static final Duration TIMEOUT = Duration.ofSeconds(5);
    private By inputField = By.cssSelector("input[type='text']");
    private By submitBtn = By.cssSelector("input[type='submit']");
    private By listSelector = By.cssSelector(".todolist li");
    private By checkboxSelector = By.cssSelector("input[type='checkbox']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.manage().timeouts().implicitlyWait(TIMEOUT);
        driver.get("https://yrgo-amazing-todo-app.netlify.app");
    }

    public WebElement getInputField() {
        return driver.findElement(inputField);
    }

    public WebElement getSubmitButton() {
        return driver.findElement(submitBtn);
    }

    public List<WebElement> getTodoList() {
        return driver.findElements(listSelector);
    }

    public WebElement getCheckboxForTodo(WebElement todo) {
        return todo.findElement(checkboxSelector);
    }

    public WebElement getTodoElement(String todoText) {
        return getTodoList().stream()
                .filter(todo -> todo.getText().equals(todoText))
                .findFirst()
                .orElse(null);
    }

    public WebElement getTodoByIndex(int index) {
        return getTodoList().get(index);
    }
}
