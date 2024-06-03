public enum OptionsOrder {
    ESC, FIRST, SECOND, THIRD, FOURTH;

    static OptionsOrder getOption(int i) {
        return values()[i];
    }
}
