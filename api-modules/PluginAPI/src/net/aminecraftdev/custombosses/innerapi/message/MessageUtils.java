package net.aminecraftdev.custombosses.innerapi.message;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by charl on 28-Apr-17.
 */
public class MessageUtils {

    private final static int CENTER_PX = 154;
    private final static int MAX_PX = 250;

    public static final int getMaxPixel() {
        return MAX_PX;
    }

    public static final List<String> getCenteredMessage(String message) {
        if(message == null || message.equals("")) return new ArrayList<>();
        List<String> arrayList = new ArrayList<>();

        message = translateString(message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;
        int charIndex = 0;
        int lastSpaceIndex = 0;
        String toSendAfter = null;
        String recentColorCode = "";

        for(char c : message.toCharArray()) {
            if(c == '§') {
                previousCode = true;
                continue;
            } else if(previousCode) {
                previousCode = false;

                if(c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else {
                    isBold = false;
                }
            } else if(c == ' ') {
                lastSpaceIndex = charIndex;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);

                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }

            if(messagePxSize >= MAX_PX) {
                toSendAfter = recentColorCode + message.substring(lastSpaceIndex + 1, message.length());
                message = message.substring(0, lastSpaceIndex + 1);
                break;
            }

            charIndex++;
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder stringBuilder = new StringBuilder();

        while (compensated < toCompensate) {
            stringBuilder.append(" ");
            compensated += spaceLength;
        }

        String s = stringBuilder.toString() + message;
        arrayList.add(s);

        if(toSendAfter != null) {
            return null;
        }

        return arrayList;
    }

    public static final void sendCenteredMessage(CommandSender player, String message) {
        if(message == null || message.equals("")) player.sendMessage("");

        message = translateString(message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;
        int charIndex = 0;
        int lastSpaceIndex = 0;
        String toSendAfter = null;
        String recentColorCode = "";

        for(char c : message.toCharArray()) {
            if(c == '§') {
                previousCode = true;
                continue;
            } else if(previousCode) {
                previousCode = false;

                if(c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else {
                    isBold = false;
                }
            } else if(c == ' ') {
                lastSpaceIndex = charIndex;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);

                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }

            if(messagePxSize >= MAX_PX) {
                toSendAfter = recentColorCode + message.substring(lastSpaceIndex + 1, message.length());
                message = message.substring(0, lastSpaceIndex + 1);
                break;
            }

            charIndex++;
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder stringBuilder = new StringBuilder();

        while (compensated < toCompensate) {
            stringBuilder.append(" ");
            compensated += spaceLength;
        }

        player.sendMessage(stringBuilder.toString() + message);

        if(toSendAfter != null) {
            sendCenteredMessage(player, toSendAfter);
        }
    }

    public static final String translateString(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
