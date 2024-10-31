import java.util.*;

public class SimpleJsonParser {

    public static void main(String[] args) {
        String jsonString = "{\"name\": \"John\", \"age\": 30, \"isStudent\": false, \"courses\": [\"Math\", \"Science\"]}";
        Object result = parseJson(jsonString);
        System.out.println(result);
    }

    public static Object parseJson(String json) {
        json = json.trim();
        if (json.startsWith("{")) {
            return parseObject(json);
        } else if (json.startsWith("[")) {
            return parseArray(json);
        } else {
            return parseValue(json);
        }
    }

    private static Map<String, Object> parseObject(String json) {
        Map<String, Object> map = new HashMap<>();
        json = json.substring(1, json.length() - 1).trim();

        String[] keyValues = splitKeyValues(json);
        for (String keyValue : keyValues) {
            String[] pair = keyValue.split(":", 2);
            String key = pair[0].trim().replaceAll("^\"|\"$", "");
            Object value = parseJson(pair[1].trim());
            map.put(key, value);
        }
        return map;
    }

    private static List<Object> parseArray(String json) {
        List<Object> list = new ArrayList<>();
        json = json.substring(1, json.length() - 1).trim();

        String[] values = splitValues(json);
        for (String value : values) {
            list.add(parseJson(value.trim()));
        }
        return list;
    }

    private static String[] splitKeyValues(String json) {
        List<String> keyValues = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int braceCount = 0;
        boolean inQuotes = false;

        for (char ch : json.toCharArray()) {
            if (ch == '\"') {
                inQuotes = !inQuotes;
            }
            if (!inQuotes) {
                if (ch == ',' && braceCount == 0) {
                    keyValues.add(current.toString().trim());
                    current.setLength(0);
                    continue;
                }
                if (ch == '{') braceCount++;
                if (ch == '}') braceCount--;
            }
            current.append(ch);
        }
        if (current.length() > 0) {
            keyValues.add(current.toString().trim());
        }
        return keyValues.toArray(new String[0]);
    }

    private static String[] splitValues(String json) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int braceCount = 0;
        boolean inQuotes = false;

        for (char ch : json.toCharArray()) {
            if (ch == '\"') {
                inQuotes = !inQuotes;
            }
            if (!inQuotes) {
                if (ch == ',' && braceCount == 0) {
                    values.add(current.toString().trim());
                    current.setLength(0);
                    continue;
                }
                if (ch == '[') braceCount++;
                if (ch == ']') braceCount--;
            }
            current.append(ch);
        }
        if (current.length() > 0) {
            values.add(current.toString().trim());
        }
        return values.toArray(new String[0]);
    }

    private static Object parseValue(String json) {
        if (json.equals("null")) return null;
        if (json.equals("true")) return true;
        if (json.equals("false")) return false;
        if (json.startsWith("\"") && json.endsWith("\"")) {
            return json.substring(1, json.length() - 1);
        }
        try {
            return Integer.parseInt(json);
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(json);
            } catch (NumberFormatException e2) {
                throw new RuntimeException("Invalid JSON value: " + json);
            }
        }
    }
}
