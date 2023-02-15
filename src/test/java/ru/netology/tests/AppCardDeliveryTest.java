package ru.netology.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.netology.data.Constants;
import ru.netology.data.DataHelper;
import ru.netology.pages.OrderCardDeliveryPage;
import ru.netology.util.ScreenShooterReportPortalExtension;

import java.text.ParseException;
import java.util.Random;

import static ru.netology.data.DataHelper.Exec.AllureControl.allureReportCreate;

@ExtendWith({ScreenShooterReportPortalExtension.class})
public class AppCardDeliveryTest {
    private static DataHelper.Exec.JarControl jarControl;
    private final String baseUrl = "http://localhost:9999";
    private final String epicName = "Заказ банковских карт";
    private final String featureName = "Формирование заказа банковской карты";
    private final String replanningStoryName = "Перепланировка даты доставки банковской карты";
    private final String orderFormStoryName = "Проверка формы заказа банковской карты";
    private final String ownerName = "Павлюков Владимир";
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
            allureReportCreate();
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

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Позитивный тест, города подставляются случайно")
    void mainPositiveTest() {
        DataHelper.CardOrderInputInfo info = DataHelper.getValidCardOrderInputInfo();
        page
                .fillForm(info)
                .clickSubmit()
                .checkNotificationMessage(info.getDate());
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Не заполнено поле город.")
    void negativeEmptyCityTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutCity())
                .clickSubmit()
                .checkCitySubText("Поле обязательно для заполнения");
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Город РФ не админ центр.")
    void negativeNotAdministrativeCenterCityTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutCity())
                .fillCity(DataHelper.getInvalidCity())
                .clickSubmit()
                .checkCitySubText("Доставка в выбранный город недоступна");
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("На день раньше допустимой даты.")
    void negativeEarlierByADayThanTheMinDateTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutDate())
                .fillDate(DataHelper.nowWithDaysShift(2))
                .clickSubmit()
                .checkDateSubText("Заказ на выбранную дату невозможен");
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Минимально допустимая дата.")
    void minimumAllowedDayTest() {
        String dateStr = DataHelper.nowWithDaysShift(3);
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutDate())
                .fillDate(dateStr)
                .clickSubmit()
                .checkNotificationMessage(dateStr);
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("На день позже допустимой даты.")
    void dayLaterThanTheMinimumAllowedDayTest() {
        String dateStr = DataHelper.nowWithDaysShift(4);
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutDate())
                .fillDate(dateStr)
                .clickSubmit()
                .checkNotificationMessage(dateStr);
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Не заполненное поле даты.")
    void negativeEmptyDateTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutDate())
                .clearDate()
                .clickSubmit()
                .checkDateSubText("Неверно введена дата");
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("На год позже допустимой даты.")
    void earLaterThanTheMinimumAllowedDayTest() {
        String dateStr = DataHelper.nowWithYearsShift(1);
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutDate())
                .fillDate(dateStr)
                .clickSubmit()
                .checkNotificationMessage(dateStr);
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Имя с ё.")
    void nameWithSmallYoTest() {
        DataHelper.CardOrderInputInfo info = DataHelper.getValidCardOrderInputInfoWithoutName();
        page
                .fillForm(info)
                .fillName("Неумёха")
                .clickSubmit()
                .checkNotificationMessage(info.getDate());
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Имя с Ё.")
    void nameWithLageYoTest() {
        DataHelper.CardOrderInputInfo info = DataHelper.getValidCardOrderInputInfoWithoutName();
        page
                .fillForm(info)
                .fillName("Ёжик")
                .clickSubmit()
                .checkNotificationMessage(info.getDate());
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Имя с пробелами и тире.")
    void nameWithDashesAndSpacesTest() {
        DataHelper.CardOrderInputInfo info = DataHelper.getValidCardOrderInputInfoWithoutName();
        page
                .fillForm(info)
                .fillName("По утру зубодробительно-скучающий")
                .clickSubmit()
                .checkNotificationMessage(info.getDate());
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.TRIVIAL)
    @DisplayName("Имя \"-\"")
    void negativeNameDashTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutName())
                .fillName("-")
                .clickSubmit()
                .checkNameSubText("В имени кроме тире должны быть буквы.");
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.TRIVIAL)
    @DisplayName("Имя начинающееся на \"-\"")
    void negativeNameWithFirstDashTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutName())
                .fillName("-Мандрагора")
                .clickSubmit()
                .checkNameSubText("Имя не может начинаться на тире.");
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.TRIVIAL)
    @DisplayName("Имя заканчивающееся на \"-\"")
    void negativeNameWithLastDashTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutName())
                .fillName("Пенелопа Армани-")
                .clickSubmit()
                .checkNameSubText("Имя не может заканчиваться на тире.");
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Не заполненное имя")
    void negativeEmptyNameTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutName())
                .clickSubmit()
                .checkNameSubText("Поле обязательно для заполнения");
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Телефон не заполнен.")
    void negativeEmptyPhoneTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutPhone())
                .clickSubmit()
                .checkPhoneSubText("Поле обязательно для заполнения");
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Телефон короче.")
    void negativePhoneShortTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutPhone())
                .fillPhone("+7978111111")
                .clickSubmit()
                .checkPhoneSubText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.");
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Без согласия на обработку персональных данных.")
    void negativeCheckboxTest() {
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutAgreement())
                .clickSubmit()
                .checkAgreementInvalidIndication();
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.NORMAL)
    @DisplayName("Выбор даты на 7 дней позже текущей через виджет календаря.")
    void calendarWidgetTest() throws ParseException {
        String dateStr = DataHelper.nowWithDaysShift(7);
        page
                .fillForm(DataHelper.getValidCardOrderInputInfoWithoutDate())
                .fillDateByWidget(dateStr)
                .clickSubmit()
                .checkNotificationMessage(dateStr);
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = orderFormStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.MINOR)
    @DisplayName("Ввод города с помощью выпадающего списка.")
    void popupListTest() {
        String[] cities = DataHelper.getValidCities();
        DataHelper.CardOrderInputInfo info = DataHelper.getValidCardOrderInputInfoWithoutCity();
        page
                .fillForm(info)
                .fillCityByPopupList(cities[new Random().nextInt(cities.length)])
                .clickSubmit()
                .checkNotificationMessage(info.getDate());
    }

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = replanningStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.TRIVIAL)
    @DisplayName("Перепланировка на ту же дату")
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

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = replanningStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Перепланировка на минимальную дату")
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

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = replanningStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Перепланировка на день позднее")
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

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = replanningStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Перепланировка на дату позднее")
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

    @Test
    @Epic(value = epicName)
    @Feature(value = featureName)
    @Story(value = replanningStoryName)
    @Owner(value = ownerName)
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Перепланировка на недоступную дату")
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