/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.core.notifications;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.annotations.VisibleForTesting;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

@Data
@Slf4j
@Singleton
public class MobileModel {
    public static final String PHONE_SEPARATOR_ESCAPED = "\\|"; // see https://stackoverflow.com/questions/5675704/java-string-split-not-returning-the-right-values
    public static final String PHONE_SEPARATOR_WRITING = "|";

    public enum OS {
        UNDEFINED(""),
        IOS("iOS"),
        IOS_DEV("iOSDev"),
        ANDROID("android");

        @Getter
        private String magicString;

        OS(String magicString) {
            this.magicString = magicString;
        }
    }

    @Nullable
    private OS os;
    @Nullable
    private String descriptor;
    @Nullable
    private String key;
    @Nullable
    private String token;
    private boolean isContentAvailable = true;

    @Inject
    public MobileModel() {
    }

    public void reset() {
        os = null;
        key = null;
        token = null;
    }

    public void applyKeyAndToken(String keyAndToken) {
        log.info("applyKeyAndToken: keyAndToken={}", keyAndToken.substring(0, 20) + "...(truncated in log for privacy reasons)");
        String[] tokens = keyAndToken.split(PHONE_SEPARATOR_ESCAPED);
        String magic = tokens[0];
        descriptor = tokens[1];
        key = tokens[2];
        token = tokens[3];
        if (magic.equals(OS.IOS.getMagicString()))
            os = OS.IOS;
        else if (magic.equals(OS.IOS_DEV.getMagicString()))
            os = OS.IOS_DEV;
        else if (magic.equals(OS.ANDROID.getMagicString()))
            os = OS.ANDROID;

        isContentAvailable = doesSupportContentAvailable(descriptor);
    }

    @VisibleForTesting
    boolean doesSupportContentAvailable(String descriptor) {
        // When testing with isContentAvailable set, notifications did not work on an
        // iPhone 6 but they did work on an iPhone 6S.
        // We don't know about other versions or models, but we will make assumptions
        // based on the device descriptor as to whether it supports isContentAvailable.
        /*
        iPod Touch 7 (iPod9,1)
        iPhone 6s (iPhone8,1)
        iPhone 6s Plus (iPhone8,2)
        iPhone SE (iPhone8,4)
        iPhone 7 (iPhone9,1)
        iPhone 7 Plus (iPhone9,2)
        iPhone 8 (iPhone10,1)
        iPhone 8 Plus (iPhone10,2)
        iPhone X (iPhone10,3)
        iPhone XS (iPhone11,2)
        iPhone XS Max (iPhone11,6)
        iPhone XR (iPhone11,8)
        iPhone 11 (iPhone12,1)
        iPhone 11 Pro (iPhone12,3)
        iPhone 11 Pro Max (iPhone12,5)
        iPad Air 2 (iPad5,4)
        iPad Air 3 (iPad11,4)
        iPad 5 (iPad6,12)
        iPad 6 (iPad7,6)
        iPad 7 (iPad7,12)
        iPad Mini 4 (iPad5,2)
        iPad Mini 5 (iPad11,2)
        iPad Pro 9.7 Inch (iPad6,4)
        iPad Pro 12.9 Inch (iPad6,8)
        iPad Pro 10.5 Inch, 2nd Gen (iPad7,4)
        iPad Pro 12.9 Inch, 2nd Gen (iPad7,2)
        iPad Pro 11 Inch, 3rd Gen (iPad8,3)
        iPad Pro 12.9 Inch, 3rd Gen (iPad8,7)
        */
        if (descriptor != null) {
            String[] descriptorTokens = descriptor.split(" ");
            if (descriptorTokens.length > 1 || !descriptor.contains(",")) {
                String model = descriptorTokens[0];
                if (model.equals("iPhone")) {
                    if (descriptorTokens.length == 1) {
                        return false;
                    }
                    String versionString = descriptorTokens[1];
                    String[] validVersions = {"SE", "X", "XS", "XR"};
                    if (Arrays.asList(validVersions).contains(versionString)) {
                        return true;
                    }
                    String versionSuffix = "";
                    if (versionString.matches("\\d[^\\d]")) {
                        versionSuffix = versionString.substring(1);
                        versionString = versionString.substring(0, 1);
                    } else if (versionString.matches("\\d{2}[^\\d]")) {
                        versionSuffix = versionString.substring(2);
                        versionString = versionString.substring(0, 2);
                    }
                    try {
                        int version = Integer.parseInt(versionString);
                        return version > 6 || (version == 6 && versionSuffix.equalsIgnoreCase("s"));
                    } catch (Throwable ignore) {
                    }
                } else if (model.equals("iPad")) {
                    try {
                        switch (descriptorTokens[1]) {
                            case "Pro":
                                return true;
                            case "Mini":
                                return Integer.parseInt(descriptorTokens[2]) >= 4;
                            case "Air":
                                return Integer.parseInt(descriptorTokens[2]) >= 2;
                            default:
                                return Integer.parseInt(descriptorTokens[1]) >= 5;
                        }
                    } catch (Throwable ignore) {
                    }
                } else if (model.equals("iPod")) {
                    try {
                        return descriptorTokens[1].equals("Touch") && Integer.parseInt(descriptorTokens[2]) >= 7;
                    } catch (Throwable ignore) {
                    }
                }
            } else {
                Pattern pattern = Pattern.compile("(iPod|iPad|iPhone)(\\d+),(\\d+)");
                Matcher matcher = pattern.matcher(descriptor);
                if (matcher.matches()) {
                    String device = matcher.group(1);
                    int version = Integer.parseInt(matcher.group(2));
                    switch (device) {
                        case "iPod":
                            return version >= 9;
                        case "iPad":
                            return version >= 5;
                        case "iPhone":
                            return version >= 8;
                    }
                }
            }
        }
        return false;
    }
}
