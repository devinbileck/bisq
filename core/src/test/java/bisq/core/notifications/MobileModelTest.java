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

import bisq.common.util.Tuple2;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Slf4j
public class MobileModelTest {

    @Test
    public void testDoesSupportContentAvailable() {
        MobileModel mobileModel = new MobileModel();
        List<Tuple2<String, Boolean>> list = Arrays.asList(
                new Tuple2<>("iPod Touch", false),
                new Tuple2<>("iPod Touch 6", false),
                new Tuple2<>("iPod Touch 7", true),
                new Tuple2<>("iPod Touch 10", true),    // Doesn't yet exist, ensure it will be parsed correctly

                new Tuple2<>("iPod8,1", false),
                new Tuple2<>("iPod9,1", true),
                new Tuple2<>("iPod10,1", true),         // Doesn't yet exist, ensure it will be parsed correctly

                new Tuple2<>("iPhone", false),
                new Tuple2<>("iPhone 3G", false),
                new Tuple2<>("iPhone 3GS", false),
                new Tuple2<>("iPhone 4", false),
                new Tuple2<>("iPhone 4s", false),
                new Tuple2<>("iPhone 5", false),
                new Tuple2<>("iPhone 5c", false),
                new Tuple2<>("iPhone 5s", false),
                new Tuple2<>("iPhone 6", false),
                new Tuple2<>("iPhone 6 Plus", false),
                new Tuple2<>("iPhone 6s", true),
                new Tuple2<>("iPhone 6s Plus", true),
                new Tuple2<>("iPhone SE", true),
                new Tuple2<>("iPhone 7", true),
                new Tuple2<>("iPhone 7 Plus", true),
                new Tuple2<>("iPhone 8", true),
                new Tuple2<>("iPhone 8 Plus", true),
                new Tuple2<>("iPhone X", true),
                new Tuple2<>("iPhone XS", true),
                new Tuple2<>("iPhone XS Max", true),
                new Tuple2<>("iPhone XR", true),
                new Tuple2<>("iPhone 11", true),
                new Tuple2<>("iPhone 11 Pro", true),
                new Tuple2<>("iPhone 11 Pro Max", true),
                new Tuple2<>("iPhone 11S", true), // Doesn't exist, but based on past versioning it is possible
                                                  // Ensure it will be parsed correctly just in case

                new Tuple2<>("iPhone7,1", false),
                new Tuple2<>("iPhone8,1", true),
                new Tuple2<>("iPhone10,1", true),

                new Tuple2<>("iPad", false),
                new Tuple2<>("iPad 4", false),
                new Tuple2<>("iPad 5", true),
                new Tuple2<>("iPad Air", false),
                new Tuple2<>("iPad Air 2", true),
                new Tuple2<>("iPad Mini", false),
                new Tuple2<>("iPad Mini 3", false),
                new Tuple2<>("iPad Mini 4", true),
                new Tuple2<>("iPad Pro 9.7 Inch", true),
                new Tuple2<>("iPad Pro 12.9 Inch", true),
                new Tuple2<>("iPad Pro 10.5 Inch, 2nd Gen", true),
                new Tuple2<>("iPad Pro 12.9 Inch, 2nd Gen", true),
                new Tuple2<>("iPad Pro 11 Inch, 3rd Gen", true),
                new Tuple2<>("iPad Pro 12.9 Inch, 3rd Gen", true),

                new Tuple2<>("iPad4,1", false),
                new Tuple2<>("iPad5,4", true)
        );

        list.forEach(tuple -> {
            assertEquals("tuple: " + tuple, tuple.second, mobileModel.doesSupportContentAvailable(tuple.first));
        });

    }
}
