[![Build status](https://ci.appveyor.com/api/projects/status/7qyu3pc2h9j4m8kj?svg=true)](https://ci.appveyor.com/project/PavlyukovVladimir/pavlyukovvvqamid45autotestingpatterns1)

***Павлюков Владимир Владимирович, группа*** **QAMID45**

# Домашнее задание к занятию «2.3. Patterns»

<details><summary>Вводная часть.</summary>

В качестве результата пришлите ссылку на ваш GitHub-проект в личном кабинете студента на сайте [netology.ru](https://netology.ru).

Все задачи этого занятия нужно делать **в разных репозиториях**.

[Шаблон для ДЗ](https://github.com/netology-code/aqa-code/tree/master/patterns).

**Важно**: если у вас что-то не получилось, то оформляйте issue [по установленным правилам](https://github.com/netology-code/aqa-homeworks/blob/master/report-requirements.md).

**Важно**: не делайте ДЗ всех занятий в одном репозитории. Иначе вам потом придётся достаточно сложно подключать системы Continuous integration.

## Как сдавать задачи

1. Инициализируйте на своём компьютере пустой Git-репозиторий.
1. Добавьте в него готовый файл [.gitignore](https://github.com/netology-code/aqa-homeworks/blob/master/.gitignore).
1. Добавьте в этот же каталог код ваших автотестов.
1. Сделайте необходимые коммиты.
1. Добавьте в каталог `artifacts` целевой сервис: `app-card-delivery.jar` для первой задачи, `app-ibank.jar` для второй задачи — см. раздел Настройка CI.
1. Создайте публичный репозиторий на GitHub и свяжите свой локальный репозиторий с удалённым.
1. Сделайте пуш — удостоверьтесь, что ваш код появился на GitHub.
1. Удостоверьтесь, что на AppVeyor сборка зелёная.
1. Поставьте бейджик сборки вашего проекта в файл README.md.
1. Ссылку на ваш проект отправьте в личном кабинете на сайте [netology.ru](https://netology.ru).
1. Задачи, отмеченные как необязательные, можно не сдавать, это не повлияет на получение зачёта.
1. Если вы обнаружили подозрительное поведение SUT, похожее на баг, создайте описание в issue на GitHub. [Придерживайтесь схемы при описании](https://github.com/netology-code/aqa-homeworks/blob/master/report-requirements.md).

## Настройка CI

Настройка CI осуществляется аналогично предыдущему заданию, за исключением того, что файл целевого сервиса может называться по-другому. Для второй задачи вам также понадобится указать нужный флаг запуска для тестового режима.

</details>

## Задача №1: заказ доставки карты (изменение даты)

<details><summary>Развернуть Задача №1: заказ доставки карты (изменение даты)</summary>

Вам необходимо автоматизировать тестирование новой функции формы заказа доставки карты:

![img_1.png](artifacts/imgs/img_1.png)![](https://github.com/ne![img.png](img.png)tology-code/aqa-homeworks/blob/master/patterns/pic/order.png)

Требования к содержимому полей, сообщения и другие элементы, по словам заказчика и разработчиков, такие же, они ничего не меняли.

Примечание: личный совет — не забудьте это перепроверить, никому нельзя доверять 😈

Тестируемая функциональность: если заполнить форму повторно теми же данными, за исключением «Даты встречи», то система предложит перепланировать время встречи:

![img.png](artifacts/imgs/img.png)![](https://github.com/netology-code/aqa-homeworks/blob/master/patterns/pic/replan.png)

После нажатия кнопки «Перепланировать» произойдёт перепланирование встречи:

![img_2.png](artifacts/imgs/img_2.png)![](https://github.com/netology-code/aqa-homeworks/blob/master/patterns/pic/success.png)

**Важно:** в этот раз вы не должны хардкодить данные прямо в тест. Используйте Faker, Lombok, data-классы для группировки нужных полей и утилитный класс-генератор данных — см. пример в презентации.

Утилитными называют классы, у которых приватный конструктор и статичные методы.

Обратите внимание, что Faker может генерировать не совсем в нужном для вас формате.

</details>

# Запуск тестов

1. Убедиться, что в файле [Constants.java](java/ru/netology/data/Constants.java),
```java
public static final boolean PRE_TEST_PREPARATION = true;
public static final boolean POST_TEST_PREPARATION = true;
```

2. Запустить в терминале выполнение тестов:

`./gradlew clean test`

3. Запустить сборку отчета allure:

`./gradlew allureReport`

4. [Просмотр отчета gradle(локальное выполнение тестов)](build/reports/tests/test/index.html)

5. [Просмотр allure отчета](build/reports/allure-report/allureReport/index.html)

<details><summary>Для тех кто любит больше контроля:</summary>

1. Установить в файле [Constants.java](java/ru/netology/data/Constants.java),
```java
public static final boolean PRE_TEST_PREPARATION = false;
public static final boolean POST_TEST_PREPARATION = false;
```

2. Runs server:
```sh
java -jar artifacts/app-card-delivery.jar & echo $! > ./testserver.pid &
```
(_id процесса сохраняется в файл, чтобы потом, если нужно, было проще вручную послать ему сигнал корректно завершиться_)

_на удаленной машине эта команда будет чуть другая `java -jar ./artifacts/app-card-delivery.jar &`_

3. Runs all tests: `./gradlew clean test`

_на удаленной машине эта команда тоже будет чуть другая `./gradlew test -Dselenide.headless=true --info`_

4. Shut down the server
```sh
kill -TERM $(cat ./testserver.pid)
```

5. Отчеты:

* [Просмотр отчета gradle(локальное выполнение тестов)](build/reports/tests/test/index.html)
* [Просмотр отчета(appveyor выполнение тестов при push. Можно скачать архив отчета reports.zip, расположен на вкладке Artifacts)](https://ci.appveyor.com/project/PavlyukovVladimir/pavlyukovvvqamid45autotestingselenide/history)
* Allure report:
    * `./gradlew allureReport --clean` - generates an Allure report
    * [Просмотр allure отчета](build/reports/allure-report/allureReport/index.html)


* Alternative allure report: `./gradlew allureServe` - generates an Allure report and opens it in the default browser
</details>

# Багрепорты

* [Не нужное требование подтвердить перепланировку при аналогичной дате](https://github.com/PavlyukovVladimir/PavlyukovVVQamid45AutotestingPatterns1/issues/9)
* [Короткие телефонные номера не вызывают ошибки](https://github.com/PavlyukovVladimir/PavlyukovVVQamid45AutotestingPatterns1/issues/8)
* [Орфографическая ошибка в сообщении об ошибке, когда поле имя неверно](https://github.com/PavlyukovVladimir/PavlyukovVVQamid45AutotestingPatterns1/issues/7)
* [Админ центры Гатчина и Красногорск вызывают ошибку](https://github.com/PavlyukovVladimir/PavlyukovVVQamid45AutotestingPatterns1/issues/6)
* [Ё в имени вызывает ошибку](https://github.com/PavlyukovVladimir/PavlyukovVVQamid45AutotestingPatterns1/issues/2)
* [ё в имени вызывает ошибку](https://github.com/PavlyukovVladimir/PavlyukovVVQamid45AutotestingPatterns1/issues/1)
* [Имя "-" не вызывает ошибку](https://github.com/PavlyukovVladimir/PavlyukovVVQamid45AutotestingPatterns1/issues/5)
* [Имя с "-" в конце не вызывает ошибку](https://github.com/PavlyukovVladimir/PavlyukovVVQamid45AutotestingPatterns1/issues/4)
* [Имя с "-" в начале не вызывает ошибку](https://github.com/PavlyukovVladimir/PavlyukovVVQamid45AutotestingPatterns1/issues/3)
