package com.example.playdocker.controllers;

import com.example.playdocker.Utils.MessageUtils;
import com.example.playdocker.clients.GoogleClient;
import com.example.playdocker.models.Message;
import com.example.playdocker.models.Score;
import com.example.playdocker.services.MessageService;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

import static com.example.playdocker.services.MessageService.findUrls;

@RestController
public class MessageController {

    @PostMapping("/receiveMessage")//Funkcja przyjmująca request typu POST.
    public String receiveMessage(@RequestBody Message message){

        String apiKey = MessageUtils.getApiKeyFromProperties();//Pobieranie klucza API z propertiesów
        ArrayList<Score> scores = new ArrayList<Score>();//Lista wyników testowanych URLi zwrócona przez API google
   /*     ArrayList<String> listOfUrls = findUrls(message.getContent());//Tworzenie listy z URLi wyszukanych w wiadomości
        for (int i = 0; i < listOfUrls.size(); i++) {//Pętla pobierająca wyniki testów dla każdego z URLi i dodaje je do listy wyników
            Score score = GoogleClient.getScoreForUrl(listOfUrls.get(i), apiKey);
            scores.add(score);
        }
        boolean isMessageDangerous = MessageService.checkForDangerousLinksInArray(scores);//Sprawdzenie, czy na liście wyników któryś z URLi nie jest niebezpieczny */
        MessageService.saveMessage(message);//Funkcja zapisująca wiadomość do bazy danych
        boolean isMessageDangerous = false;
        if(isMessageDangerous) {
            return "Wiadomość może być potencjalnie niebezpieczna";
        } else {
            return "Wiadomość jest bezpieczna";
        }
    }

    @PostMapping("/subscription")//Funkcja przyjmująca request typu POST. Odpowiada za przyjęcie wiadomości o subskrypcji lub jej anulowaniu.
    public String subscription(@RequestBody Message message){
        if(message.getContent().equalsIgnoreCase("start")){//Jeśli wiadomość brzmi "start" to subskrybuj
            boolean isSubscriptionCorrect = MessageService.subscribe(message.getSenderNumber());
            if(isSubscriptionCorrect){
            return "Pomyślnie zasubskrybowano";} else {
                return "Wystąpił błąd podczas dodawania subskrypcji";
            }
        } else if(message.getContent().equalsIgnoreCase("stop")){//Jeśli wiadomość brzmi "stop" to odsubskrybuj
            boolean isSubscriptionCorrect = MessageService.unsubscribe(message.getSenderNumber());
            if(isSubscriptionCorrect){
            return "Pomyślnie odsubskrybowano";} else {
                return "Wystąpił błąd podczas anulowania subskrypcji";
            }
        } else {//Nieobsługiwana treść wiadomości
            return "Nieprawidłowa treść wiadomości";
        }
    }
}