import java.util.List;
import java.util.Scanner;

class Main {
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        String request;
        StringBuilder response;
        do {
            System.out.println("Введите поисковой запрос:");
            request = scan.nextLine();
            response = WikiSearch.getInfo(request); //запрос к серверу
            if(response.isEmpty()){ //проверка на то, что поиск не вернул результатов
                System.out.println("Поиск не дал результатов, либо вы ввели некорректный запрос.");
            }
        }
        while(request.isEmpty() || response.isEmpty()); //работает, пока не получен корректный запрос

        List<String> searchResults = WikiSearch.parsing(response); //парсим
        WikiSearch.printSearchResults(searchResults); //выводим результаты
        int choise = -1;
        System.out.println("Выберите один из предложенных вариантов:");
        while (choise <= 0 || choise >= searchResults.size() + 1) {

            if (scan.hasNextInt()) {
                choise = scan.nextInt();
                scan.nextLine();
                if (choise <= 0 || choise >= searchResults.size() + 1) { //есть ли выбранный номер статьи
                    System.out.print("Выбран недопустимый номер статьи. Выберите номер статьи из предложенных вариантов: ");
                }
            }
            else {
                System.out.print("Неверный ввод. Введите число — номер статьи из предложенных вариантов: ");
                scan.nextLine();
            }
        }
        scan.close();
        String selectedOption = searchResults.get(choise - 1);
        WikiSearch.openURL(selectedOption); //открываем статью в браузере
    }
}