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

package bisq.desktop.components;

import bisq.core.locale.TradeCurrency;

import com.jfoenix.controls.JFXComboBox;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.event.EventHandler;

import javafx.collections.ObservableList;


public class CustomComboBox<T> extends JFXComboBox<T> {
    private String keys;

    private class KeyHandler implements EventHandler<KeyEvent> {

        public KeyHandler() {
            keys = "";
        }

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ESCAPE) {
                keys = "";
                return;
            } else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP) {
                keys = "";
                return;
            } else if (event.getCode() == KeyCode.BACK_SPACE && keys.length() > 0) {
                keys = keys.substring(0, keys.length() - 1);
            } else if (event.getCode().isLetterKey()) {
                keys += event.getText();
            } else {
                return;
            }

            if (keys.length() == 0) {
                return;
            }

            for (T item : getItems()) {
                // TODO handle other item instances
                if (item instanceof TradeCurrency && ((TradeCurrency) item).getNameAndCode()
                        .toUpperCase().startsWith(keys.toUpperCase())) {
                    // TODO scroll to top
                    // It does not appear to be possible in Java 9+ to scroll to entry matching
                    // typed key in a ComboBoxListView
                    // See http://mail.openjdk.java.net/pipermail/openjfx-dev/2015-April/017063.html
                    getSelectionModel().select(item);
                    return;
                } else if (item.toString().toUpperCase().startsWith(keys.toUpperCase())) {
                    // TODO scroll to top
                    getSelectionModel().select(item);
                    return;
                }
            }
        }
    }

    public CustomComboBox() {
        initialize();
    }

    public CustomComboBox(final ObservableList<T> items) {
        super(items);
        initialize();
    }

    private void initialize() {
        setOnKeyReleased(new KeyHandler());
    }
}
