package androidAutoGenerateLayoutRes;

public class AutoGenerateAndroidColors {
    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder();
        String format = "<color name=\"color_%s\">#%s</color>\n";
        StringBuffer color = new StringBuffer();
        int start = 0;
        int end = start + 16;
        for (int i1 = start; i1 < end; i1++) {
            for (int i2 = start; i2 < end; i2++) {
                for (int i3 = start; i3 < end; i3++) {
                    for (int i4 = start; i4 < end; i4++) {
                        for (int i5 = start; i5 < end; i5++) {
                            for (int i6 = start; i6 < end; i6++) {
                                color.setLength(0);
                                color.append(getCharShow(i1)).append(getCharShow(i2)).append(getCharShow(i3)).append(getCharShow(i4)).append(
                                        getCharShow(i5)).append(getCharShow(i6));
                                builder.append(String.format(format, color, color));
                            }
                        }
                    }
                }
            }
        }
        System.out.println(builder);
    }

    public static String getCharShow(int index) {
        if (index < 10) {
            return Integer.valueOf(index).toString();
        } else {
            return String.valueOf((char) (index + 55));
        }
    }
}
