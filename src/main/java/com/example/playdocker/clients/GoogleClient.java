package com.example.playdocker.clients;

import com.example.playdocker.models.Message;
import com.example.playdocker.models.Score;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleClient {

    //Funkcja tworząca obiekt Score dla podanego URL.
    public static Score getScoreForUrl(String url, String apiKey){
        Score score = new Score();
        try {
            String data = checkThreatLevel(url, apiKey);//Pobieranie do Stringa JSONa pobranego z API
            JSONParser parser = new JSONParser();
            try {
                JSONObject json = (JSONObject) parser.parse(data);//Przetworzenie Stringa na JSONObject
                JSONObject result = (JSONObject) json.get("result");

                score.setThreatType((String) result.get("threatType"));//Pobranie z JSONObject zmiennych do stworzenia obiektu Score
                score.setConfidenceLevel((String) result.get("confidenceLevel"));

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return score;
    }

    //Funkcja wysyłająca do API google request mający sprawdzić poziom zagrożenia URL z argumentu
    private static String checkThreatLevel(String uri, String apiKey) throws IOException {
        return postResponse("https://webrisk.googleapis.com/v1eap1:evaluateUri/json?uri:" + uri + "&threatTypes:SOCIAL_ENGINEERING" + "&allowScan:true" + "&key:" + apiKey);
    }

    //Funkcja wysyłająca request typu POST na podany adres. Zwraca zawartość odpowiedzi API w postaci Stringa.
    private static String postResponse(String uri) throws IOException {

        URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();//Utworzenie połączenia HTTP
        con.setRequestMethod("POST");//Ustawienie metody requesta jako POST
        con.setRequestProperty("Content-Length", "400");//Dodanie do headera wartości Content-Length

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {//Wyciągnięcie odpowiedzi do StringBuffera
            content.append(inputLine);
        }
        in.close();
        return String.valueOf(content);
    }
}