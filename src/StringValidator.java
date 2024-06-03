public class StringValidator {
    protected boolean checkIfLatex(String str) {
        return str.startsWith("$") && str.endsWith("$");
    }

    protected boolean checkIfImg(String str) {
        return str.startsWith("[\\href]") && str.endsWith("}");
    }
}
