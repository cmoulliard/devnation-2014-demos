package org.devnation.camel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InsightUtils {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public static String formatDate(long timestamp) {
        return simpleDateFormat.format(new Date(timestamp));
    }

    /**
     * Produce a string in double quotes with backslash sequences in all the
     * right places. A backslash will be inserted within </, producing <\/,
     * allowing JSON text to be delivered in HTML. In JSON text, a string
     * cannot contain a control character or an unescaped quote or backslash.
     * @param string A String
     * @return  A String correctly formatted for insertion in a JSON text.
     */
    public static void quote(String string, StringBuilder w) {
        if (string == null || string.length() == 0) {
            w.append("\"\"");
            return;
        }

        char         b;
        char         c = 0;
        String       hhhh;
        int          i;
        int          len = string.length();

        w.append('"');
        for (i = 0; i < len; i += 1) {
            b = c;
            c = string.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    w.append('\\');
                    w.append(c);
                    break;
                case '/':
                    if (b == '<') {
                        w.append('\\');
                    }
                    w.append(c);
                    break;
                case '\b':
                    w.append("\\b");
                    break;
                case '\t':
                    w.append("\\t");
                    break;
                case '\n':
                    w.append("\\n");
                    break;
                case '\f':
                    w.append("\\f");
                    break;
                case '\r':
                    w.append("\\r");
                    break;
                default:
                    if (c < ' ' || (c >= '\u0080' && c < '\u00a0') ||
                            (c >= '\u2000' && c < '\u2100')) {
                        hhhh = "000" + Integer.toHexString(c);
                        w.append("\\u" + hhhh.substring(hhhh.length() - 4));
                    } else {
                        w.append(c);
                    }
            }
        }
        w.append('"');
    }

}