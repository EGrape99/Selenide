import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;


public class DeliveryTest {
    public static String setLocalDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldRegister() {
        String date = setLocalDate(3);
        $("[data-test-id=city] input").setValue("Калининград");
        $("[data-test-id=date] input").doubleClick().sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79052228899");
        $("[data-test-id=agreement]").click();
        $$("[type='button']").find(Condition.exactText("Запланировать")).click();
        $("[data-test-id=success-notification]").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Успешно! Встреча успешно запланирована на " + date),
                        Duration.ofSeconds(15));

    }

    @Test
    void shouldNotRegisterIncorrectCity() {
        String date = setLocalDate(3);
        $("[data-test-id=city] input").setValue("Сеул");
        $("[data-test-id=date] input").doubleClick().sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79052228899");
        $("[data-test-id=agreement]").click();
        $$("[type='button']").find(Condition.exactText("Запланировать")).click();
        $(byText("Доставка в выбранный город недоступна")).shouldBe(visible);
    }

    @Test
    void shouldNotRegisterIncorrectName() {
        String date = setLocalDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(date);
        $("[data-test-id=name] input").setValue("Ivanov Ivan");
        $("[data-test-id=phone] input").setValue("+79052228899");
        $("[data-test-id=agreement]").click();
        $$("[type='button']").find(Condition.exactText("Запланировать")).click();
        $(byText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")).shouldBe(visible);
    }

    @Test
    void shouldNotRegisterIncorrectData() {
        String date = setLocalDate(1);
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79052228899");
        $("[data-test-id=agreement]").click();
        $$("[type='button']").find(Condition.exactText("Запланировать")).click();
        $(byText("Заказ на выбранную дату невозможен")).shouldBe(visible);

    }
}

