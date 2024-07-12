package com.ldms.amortisation.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConvertToBigDecimal{

        public static BigDecimal getBigDecimal(double monthlyRepayment) {
            BigDecimal roundedMonthlyRepayment = BigDecimal.valueOf(monthlyRepayment).setScale(2, RoundingMode.HALF_EVEN);
            return roundedMonthlyRepayment;
        }
}
