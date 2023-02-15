package ru.netology.data;

import com.github.javafaker.Faker;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.SneakyThrows;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class DataHelper {
    private DataHelper() {
    }

    public static class Exec {
        private Exec() {
        }

        public static class JarControl {
            private final String pathToJarFile;
            private Process process = null;

            public JarControl() {
                this.pathToJarFile = "artifacts/app-card-delivery.jar";
            }

            public void start() {
                process = execJar(
                        pathToJarFile,
                        " & echo $! > ./testserver.pid"); // для ручной остановки
            }

            public void stop() {
                process.destroy();
            }
        }

        @SneakyThrows
        private static Process execJar(@NotNull String pathToJarFile, String... params) {
            List<String> commands = new ArrayList<>();
            commands.add("java");
            commands.add("-jar");
            commands.add(pathToJarFile);
            commands.addAll(Arrays.asList(params));
            return new ProcessBuilder()
                    .directory(new File("./"))
                    .command(commands)
                    .start();
        }

        @SneakyThrows
        private static Process execBashScriptFromFile(@NotNull String pathToScriptFile, String... params) {
            List<String> commands = new ArrayList<>();
            commands.add("sh");
            commands.add(pathToScriptFile);
            commands.addAll(Arrays.asList(params));
            return new ProcessBuilder()
                    .directory(new File("./"))
                    .command(commands)
                    .start();
        }
    }

    public static String nowWithDaysShift(int days_count) {
        return LocalDate.now().plusDays(days_count).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String dateWithDaysShift(@NotNull String dateStr, int days_count) {
        return LocalDate
                .parse(dateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                .plusDays(days_count)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String nowWithYearsShift(int years_count) {
        return LocalDate.now().plusYears(years_count).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String getInvalidCity() {
        return "Оха";
    }

    public static String getValidDate(){
        return nowWithDaysShift(3 + (new Random()).nextInt(29)); // [3, ..., 31]
    }

    @Value
    public static class CardOrderInputInfo {
        String city;
        String date;
        String name;
        String phone;
        Boolean isAgreement;
    }

    public static CardOrderInputInfo getValidCardOrderInputInfo() {
        Faker faker = new Faker(Locale.forLanguageTag("ru"));
        var cities = getValidCities();
        var city = cities[(new Random()).nextInt(cities.length)];
        var date = nowWithDaysShift(4 + (new Random()).nextInt(28)); // [4, ..., 31]
        var name = faker.name().fullName();
        // TODO убрать замену ё на е, когда починят баг
        name = name.replaceAll("Ё", "Е");
        name = name.replaceAll("ё", "е");
        var phone = faker.phoneNumber().phoneNumber();
        phone = phone.replaceAll("\\(", "");
        phone = phone.replaceAll("\\)", "");
        phone = phone.replaceAll("-", "");
        CardOrderInputInfo cardOrderInputInfo = new CardOrderInputInfo(city, date, name, phone, true);
        return cardOrderInputInfo;
    }

    public static String[] getValidCities() {
        return new String[]{
                "Абакан",
                "Анадырь",
                "Архангельск",
                "Астрахань",
                "Барнаул",
                "Белгород",
                "Биробиджан",
                "Благовещенск",
                "Брянск",
                "Великий Новгород",
                "Владивосток",
                "Владикавказ",
                "Владимир",
                "Волгоград",
                "Вологда",
                "Воронеж",
                "Гатчина",
                "Горно-Алтайск",
                "Грозный",
                "Екатеринбург",
                "Иваново",
                "Ижевск",
                "Иркутск",
                "Йошкар-Ола",
                "Казань",
                "Калининград",
                "Калуга",
                "Кемерово",
                "Киров",
                "Кострома",
                "Красногорск",
                "Краснодар",
                "Красноярск",
                "Курган",
                "Курск",
                "Кызыл",
                "Липецк",
                "Магадан",
                "Магас",
                "Майкоп",
                "Махачкала",
                "Москва",
                "Мурманск",
                "Нальчик",
                "Нарьян-Мар",
                "Нижний Новгород",
                "Новосибирск",
                "Омск",
                "Орёл",
                "Оренбург",
                "Пенза",
                "Пермь",
                "Петрозаводск",
                "Петропавловск-Камчатский",
                "Псков",
                "Ростов-на-Дону",
                "Рязань",
                "Салехард",
                "Самара",
                "Санкт-Петербург",
                "Санкт-Петербург",
                "Саранск",
                "Саратов",
                "Севастополь",
                "Симферополь",
                "Смоленск",
                "Ставрополь",
                "Сыктывкар",
                "Тамбов",
                "Тверь",
                "Томск",
                "Тула",
                "Тюмень",
                "Улан-Удэ",
                "Ульяновск",
                "Уфа",
                "Хабаровск",
                "Ханты-Мансийск",
                "Чебоксары",
                "Челябинск",
                "Черкесск",
                "Чита",
                "Элиста",
                "Южно-Сахалинск",
                "Якутск",
                "Ярославль"
        };
    }

    public static CardOrderInputInfo getValidCardOrderInputInfoWithoutCity() {
        CardOrderInputInfo info = getValidCardOrderInputInfo();
        return new CardOrderInputInfo(null, info.getDate(), info.getName(), info.getPhone(), info.getIsAgreement());
    }

    public static CardOrderInputInfo getValidCardOrderInputInfoWithoutDate() {
        CardOrderInputInfo info = getValidCardOrderInputInfo();
        return new CardOrderInputInfo(info.getCity(), null, info.getName(), info.getPhone(), info.getIsAgreement());
    }

    public static CardOrderInputInfo getValidCardOrderInputInfoWithoutName() {
        CardOrderInputInfo info = getValidCardOrderInputInfo();
        return new CardOrderInputInfo(info.getCity(), info.getDate(), null, info.getPhone(), info.getIsAgreement());
    }

    public static CardOrderInputInfo getValidCardOrderInputInfoWithoutPhone() {
        CardOrderInputInfo info = getValidCardOrderInputInfo();
        return new CardOrderInputInfo(info.getCity(), info.getDate(), info.getName(), null, info.getIsAgreement());
    }

    public static CardOrderInputInfo getValidCardOrderInputInfoWithoutAgreement() {
        CardOrderInputInfo info = getValidCardOrderInputInfo();
        return new CardOrderInputInfo(info.getCity(), info.getDate(), info.getName(), info.getPhone(), false);
    }

//    @Test
//    void test() {
//        String date = nowWithDaysShift(-2);
//        String date2 = nowWithYearsShift(-2);
//        String ff = "" + getValidCardOrderInputInfo();
//        System.out.println();
//    }
}
