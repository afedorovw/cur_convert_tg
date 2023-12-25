package ru.edu.cct.bot;

public interface CommandAndInfo {

    String START = "/start";
    String USD = "/usd";
    String EUR = "/eur";
    String LIST = "/list";
    String HELP = "/info";

    String startBot = """
                Добро пожаловать в бот, %s!
                
                Здесь Вы сможете узнать официальные курсы валют на сегодня, установленные ЦБ РФ.
                
                Для этого воспользуйтесь командами:
                /usd - курс доллара
                /eur - курс евро
                /list -  курс всех доступных валют
                
                Дополнительные команды:
                /info - получение справки
                """;

    String helpComm = """
                Справочная информация по боту
                
                Для получения текущих курсов валют воспользуйтесь командами:
                /usd - курс доллара
                /eur - курс евро
                /list -  курс всех доступных валют
                """;

    String curr = "Курс на %s составляет %s рублей";

    String errorCurLog = "Ошибка получения курса валюты";

    String errorCurLogInfo = "Не удалось получить текущий курс валют. Попробуйте позже.";
}
