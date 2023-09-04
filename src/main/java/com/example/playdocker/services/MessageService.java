package com.example.playdocker.services;

import com.example.playdocker.Utils.MessageUtils;
import com.example.playdocker.models.Message;
import com.example.playdocker.models.Score;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageService {
    public static boolean subscribe(String senderNumber) {//Funkcja odpowiedzialna za zapisanie numeru telefonu do pliku CSV jeśli numer nie występuje jeszcze w bazie.
        String environment = MessageUtils.getEnvironmentFromProperties();//Wyciągnięcie zmiennej mówiącej czy operujemy na środowisku testowym czy produkcyjnym.
        String filePath;//Plik pełniący funkcję bazy.
        boolean isNumberInDatabase;
        boolean isSubscriptionOk = false;

        if(environment.equalsIgnoreCase("produkcja")) {//Zależnie od środowiska wybierane jest na której "bazie" operować
            filePath = "numeryTelefonówProd.csv";
        } else {
            filePath = "numeryTelefonówTest.csv";
        }

        isNumberInDatabase = checkIsNumberInBase(senderNumber, filePath);//Sprawdzenie czy numeru nie ma już w bazie

        if (isNumberInDatabase) {//Jeśli numer jest już w bazie, ustaw negatywną odpowiedź
            isSubscriptionOk = false;
        } else {//Jeśli numeru nie ma w bazie, jest on dodawany
            try {
                FileWriter csvWriter = new FileWriter(filePath, true);

                csvWriter.append(senderNumber);
                csvWriter.append(",");
                csvWriter.append("\n");

                csvWriter.flush();
                csvWriter.close();
                isSubscriptionOk = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return isSubscriptionOk;
    }

    public static boolean unsubscribe(String numberToRemove) {//Funkcja odpowiedzialna za usunięcie numeru telefonu z pliku CSV jeśli podany numer w niej istnieje.
        String environment = MessageUtils.getEnvironmentFromProperties();//Wyciągnięcie zmiennej mówiącej czy operujemy na środowisku testowym czy produkcyjnym.
        String filePath;//Plik pełniący funkcję bazy.
        boolean isNumberInDatabase;
        boolean isSubscriptionOk = false;

        if(environment.equalsIgnoreCase("produkcja")) {//Zależnie od środowiska wybierane jest na której "bazie" operować
            filePath = "numeryTelefonówProd.csv";
        } else {
            filePath = "numeryTelefonówTest.csv";
        }

        isNumberInDatabase = checkIsNumberInBase(numberToRemove, filePath);//Sprawdzenie czy numer jest w bazie

        if(isNumberInDatabase == true){//Jeśli numer jest w bazie, usuń go z niej
            try {
                List<String> lines = new ArrayList<>();
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                String line;
                isSubscriptionOk = true;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                reader.close();

                // Usuń wybrany numer z listy
                Iterator<String> iterator = lines.iterator();
                while (iterator.hasNext()) {
                    String currentLine = iterator.next();
                    if (currentLine.contains(numberToRemove)) {
                        iterator.remove();
                    }
                }

                // Zapisz zmodyfikowaną zawartość z powrotem do pliku CSV
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
                for (String modifiedLine : lines) {
                    writer.write(modifiedLine);
                    writer.newLine();
                }
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isSubscriptionOk;
    }

    public static String saveMessage(Message message) {//Funkcja zapisująca wiadomość do pliku CSV jeśli numer występuje w bazie subskrybentów.
        String environment = MessageUtils.getEnvironmentFromProperties();//Wyciągnięcie zmiennej mówiącej czy operujemy na środowisku testowym czy produkcyjnym.
        String filePath;//Plik pełniący funkcję bazy.
        String numbersFilePath;
        ArrayList<String> listOfUrls;
        boolean isNumberInDatabase;
        if(environment.equalsIgnoreCase("produkcja")){//Zależnie od środowiska wybierane jest na której "bazie" operować
            filePath = "wiadomosciProd.csv";
        } else {
            filePath = "wiadomosciTest.csv";
        }

        if(environment.equalsIgnoreCase("produkcja")){//Zależnie od środowiska wybierane jest na której "bazie" operować
            numbersFilePath = "numeryTelefonówProd.csv";
        } else {
            numbersFilePath = "numeryTelefonówTest.csv";
        }

        isNumberInDatabase = checkIsNumberInBase(message.getReceiverNumber(), numbersFilePath);//Sprawdzenie czy numer sybskrybuje usługę

        if(isNumberInDatabase){
            listOfUrls = findUrls(message.getContent());
        try {//Zapisanie wiadomości do pliku CSV

            FileWriter csvWriter = new FileWriter("wiadomosci.csv", true);

            csvWriter.append(message.getId());
            csvWriter.append(",");
            csvWriter.append(message.getSenderNumber());
            csvWriter.append(",");
            csvWriter.append(message.getReceiverNumber());
            csvWriter.append(",");
            csvWriter.append(message.getContent());
            csvWriter.append(",");
            csvWriter.append("\n");

            csvWriter.flush();
            csvWriter.close();

            return "Rekord został zapisany do pliku CSV.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Błąd podczas zapisu rekordu do pliku CSV.";
        }
        } else {
            return "Numer nie subskrybuje usługi";
        }
    }

    public static boolean checkIsNumberInBase(String number, String filePath) {//Funkcja sprawdzająca czy podany numer istnieje w pliku CSV.
        boolean numberFound = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {//Linia jest dzielona po separatorze
                String[] fields = line.split(",");

                for (String field : fields) {
                    if (field.trim().equals(number)) {
                        numberFound = true;
                        break;
                    }
                }

                if (numberFound) {
                    break; // Numer został znaleziony, nie trzeba dalej szukać
                }
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return numberFound;
    }

    public static ArrayList<String> findUrls(String message) {//Funkcja odpowiadająca za znalezienie wszystkich URLi w podanej wiadomości.
        ArrayList<String> urls = new ArrayList<>();

        String regex = "((https?)://[\\w\\d\\-\\./?=&#]+)|(www\\.[\\w\\d\\-\\./?=&#]+)|(ftp\\.[\\w\\d\\-\\./?=&#]+)"; //REGEX odpowiedzialny za znalezienie URL.
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);


        while (matcher.find()) {//Pętla dodająca wszystkie URLe do listy.
            String url = matcher.group();
            urls.add(url);
        }

        return urls;
    }

    public static boolean checkForDangerousLinksInArray(ArrayList<Score> scores){
        boolean isMessageDangerous = false;

        for (int i = 0; i < scores.size(); i++) {//Sprawdzanie czy wśród wyników jest niebezpieczny link
            if (!scores.get(i).getConfidenceLevel().equalsIgnoreCase("safe")) {
                isMessageDangerous = true;
            }
        }
        return isMessageDangerous;
    }
}