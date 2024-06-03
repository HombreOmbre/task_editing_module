public class LatexDesc
    extends DescMethods {
    private String latexTxt;

    public LatexDesc(String latexTxt) {
        this.latexTxt = latexTxt.replace("$", "").replace("←", "\n");
    }

    @Override
    public String getDescValue() {
        return "$" + this.latexTxt + "$";
    }
}
