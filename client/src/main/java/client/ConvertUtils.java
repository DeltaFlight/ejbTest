package client;

/**
 * @author fly
 */
public class ConvertUtils {

    public static String readableNumber(Double value) {
        return value == null ? "null" : readableNumber(value.doubleValue());
    }

    public static String readableNumber(double value) {
        double signum = Math.signum(value);
        value = Math.abs(value);
        if (value < 1e-12) {
            return "0";
        } else if (value < 1e-6) {
            return format(signum, value * 1e9, "n");
        } else if (value < 1e-3) {
            return format(signum, value * 1e6, "mk");
        } else if (value < 1) {
            return format(signum, value * 1e3, "m");
        } else if (value < 1e3) {
            return format(signum, value, "");
        } else if (value < 1e6) {
            return format(signum, value / 1e3, "k");
        } else if (value < 1e9) {
            return format(signum, value / 1e6, "M");
        } else {
            return format(signum, value / 1e9, "G");
        }
    }

    public static Double fromReadableNumber(String readable) {
        try {
            return Double.valueOf(readable);
        } catch (NumberFormatException ex) {
            double multiplier = multiplier(readable.charAt(readable.length() - 1));
            try {
                return multiplier * Double.valueOf(readable.substring(0, readable.length() - 1));
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    private static double multiplier(char suffix) {
        switch (suffix) {
            case 'k':
                return 1000;
            case 'M':
                return 1_000_000;
            case 'm':
                return 0.001;
            default:
                throw new IllegalArgumentException("Unknown suffix " + suffix);
        }
    }

    private static String format(double signum, double value, String measure) {
        return String.format(value < 1.999 ? "%.2f%s" : (value < 20 ? "%.1f%s" : "%.0f%s"), signum * value, measure);
    }

}
