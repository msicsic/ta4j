/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan, 2017-2021 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.indicators.numeric.facades;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.numeric.NumericIndicator;
import org.ta4j.core.num.Num;

/**
 * A facade to create the MACD indicator, signal and histogram.
 *
 * <p>
 * This class creates lightweight "fluent" numeric indicators. These objects are
 * not cached, although they are wrapped around cached EMA objects.
 */
public class MACD {

    private final NumericIndicator line;

    public MACD(BarSeries bs, int n1, int n2) {
        Indicator<Num> price = new ClosePriceIndicator(bs);
        NumericIndicator ema1 = NumericIndicator.of(new EMAIndicator(price, n1));
        NumericIndicator ema2 = NumericIndicator.of(new EMAIndicator(price, n2));
        this.line = ema1.minus(ema2);
    }

    public NumericIndicator line() {
        return line;
    }

    /**
     * Creates an exponential moving average to act as a signal line for this MACD.
     * 
     * @param n the number of periods used to average MACD
     * @return a NumericIndicator wrapped around cached EMA indicator
     */
    public NumericIndicator signal(int n) {
        return NumericIndicator.of(new EMAIndicator(line, n));
    }

    /**
     * Creates an object to calculate the MACD histogram.
     * 
     * @param n the number of periods used for the signal line
     * 
     * @return an object to calculate the difference between this MACD and its
     *         signal
     */
    public NumericIndicator histogram(int n) {
        return line.minus(signal(n));
    }
}
