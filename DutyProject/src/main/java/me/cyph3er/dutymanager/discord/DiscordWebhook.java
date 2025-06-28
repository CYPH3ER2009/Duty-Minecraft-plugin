package me.cyph3er.dutymanager.discord;

import me.cyph3er.dutymanager.DutyManager;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordWebhook {

    public static void logDutyToggle(String player, boolean entered) {
        String msg = entered ?
                player + " entered duty mode." :
                player + " exited duty mode.";

        sendWebhook(msg);
    }

    public static void sendWeeklyReport(String msg) {
        sendWebhook("**Weekly Staff Report:**\n" + msg);
    }

    private static void sendWebhook(String content) {
        String webhook = DutyManager.getInstance().getConfig().getString("discord.webhook_url");
        if (webhook == null || webhook.isEmpty()) return;

        try {
            URL url = new URL(webhook);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            String json = "{\"content\":\"" + content.replace("\"", "\\\"") + "\"}";

            try (OutputStream os = con.getOutputStream()) {
                os.write(json.getBytes());
            }

            con.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
