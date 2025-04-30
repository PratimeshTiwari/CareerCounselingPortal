package com.example.careercounseling.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.careercounseling.api.key;

public class GeminiAPIService {
    // Updated to use the gemini-2.0-flash model
        private static final String API_KEY = key.API_KEY;
    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
                    + API_KEY;

    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_BACKOFF_MS = 1_000L;
    private static final String[] FALLBACK = {
            "Software Developer", "Data Scientist",
            "Cloud Architect", "Cybersecurity Analyst", "ML Engineer"
    };

    public String getCareerSuggestion(String userData) {
        // very short prompt
        String prompt = "Suggest career for: " + userData;
        return callGeminiAPI(prompt);
    }

    public String getDetailedCareerPathway(String career) {
        String prompt = "Detail path for: " + career;
        return callGeminiAPI(prompt);
    }

    private String callGeminiAPI(String prompt) {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection)new URL(GEMINI_API_URL).openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                // minimal JSON: no extra fields
                String body = "{\"contents\":[{\"parts\":[{\"text\":\""
                        + prompt + "\"}]}]}";

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body.getBytes(StandardCharsets.UTF_8));
                }

                int status = conn.getResponseCode();
                InputStream in = (status >= 200 && status < 300)
                        ? conn.getInputStream()
                        : conn.getErrorStream();

                // read all
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(in, StandardCharsets.UTF_8)
                );
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                String json = sb.toString();
                System.out.println("🔍 response " + attempt + ": " + json);

                if (status == 503) {
                    Thread.sleep(INITIAL_BACKOFF_MS * (1L << (attempt - 1)));
                    continue;
                }
                if (status < 200 || status >= 300) {
                    return "Service unavailable (HTTP " + status + ")";
                }

                // parse
                JSONObject root = new JSONObject(json);
                JSONArray cands = root.optJSONArray("candidates");
                if (cands != null && cands.length() > 0) {
                    return cands
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text")
                            .trim();
                }
                return root.toString();
            } catch (IOException | JSONException | InterruptedException e) {
                if (attempt == MAX_RETRIES) break;
                try { Thread.sleep(INITIAL_BACKOFF_MS * (1L << (attempt - 1))); }
                catch (InterruptedException ignored) {}
            } finally {
                if (conn != null) conn.disconnect();
            }
        }
        // fallback
        return "Offline suggestion: "
                + FALLBACK[(int)(Math.random() * FALLBACK.length)];
    }
}