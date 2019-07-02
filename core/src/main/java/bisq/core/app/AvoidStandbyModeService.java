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

package bisq.core.app;

import bisq.core.user.Preferences;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.extern.slf4j.Slf4j;



import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

@Slf4j
public class AvoidStandbyModeService {
    private final Preferences preferences;
    private volatile boolean isStopped;

    @Inject
    public AvoidStandbyModeService(Preferences preferences) {
        this.preferences = preferences;

        preferences.getUseStandbyModeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                isStopped = true;
                log.info("AvoidStandbyModeService stopped");
            } else {
                start();
            }
        });
    }

    public void init() {
        isStopped = preferences.isUseStandbyMode();
        if (!isStopped) {
            start();
        }
    }

    private void start() {
        isStopped = false;
        log.info("AvoidStandbyModeService started");
        Thread thread = new Thread(this::play);
        thread.setName("AvoidStandbyModeService-thread");
        thread.start();
    }

    private void play() {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        File soundFile;
        AudioInputStream audioInputStream = null;
        AudioFormat audioFormat = null;
        try {
            soundFile = new File(BisqEnvironment.getStaticAppDataDir(), "prevent-app-nap-silent-sound.aiff");
            if (!soundFile.exists()) {
                inputStream = getClass().getClassLoader().getResourceAsStream("prevent-app-nap-silent-sound.aiff");
                outputStream = new FileOutputStream(soundFile);
                IOUtils.copy(inputStream, outputStream);
            }
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            audioFormat = audioInputStream.getFormat();
        } catch (IOException | UnsupportedAudioFileException e) {
            log.error("AvoidStandbyModeService failed; {}", e.toString());
            isStopped = true;
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException ignore) {
            }
        }
        while (!isStopped) {
            try {
                byte[] tempBuffer = new byte[10000];
                DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
                SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                sourceDataLine.open(audioFormat);
                sourceDataLine.start();
                int cnt;
                while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1 && !isStopped) {
                    if (cnt > 0) {
                        sourceDataLine.write(tempBuffer, 0, cnt);
                    }
                }
                sourceDataLine.drain();
                sourceDataLine.close();
            } catch (Exception e) {
                log.error(e.toString());
                e.printStackTrace();
            }
        }
    }
}
