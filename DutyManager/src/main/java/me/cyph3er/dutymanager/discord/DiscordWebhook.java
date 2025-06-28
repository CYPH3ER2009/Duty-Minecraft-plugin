import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscordWebhook {

    private final String url;
    private String content;
    private String username;
    private String avatarUrl;
    private boolean tts;
    private final List<EmbedObject> embeds = new ArrayList<>();

    public DiscordWebhook(String url) {
        this.url = url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setTts(boolean tts) {
        this.tts = tts;
    }

    public void addEmbed(EmbedObject embed) {
        this.embeds.add(embed);
    }

    public void execute() throws java.io.IOException {
        if (this.content == null && this.embeds.isEmpty()) {
            throw new IllegalArgumentException("Set content or add at least one EmbedObject");
        }

        JSONObject json = new JSONObject();
        json.put("content", this.content);
        json.put("username", this.username);
        json.put("avatar_url", this.avatarUrl);
        json.put("tts", this.tts);

        if (!this.embeds.isEmpty()) {
            List<JSONObject> embedObjects = new ArrayList<>();
            for (EmbedObject embed : this.embeds) {
                JSONObject jsonEmbed = new JSONObject();
                jsonEmbed.put("title", embed.title);
                jsonEmbed.put("description", embed.description);
                jsonEmbed.put("url", embed.url);
                jsonEmbed.put("color", embed.color);

                if (embed.author != null) {
                    JSONObject jsonAuthor = new JSONObject();
                    jsonAuthor.put("name", embed.author.name);
                    jsonAuthor.put("url", embed.author.url);
                    jsonAuthor.put("icon_url", embed.author.iconUrl);
                    jsonEmbed.put("author", jsonAuthor);
                }
                
                if (embed.footer != null) {
                    JSONObject jsonFooter = new JSONObject();
                    jsonFooter.put("text", embed.footer.text);
                    jsonFooter.put("icon_url", embed.footer.iconUrl);
                    jsonEmbed.put("footer", jsonFooter);
                }
                
                if (embed.thumbnail != null) {
                    JSONObject jsonThumbnail = new JSONObject();
                    jsonThumbnail.put("url", embed.thumbnail.url);
                    jsonEmbed.put("thumbnail", jsonThumbnail);
                }
                
                if (embed.image != null) {
                    JSONObject jsonImage = new JSONObject();
                    jsonImage.put("url", embed.image.url);
                    jsonEmbed.put("image", jsonImage);
                }

                if (!embed.fields.isEmpty()) {
                    List<JSONObject> jsonFields = new ArrayList<>();
                    for (FieldObject field : embed.fields) {
                        JSONObject jsonField = new JSONObject();
                        jsonField.put("name", field.name);
                        jsonField.put("value", field.value);
                        jsonField.put("inline", field.inline);
                        jsonFields.add(jsonField);
                    }
                    jsonEmbed.put("fields", jsonFields);
                }

                embedObjects.add(jsonEmbed);
            }
            json.put("embeds", embedObjects);
        }

        URL webhookUrl = new URL(this.url);
        HttpURLConnection connection = (HttpURLConnection) webhookUrl.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "Java-Discord-Webhook");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        try (OutputStream stream = connection.getOutputStream()) {
            stream.write(json.toString().getBytes("UTF-8"));
            stream.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode < 200 || responseCode >= 300) {
            throw new java.io.IOException("Webhook failed to execute with response code: " + responseCode);
        }
    }

    public static class EmbedObject {
        private String title;
        private String description;
        private String url;
        private Integer color;
        private AuthorObject author;
        private FooterObject footer;
        private ImageObject image;
        private ThumbnailObject thumbnail;
        private final List<FieldObject> fields = new ArrayList<>();

        public EmbedObject setTitle(String title) {
            this.title = title;
            return this;
        }

        public EmbedObject setDescription(String description) {
            this.description = description;
            return this;
        }

        public EmbedObject setUrl(String url) {
            this.url = url;
            return this;
        }

        public EmbedObject setColor(Integer color) {
            this.color = color;
            return this;
        }

        public EmbedObject setAuthor(String name, String url, String iconUrl) {
            this.author = new AuthorObject(name, url, iconUrl);
            return this;
        }

        public EmbedObject setFooter(String text, String iconUrl) {
            this.footer = new FooterObject(text, iconUrl);
            return this;
        }
        
        public EmbedObject setImage(String url) {
            this.image = new ImageObject(url);
            return this;
        }
        
        public EmbedObject setThumbnail(String url) {
            this.thumbnail = new ThumbnailObject(url);
            return this;
        }

        public EmbedObject addField(String name, String value, boolean inline) {
            this.fields.add(new FieldObject(name, value, inline));
            return this;
        }
    }
    
    private static class AuthorObject {
        private final String name;
        private final String url;
        private final String iconUrl;
        
        private AuthorObject(String name, String url, String iconUrl) {
            this.name = name;
            this.url = url;
            this.iconUrl = iconUrl;
        }
    }
    
    private static class FooterObject {
        private final String text;
        private final String iconUrl;
        
        private FooterObject(String text, String iconUrl) {
            this.text = text;
            this.iconUrl = iconUrl;
        }
    }
    
    private static class ImageObject {
        private final String url;
        
        private ImageObject(String url) {
            this.url = url;
        }
    }
    
    private static class ThumbnailObject {
        private final String url;
        
        private ThumbnailObject(String url) {
            this.url = url;
        }
    }

    private static class FieldObject {
        private final String name;
        private final String value;
        private final boolean inline;

        private FieldObject(String name, String value, boolean inline) {
            this.name = name;
            this.value = value;
            this.inline = inline;
        }
    }
    
    private static class JSONObject {
        private final Map<String, Object> map = new HashMap<>();
        
        public void put(String key, Object value) {
            if (value != null) {
                map.put(key, value);
            }
        }
        
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("{");
            boolean first = true;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!first) {
                    builder.append(",");
                }
                builder.append("\"").append(entry.getKey()).append("\":");
                
                Object value = entry.getValue();
                if (value instanceof String) {
                    builder.append("\"").append(escapeJson(value.toString())).append("\"");
                } else if (value instanceof Integer || value instanceof Double || value instanceof Boolean) {
                    builder.append(value);
                } else if (value instanceof List) {
                    builder.append("[");
                    boolean firstItem = true;
                    for (Object item : (List<?>) value) {
                        if (!firstItem) {
                            builder.append(",");
                        }
                        builder.append(item.toString());
                        firstItem = false;
                    }
                    builder.append("]");
                } else if (value instanceof JSONObject) {
                    builder.append(value.toString());
                } else {
                    builder.append("\"").append(escapeJson(value.toString())).append("\"");
                }
                first = false;
            }
            builder.append("}");
            return builder.toString();
        }
        
        private String escapeJson(String s) {
            return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\b", "\\b").replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
        }
    }
}