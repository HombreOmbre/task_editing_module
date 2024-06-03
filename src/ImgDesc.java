public class ImgDesc
    extends DescMethods {
    private String imgHref;

    public ImgDesc(String imgHref) {
        this.imgHref = imgHref
                        .replaceFirst("[{]", "")
                        .replaceFirst("[}]", "")
                        .replaceFirst("[\\href]", "");
    }

    @Override
    public String getDescValue() {
        return "\\href{" + imgHref + "}";
    }
}
