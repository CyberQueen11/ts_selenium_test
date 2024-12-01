package se.yrgo.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DonePage {
    private WebDriver driver;

    private By listSelector = By.cssSelector(".todolist li");
    private By checkboxSelector = By.cssSelector("input[type='checkbox']");

    public DonePage(WebDriver driver) {
        this.driver = driver;
    }

    public List<WebElement> getDoneTodos() {
        return driver.findElements(listSelector);
    }

    public WebElement getCheckboxForDoneTodo(WebElement todo) {
        return todo.findElement(checkboxSelector);
    }
}
