package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.Constants;
import ru.netology.data.DataHelper;
import ru.netology.pages.OrderCardDeliveryPage;

import java.text.ParseException;
import java.util.Random;


public class AppCardDeliveryTest {
    private static DataHelper.Exec.JarControl jarControl;
    private final String baseUrl = "http://localhost:9999";
    private OrderCardDeliveryPage page;

    @BeforeAll
    static void setUpAll() {
        if (Constants.PRE_TEST_PREPARATION) {
            jarControl = new DataHelper.Exec.JarControl();
            jarControl.start();
        }
        SelenideLogger.addListener(
                "AllureSelenide", new AllureSelenide()
                        .screenshots(true)  // делать скриншоты
                        .savePageSource(false)) ; // не сохранять копии html страниц
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("AllureSelenide");
        if (Constants.POST_TEST_PREPARATION) {
            jarControl.stop();
        }
    }

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        Configuration.baseUrl = baseUrl;
//        Configuration.holdBrowserOpen = true;  // false не оставляет браузер открытым по завершению теста
//        Configuration.reportsFolder = "build/reports/tests/test/screenshoots";
        Selenide.open("");
        page = new OrderCardDeliveryPage();
    }

    @DisplayName("Позитивный тест, города подставляются случайно")
    @Test
    void mainPositiveTest() {
        DataHelper.CardOrderInputInfo info = DataHelper.getValidCardOrderInputInfo();
        page
                .fillForm(info)
                .clickSubmit()
                .checkNotificationMessage(info.getDate());
    }

    @DisplayName("Не заполнено поле город.")
    @Test
    void negativeEmptyCityTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutCity())
                .clickSubmit()
                .checkCitySubText("Поле обязательно для заполнения");
    }

    @DisplayName("Город РФ не админ центр.")
    @Test
    void negativeNotAdministrativeCenterCityTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutCity())
                .fillCity(DataHelper.getInvalidCity())
                .clickSubmit()
                .checkCitySubText("Доставка в выбранный город недоступна");
    }

    @DisplayName("На день раньше допустимой даты.")
    @Test
    void negativeEarlierByADayThanTheMinDateTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutDate())
                .fillDate(DataHelper.nowWithDaysShift(2))
                .clickSubmit()
                .checkDateSubText("Заказ на выбранную дату невозможен");
    }

    @DisplayName("Минимально допустимая дата.")
    @Test
    void minimumAllowedDayTest() {
        String dateStr = DataHelper.nowWithDaysShift(3);
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutDate())
                .fillDate(dateStr)
                .clickSubmit()
                .checkNotificationMessage(dateStr);
    }

    @DisplayName("На день позже допустимой даты.")
    @Test
    void dayLaterThanTheMinimumAllowedDayTest() {
        String dateStr = DataHelper.nowWithDaysShift(4);
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutDate())
                .fillDate(dateStr)
                .clickSubmit()
                .checkNotificationMessage(dateStr);
    }

    @DisplayName("Не заполненное поле даты.")
    @Test
    void negativeEmptyDateTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutDate())
                .clearDate()
                .clickSubmit()
                .checkDateSubText("Неверно введена дата");
    }

    @DisplayName("На год позже допустимой даты.")
    @Test
    void earLaterThanTheMinimumAllowedDayTest() {
        String dateStr = DataHelper.nowWithYearsShift(1);
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutDate())
                .fillDate(dateStr)
                .clickSubmit()
                .checkNotificationMessage(dateStr);
    }

    @DisplayName("Имя с ё.")
    @Test
    void nameWithSmallYoTest() {
        DataHelper.CardOrderInputInfo info = DataHelper.getValidCardOrderInputInfoWithoutName();
        page
                .fillForm(info)
                .fillName("Неумёха")
                .clickSubmit()
                .checkNotificationMessage(info.getDate());
    }

    @DisplayName("Имя с Ё.")
    @Test
    void nameWithLageYoTest() {
        DataHelper.CardOrderInputInfo info = DataHelper.getValidCardOrderInputInfoWithoutName();
        page
                .fillForm(info)
                .fillName("Ёжик")
                .clickSubmit()
                .checkNotificationMessage(info.getDate());
    }

    @DisplayName("Имя с пробелами и тире.")
    @Test
    void nameWithDashesAndSpacesTest() {
        DataHelper.CardOrderInputInfo info = DataHelper.getValidCardOrderInputInfoWithoutName();
        page
                .fillForm(info)
                .fillName("По утру зубодробительно-скучающий")
                .clickSubmit()
                .checkNotificationMessage(info.getDate());
    }

    @DisplayName("Имя \"-\"")
    @Test
    void negativeNameDashTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutName())
                .fillName("-")
                .clickSubmit()
                .checkNameSubText("В имени кроме тире должны быть буквы.");
    }

    @DisplayName("Имя начинающееся на \"-\"")
    @Test
    void negativeNameWithFirstDashTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutName())
                .fillName("-Мандрагора")
                .clickSubmit()
                .checkNameSubText("Имя не может начинаться на тире.");
    }

    @DisplayName("Имя заканчивающееся на \"-\"")
    @Test
    void negativeNameWithLastDashTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutName())
                .fillName("Пенелопа Армани-")
                .clickSubmit()
                .checkNameSubText("Имя не может заканчиваться на тире.");
    }

    @DisplayName("Не заполненное имя")
    @Test
    void negativeEmptyNameTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutName())
                .clickSubmit()
                .checkNameSubText("Поле обязательно для заполнения");
    }

    @DisplayName("Телефон не заполнен.")
    @Test
    void negativeEmptyPhoneTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutPhone())
                .clickSubmit()
                .checkPhoneSubText("Поле обязательно для заполнения");
    }

    @DisplayName("Телефон короче.")
    @Test
    void negativePhoneShortTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutPhone())
                .fillPhone("+7978111111")
                .clickSubmit()
                .checkPhoneSubText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.");
    }

    @DisplayName("Без согласия на обработку персональных данных.")
    @Test
    void negativeCheckboxTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutAgreement())
                .clickSubmit()
                .checkAgreementInvalidIndication();
    }

    @DisplayName("Выбор даты на 7 дней позже текущей через виджет календаря.")
    @Test
    void calendarWidgetTest() throws ParseException {
        String dateStr = DataHelper.nowWithDaysShift(7);
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutDate())
                .fillDateByWidget(dateStr)
                .clickSubmit()
                .checkNotificationMessage(dateStr);
    }

    @DisplayName("Ввод города с помощью выпадающего списка.")
    @Test
    void popupListTest() {
        String[] cities = DataHelper.getValidCities();
        DataHelper.CardOrderInputInfo info = DataHelper.getValidCardOrderInputInfoWithoutCity();
        page
                .fillForm(info)
                .fillCityByPopupList(cities[new Random().nextInt(cities.length)])
                .clickSubmit()
                .checkNotificationMessage(info.getDate());
    }

    @DisplayName("Перепланировка на ту же дату")
    @Test
    void replanningSameDateTest() {
        DataHelper.CardOrderInputInfo info = DataHelper.getValidCardOrderInputInfo();

        page
                .fillForm(info)
                .clickSubmit()
                .checkNotificationMessage(info.getDate());

        Selenide.closeWindow();
        Selenide.closeWebDriver();
        setUp();

        page
                .fillForm(info)
                .clickSubmit()
                .checkDateSubText("Заказ на выбранную дату уже сделан");
        ;
    }

    @DisplayName("Перепланировка на минимальную дату")
    @Test
    void replanningMinDateTest() {
        DataHelper.CardOrderInputInfo info = DataHelper.getValidCardOrderInputInfoWithoutDate();
        String dateStr = DataHelper.getValidDate();
        page
                .fillForm(info)
                .fillDate(dateStr)
                .clickSubmit()
                .checkNotificationMessage(dateStr);

        Selenide.closeWindow();
        Selenide.closeWebDriver();
        setUp();

        String otherDateStr = DataHelper.nowWithDaysShift(3);
        page
                .fillForm(info)
                .fillDate(otherDateStr)
                .clickSubmit()
                .checkReplanMessage()
                .replanButtonClick()
                .checkNotificationMessage(otherDateStr);
    }

    @DisplayName("Перепланировка на день позднее")
    @Test
    void replanningOneDayLaterTest() {
        DataHelper.CardOrderInputInfo info = DataHelper.getValidCardOrderInputInfoWithoutDate();
        String dateStr = DataHelper.getValidDate();
        page
                .fillForm(info)
                .fillDate(dateStr)
                .clickSubmit()
                .checkNotificationMessage(dateStr);

        Selenide.closeWindow();
        Selenide.closeWebDriver();
        setUp();

        String otherDateStr = DataHelper.dateWithDaysShift(dateStr, 1);
        page
                .fillForm(info)
                .fillDate(otherDateStr)
                .clickSubmit()
                .checkReplanMessage()
                .replanButtonClick()
                .checkNotificationMessage(otherDateStr);
    }

    @DisplayName("Перепланировка на дату позднее")
    @Test
    void replanningLaterDateTest() {
        DataHelper.CardOrderInputInfo info = DataHelper.getValidCardOrderInputInfoWithoutDate();
        String dateStr = DataHelper.getValidDate();
        page
                .fillForm(info)
                .fillDate(dateStr)
                .clickSubmit()
                .checkNotificationMessage(dateStr);

        Selenide.closeWindow();
        Selenide.closeWebDriver();
        setUp();

        String otherDateStr = DataHelper.dateWithDaysShift(dateStr, 1 + new Random().nextInt(30));
        page
                .fillForm(info)
                .fillDate(otherDateStr)
                .clickSubmit()
                .checkReplanMessage()
                .replanButtonClick()
                .checkNotificationMessage(otherDateStr);
    }

    @DisplayName("Перепланировка на недоступную дату")
    @Test
    void replanningNotAvailableDateTest() {
        DataHelper.CardOrderInputInfo info = DataHelper.getValidCardOrderInputInfoWithoutDate();
        String dateStr = DataHelper.getValidDate();
        page
                .fillForm(info)
                .fillDate(dateStr)
                .clickSubmit()
                .checkNotificationMessage(dateStr);

        String otherDateStr = DataHelper.nowWithDaysShift(3 - new Random().nextInt(30));

        Selenide.closeWindow();
        Selenide.closeWebDriver();
        setUp();

        page
                .fillForm(info)
                .fillDate(otherDateStr)
                .clickSubmit()
                .checkDateSubText("Заказ на выбранную дату невозможен");
    }
}